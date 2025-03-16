package io.justdevit.telegram.flow.dsl

import com.github.kotlintelegrambot.logging.LogLevel
import io.justdevit.kotlin.boost.eventbus.EventListener
import io.justdevit.kotlin.boost.extension.randomString
import io.justdevit.telegram.flow.ChatStateStore
import io.justdevit.telegram.flow.InMemoryChatStateStore
import io.justdevit.telegram.flow.TelegramFlowRunner
import io.justdevit.telegram.flow.listener.TelegramBotErrorHandler
import io.justdevit.telegram.flow.listener.TelegramFlowRunnerErrorLogger
import io.justdevit.telegram.flow.model.ChatEvent
import io.justdevit.telegram.flow.model.ChatFlow
import io.justdevit.telegram.flow.model.TelegramBotExecutionFailure

/**
 * Constructs and runs an instance of [TelegramFlowRunner] based on the provided configuration.
 *
 * @param token The Bot token used to authenticate with the Telegram Bot API.
 * @param name The name of the flow runner. Defaults to a randomly generated name prefixed with "telegram-flow-runner".
 * @param logLevel The logging level for the flow runner. Defaults to `LogLevel.Error`.
 * @param autoStartup Indicates whether the runner should automatically start after creation. Defaults to `true`.
 * @param configure A lambda function to apply additional custom configurations to the [TelegramFlowRunnerBuilder].
 * @return A configured instance of [TelegramFlowRunner].
 */
fun runner(
    token: String,
    name: String = "telegram-flow-runner-${randomString()}",
    logLevel: LogLevel = LogLevel.Error,
    autoStartup: Boolean = true,
    configure: TelegramFlowRunnerBuilder.() -> Unit = {},
) = TelegramFlowRunnerBuilder(
    token = token,
    name = name,
    autoStartup = autoStartup,
    logLevel = logLevel,
).apply { configure() }
    .build()

/**
 * This builder class is used to configure and construct an instance of [TelegramFlowRunner].
 *
 * @param token The bot token used to authenticate with the Telegram Bot API.
 * @param name The name of the flow runner. Defaults to a randomly generated name prefixed with "telegram-flow-runner".
 * @param autoStartup Determines if the flow runner should start automatically after being built. Defaults to `true`.
 * @param logLevel The logging level of the runner. Defaults to `LogLevel.Error`.
 */
class TelegramFlowRunnerBuilder(
    private val token: String,
    private val name: String = "telegram-flow-runner-${randomString()}",
    private val autoStartup: Boolean = true,
    private val logLevel: LogLevel = LogLevel.Error,
) {

    private val listeners = mutableListOf<EventListener<*>>()
    private val flows = mutableListOf<ChatFlow>()
    var chatStateStore: ChatStateStore = InMemoryChatStateStore()
    var errorHandler: TelegramBotErrorHandler = TelegramFlowRunnerErrorLogger()

    /**
     * Adds one or more event listeners to the [TelegramFlowRunnerBuilder].
     *
     * @param listeners A variable number of event listeners to be added.
     * @return The current instance of [TelegramFlowRunnerBuilder] with the added listeners.
     */
    fun addListener(vararg listeners: EventListener<*>): TelegramFlowRunnerBuilder {
        this.listeners += listeners
        return this
    }

    /**
     * Adds the current event listener to the collection of listeners.
     *
     * This operator function is a shorthand for adding a single event listener
     * to the `listeners` collection within the [TelegramFlowRunnerBuilder].
     */
    @Suppress("UNCHECKED_CAST")
    operator fun EventListener<*>.unaryPlus() {
        listeners += this@unaryPlus
    }

    /**
     * Adds one or more [ChatFlow] instances to the flows collection.
     *
     * @param flows The [ChatFlow] instances to be added to the Telegram Flow Runner.
     * @return The current instance of [TelegramFlowRunnerBuilder] with the added flows.
     */
    fun addFlow(vararg flows: ChatFlow): TelegramFlowRunnerBuilder {
        this.flows += flows
        return this
    }

    /**
     * Adds the current [ChatFlow] to the collection of flows in the [TelegramFlowRunnerBuilder].
     *
     * This operator function provides a shorthand for including a [ChatFlow] in the `flows` collection
     * within the [TelegramFlowRunnerBuilder].
     */
    operator fun ChatFlow.unaryPlus() {
        flows += this@unaryPlus
    }

    /**
     * Adds one or more [ChatFlowBuilder] instances and builds them into flows for the current instance
     * of [TelegramFlowRunnerBuilder].
     *
     * @param builders A variable number of [ChatFlowBuilder] instances to be converted and added as flows.
     * @return The current instance of [TelegramFlowRunnerBuilder] with the resulting flows added.
     */
    fun addFlow(vararg builders: ChatFlowBuilder): TelegramFlowRunnerBuilder {
        this.flows += builders.map { it.build() }
        return this
    }

    /**
     * Adds the built result of the current [ChatFlowBuilder] to the list of flows in the parent builder.
     *
     * This operator function allows a [ChatFlowBuilder] instance to be converted into a [ChatFlow] by
     * invoking its `build` method, and then appending the resulting flow to the `flows` collection
     * of the enclosing [TelegramFlowRunnerBuilder] instance.
     */
    operator fun ChatFlowBuilder.unaryPlus() {
        flows += this@unaryPlus.build()
    }

    /**
     * Registers an action to be executed for chat events of the specified type [T].
     *
     * @param T The specific type of `ChatEvent` that the action should handle.
     * @param action A lambda function that defines the action to be performed on the chat event of type [T].
     * @return The current instance of [TelegramFlowRunnerBuilder] for further configuration or chaining.
     */
    @Suppress("UNCHECKED_CAST")
    inline fun <reified T : ChatEvent> on(crossinline action: (T) -> Unit): TelegramFlowRunnerBuilder {
        addListener(
            object : EventListener<T> {
                override val supportedClass = T::class.java

                override suspend fun onEvent(event: T) = action(event)
            } as EventListener<ChatEvent>,
        )
        return this
    }

    /**
     * Registers an action to handle errors occurring during the execution of Telegram Flow operations.
     *
     * @param action A lambda function invoked with a [TelegramBotExecutionFailure] instance,
     * representing the error details such as the failed update and the associated throwable.
     * @return The current instance of [TelegramFlowRunnerBuilder] for further configuration or chaining.
     */
    @Suppress("UNCHECKED_CAST")
    inline fun onError(crossinline action: (TelegramBotExecutionFailure) -> Unit): TelegramFlowRunnerBuilder {
        addListener(
            object : TelegramBotErrorHandler() {
                override suspend fun onEvent(event: TelegramBotExecutionFailure) = action(event)
            } as EventListener<ChatEvent>,
        )
        return this
    }

    /**
     * Builds and returns an instance of [TelegramFlowRunner] based on the configuration provided
     * in the [TelegramFlowRunnerBuilder].
     *
     * The method initializes a [TelegramFlowRunner] instance using the builder's properties,
     * such as token, name, log level, listeners, flows, chat state extractor, error handler,
     * and execution listener. It also registers the provided listeners and optionally starts
     * the runner if the auto-startup flag is enabled.
     *
     * @return A configured and optionally started instance of [TelegramFlowRunner].
     */
    fun build(): TelegramFlowRunner {
        val runner = TelegramFlowRunner(
            token = token,
            name = name,
            logLevel = logLevel,
            chatStateStore = chatStateStore,
            errorHandler = errorHandler,
            flows = flows.toList(),
        )
        runner.register(*listeners.toTypedArray())
        return runner.also {
            if (autoStartup) {
                it.start()
            }
        }
    }
}
