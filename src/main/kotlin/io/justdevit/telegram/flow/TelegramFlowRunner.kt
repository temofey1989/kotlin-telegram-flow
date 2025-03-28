package io.justdevit.telegram.flow

import com.github.kotlintelegrambot.Bot
import com.github.kotlintelegrambot.bot
import com.github.kotlintelegrambot.dispatch
import com.github.kotlintelegrambot.dispatcher.Dispatcher
import com.github.kotlintelegrambot.dispatcher.callbackQuery
import com.github.kotlintelegrambot.dispatcher.command
import com.github.kotlintelegrambot.dispatcher.handlers.ErrorHandler
import com.github.kotlintelegrambot.dispatcher.message
import com.github.kotlintelegrambot.dispatcher.preCheckoutQuery
import com.github.kotlintelegrambot.entities.BotCommand
import com.github.kotlintelegrambot.entities.ChatId.Companion.fromId
import com.github.kotlintelegrambot.entities.Message
import com.github.kotlintelegrambot.entities.Update
import com.github.kotlintelegrambot.extensions.filters.Filter
import com.github.kotlintelegrambot.extensions.filters.Filter.Command
import com.github.kotlintelegrambot.logging.LogLevel
import io.justdevit.kotlin.boost.eventbus.DefaultEventBus
import io.justdevit.kotlin.boost.eventbus.EventBus
import io.justdevit.kotlin.boost.eventbus.EventListener
import io.justdevit.kotlin.boost.extension.randomString
import io.justdevit.kotlin.boost.extension.runIf
import io.justdevit.kotlin.boost.logging.withCoTracing
import io.justdevit.telegram.flow.listener.TelegramBotErrorHandler
import io.justdevit.telegram.flow.listener.TelegramFlowRunnerErrorLogger
import io.justdevit.telegram.flow.model.CallbackChatContext
import io.justdevit.telegram.flow.model.ChatContext
import io.justdevit.telegram.flow.model.ChatFlow
import io.justdevit.telegram.flow.model.ChatState
import io.justdevit.telegram.flow.model.ChatStateExtractionContext
import io.justdevit.telegram.flow.model.CommandChatContext
import io.justdevit.telegram.flow.model.PreCheckoutChatContext
import io.justdevit.telegram.flow.model.SuccessfulPaymentChatContext
import io.justdevit.telegram.flow.model.TelegramBotExecutionFailure
import io.justdevit.telegram.flow.model.TelegramErrorReceived
import io.justdevit.telegram.flow.model.TextChatContext
import kotlinx.coroutines.supervisorScope
import java.util.concurrent.locks.ReentrantLock

/**
 * [TelegramFlowRunner] is a bot execution manager designed to run defined Chat Flows
 * on a Telegram Bot. It provides lifecycle management, event handling, error handling,
 * and message processing functionality.
 *
 * @property name The unique name assigned to the runner instance, using a default value if unspecified.
 * @property token The Telegram Bot token used to authenticate with the Telegram Bot API.
 * @property flows A list of Chat Flows defining the bot's conversational logic.
 * @property logLevel Defines the logging verbosity for bot operations.
 * @property eventBus The event bus used to publish and consume bot-related events.
 * @property chatStateStore Extracts the chat state during bot execution to maintain stateful interactions.
 * @property errorHandler A handler to process bot errors during execution.
 * @constructor Initializes a [TelegramFlowRunner] instance with the specified properties.
 */
class TelegramFlowRunner(
    val name: String = "telegram-flow-runner-${randomString()}",
    private val token: String,
    private val flows: List<ChatFlow> = emptyList(),
    private val logLevel: LogLevel = LogLevel.Error,
    private val eventBus: EventBus = DefaultEventBus(),
    private val chatStateStore: ChatStateStore = InMemoryChatStateStore(),
    private val errorHandler: TelegramBotErrorHandler = TelegramFlowRunnerErrorLogger(),
) {

    lateinit var bot: Bot
    private var ready = false
    private var started = false
    private val lock = ReentrantLock()
    private val flowExecutor = ChatFlowExecutor(flows, eventBus, chatStateStore)

    /**
     * Starts the Telegram Bot polling if it is not already started.
     */
    fun start() {
        lock.runIf({ !started }) {
            if (!ready) {
                configure()
            }
            bot.startPolling()
            started = true
        }
    }

    /**
     * Stops the Telegram Bot polling if it is currently running.
     */
    fun stop() {
        lock.runIf({ started }) {
            bot.stopPolling()
            started = false
        }
    }

    /**
     * Registers a variable number of event listeners with the event bus.
     *
     * @param listeners The event listeners to be registered.
     */
    fun register(vararg listeners: EventListener<*>) {
        eventBus.register(*listeners)
    }

    /**
     * Unregisters a variable number of event listeners from the event bus.
     *
     * @param listeners The event listeners to be unregistered.
     */
    fun unregister(vararg listeners: EventListener<*>) {
        eventBus.unregister(*listeners)
    }

    private fun configure() {
        if (ready) {
            return
        }
        bot = bot {
            token = this@TelegramFlowRunner.token
            logLevel = this@TelegramFlowRunner.logLevel
            dispatch {
                commands()
                messages()
                callbacks()
                preCheckoutQueries()
                successfulPayments()
                errors()
            }
        }
        bot.setupMenu()
        eventBus.register(errorHandler)
        ready = true
    }

    private fun Dispatcher.errors() {
        addErrorHandler(
            ErrorHandler {
                eventBus.publish(TelegramErrorReceived(error, bot))
            },
        )
    }

    private fun Bot.setupMenu() {
        flows
            .asSequence()
            .map { it.menu }
            .filterNotNull()
            .sortedBy { it.order }
            .map { BotCommand(it.command, it.description) }
            .toList()
            .takeIf { it.isNotEmpty() }
            ?.apply { setMyCommands(this) }
    }

    private fun Dispatcher.commands() {
        flows.forEach { flow ->
            command(flow.id) {
                withErrorHandling(update) {
                    extractChatState(message.chat.id, update)?.also {
                        CommandChatContext(
                            bot = bot,
                            state = it,
                            update = update,
                            command = flow.id,
                            args = args,
                        ).execute()
                    }
                }
            }
        }
        message(Command) {
            bot.deleteMessage(fromId(message.chat.id), message.messageId)
        }
    }

    private fun Dispatcher.messages() {
        message(Filter.Text) {
            withErrorHandling(update) {
                extractChatState(message.chat.id, update)?.also {
                    TextChatContext(
                        bot = bot,
                        state = it,
                        update = update,
                        text = message.text ?: "",
                    ).execute()
                }
            }
        }
    }

    private fun Dispatcher.callbacks() {
        flows.forEach { flow ->
            flow
                .stepMap
                .filter { it.value.suspendable }
                .forEach {
                    callbackQuery(it.value.fullName) {
                        withErrorHandling(update) {
                            val message = callbackQuery.message ?: return@withErrorHandling
                            extractChatState(message.chat.id, update)?.also {
                                CallbackChatContext(
                                    bot = bot,
                                    state = it,
                                    update = update,
                                    callbackQuery = callbackQuery,
                                ).execute()
                            }
                        }
                    }
                }
        }
    }

    private fun Dispatcher.preCheckoutQueries() {
        preCheckoutQuery {
            withErrorHandling(update) {
                val user = update.preCheckoutQuery!!.from
                extractChatState(user.id, update)?.also {
                    PreCheckoutChatContext(
                        bot = bot,
                        state = it,
                        update = update,
                        preCheckoutQuery = preCheckoutQuery,
                    ).execute()
                }
            }
        }
    }

    private fun Dispatcher.successfulPayments() {
        message(
            object : Filter {
                override fun Message.predicate() = successfulPayment != null
            },
        ) {
            withErrorHandling(update) {
                extractChatState(message.chat.id, update)?.also {
                    val successfulPayment = message.successfulPayment ?: return@withErrorHandling
                    SuccessfulPaymentChatContext(
                        bot = bot,
                        state = it,
                        update = update,
                        successfulPayment = successfulPayment,
                    ).execute()
                }
            }
        }
    }

    private suspend fun withErrorHandling(update: Update, action: suspend () -> Unit) {
        withCoTracing {
            try {
                supervisorScope {
                    action()
                }
            } catch (throwable: Throwable) {
                eventBus.coPublish(TelegramBotExecutionFailure(update, throwable))
            }
        }
    }

    private suspend fun extractChatState(chatId: Long, update: Update) = chatStateStore.extract(chatId, ChatStateExtractionContext(update, this))?.enrichMetadata()

    private suspend fun ChatContext.execute() {
        flowExecutor.execute(this)
    }

    private fun ChatState.enrichMetadata() =
        copy(
            metadata = buildMap {
                putAll(metadata)
                put(TELEGRAM_FLOW_RUNNER_NAME_KEY, this@TelegramFlowRunner.name)
            }.toMap(),
        )
}
