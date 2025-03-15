@file:Suppress("unused")

package io.justdevit.telegram.flow.chat

import com.github.kotlintelegrambot.dispatcher.Dispatcher
import com.github.kotlintelegrambot.entities.InlineKeyboardMarkup
import com.github.kotlintelegrambot.entities.Message
import com.github.kotlintelegrambot.entities.ParseMode
import com.github.kotlintelegrambot.entities.ParseMode.MARKDOWN_V2
import com.github.kotlintelegrambot.entities.keyboard.InlineKeyboardButton.CallbackData
import com.github.kotlintelegrambot.entities.payments.LabeledPrice
import com.github.kotlintelegrambot.entities.payments.PaymentInvoiceInfo
import com.github.kotlintelegrambot.types.TelegramBotResult
import com.github.kotlintelegrambot.types.TelegramBotResult.Error
import io.justdevit.telegram.flow.CALLBACK_DATA_DELIMITER
import io.justdevit.telegram.flow.CALLBACK_SUSPENDED_STEP_MARKER
import io.justdevit.telegram.flow.SHORT_MESSAGE_LIFETIME
import io.justdevit.telegram.flow.SUSPENDED_STEP_MARKER
import io.justdevit.telegram.flow.TelegramException
import io.justdevit.telegram.flow.extension.sendPdf
import io.justdevit.telegram.flow.i18n.T
import kotlinx.coroutines.delay
import java.math.BigDecimal
import kotlin.time.Duration

/**
 * Creates a [ChatFlow] using the given string as its ID and applying the configuration defined in the specified block.
 *
 * @param block A lambda with a receiver of type [ChatFlowBuilder] that defines the steps and optional menu configuration of the chat flow.
 * @return A [ChatFlow] instance initialized with the provided ID and the configuration defined in the block.
 */
context(Dispatcher)
operator fun String.minus(block: ChatFlowBuilder.() -> Unit): ChatFlow = chatFlow(this, null, block)

/**
 * A utility function to create and build a chat flow using the provided configuration.
 *
 * @param name The name of the chat flow, which is used as the command for the associated menu.
 * @param description An optional description for the chat flow menu item. Defaults to the same value as the `name`.
 * @param order An optional integer specifying the order of the chat flow in the menu. Defaults to 0.
 * @param block A lambda with a receiver of type [ChatFlowBuilder] used to define the structure and behavior of the chat flow.
 * @return A completed and configured [ChatFlow] instance based on the specified parameters and builder configuration.
 */
fun chatFlow(
    name: String,
    description: String = name,
    order: Int = 0,
    block: ChatFlowBuilder.() -> Unit,
) = chatFlow(
    name = name,
    menu = ChatMenu(command = name, description = description, order = order),
    block = block,
)

/**
 * Creates and builds a chat flow using the specified configuration and steps defined in the builder block.
 *
 * @param name The name or identifier of the chat flow, which should be unique and descriptive.
 * @param menu An optional menu of type [ChatMenu] that can be associated with the chat flow for user interactions.
 *             Defaults to `null` if not provided.
 * @param block A lambda with a receiver of [ChatFlowBuilder] to define the steps and behavior of the chat flow.
 *              This block is invoked to configure the chat flow before it is built.
 * @return A completed and configured [ChatFlow] instance based on the specified parameters and builder configuration.
 */
fun chatFlow(
    name: String,
    menu: ChatMenu? = null,
    block: ChatFlowBuilder.() -> Unit,
) = ChatFlowBuilder(name, menu).also(block).build()

fun ChatStepContext.message(
    parseMode: ParseMode? = MARKDOWN_V2,
    saveResponseId: Boolean = true,
    supplier: () -> String,
): Message {
    val text = supplier()
    ChatStepContext.log.debug { "Sending text to chat [${state.chatId}]: $text" }
    return bot
        .sendMessage(
            chatId = state.botChatId,
            text = text,
            parseMode = parseMode,
        ).storeSuccessful(saveResponseId)
}

/**
 * Sends a short-lived message to the user and deletes it after a specified duration.
 *
 * @param displayDuration The duration for which the message will remain visible before being deleted. Defaults to [SHORT_MESSAGE_LIFETIME].
 * @param parseMode The parse mode used for formatting the message text. Defaults to [MARKDOWN_V2].
 * @param supplier A supplier function providing the content of the message.
 */
suspend fun ChatStepContext.shortMessage(
    displayDuration: Duration = SHORT_MESSAGE_LIFETIME,
    parseMode: ParseMode? = MARKDOWN_V2,
    supplier: () -> String,
) = message(parseMode = parseMode, saveResponseId = false, supplier = supplier).apply {
    delay(displayDuration)
    bot.deleteMessage(chatId = this@shortMessage.state.botChatId, messageId = messageId)
}

/**
 * Sends location data to the chat as a message.
 *
 * @param coordinates The geographical coordinates to send, including latitude, longitude, and address.
 * @param saveResponseId Indicates whether the message ID of sent location should be stored in the flow's data (default is `true`).
 * @return Sent location message as a [Message] object.
 */
fun ChatStepContext.location(coordinates: GeoCoordinates, saveResponseId: Boolean = true): Message {
    ChatStepContext.log.debug { "Sending location to chat [${state.chatId}]: $coordinates" }
    return bot
        .sendLocation(
            chatId = state.botChatId,
            latitude = coordinates.latitude.toFloat(),
            longitude = coordinates.longitude.toFloat(),
        ).let {
            val message = it
                .first
                ?.body()
                ?.result
                ?: throw (
                    it.second?.cause
                        ?: IllegalStateException("No exception for location message.")
                )
            if (saveResponseId) {
                state.flowInfo?.flowData += (step.name to ServerMessageId(message.messageId))
            }
            message
        }
}

/**
 * Retrieves the chat flow data of the specified type for the current step context.
 *
 * This method casts the current flow data stored in the chat state to the specified type [T].
 * It is expected that the type [T] is a subtype of [ChatFlowData].
 *
 * @param T The type of data extending [ChatFlowData].
 * @return The chat flow data associated with the current chat flow, casted to type [T].
 * @throws ClassCastException if the flow data cannot be cast to type [T].
 */
inline fun <reified T : ChatFlowData> ChatStepContext.data(): T = (state.flowInfo?.flowData ?: throw IllegalStateException("No flow data exist in current chat state.")) as T

/**
 * Executes an action on the chat flow data of the current step.
 *
 * This function provides access to the specific implementation of [ChatFlowData] associated with
 * the current step in the chat flow and allows performing operations on it.
 *
 * @param T The type of data extending [ChatFlowData].
 * @param action A lambda function that operates on the chat flow data of type [T].
 */
inline fun <reified T : ChatFlowData> ChatStepContext.data(action: T.() -> Unit) {
    action(data())
}

/**
 * Sends a question with options to the chat and builds an inline keyboard for user responses.
 *
 * @param question The question or prompt to display to the user.
 * @param parseMode The parse mode for formatting the message text, defaults to [MARKDOWN_V2].
 * @param saveResponseId A flag indicating whether to save the response message ID, defaults to `true`.
 * @param block A lambda configuring the options for the chat using a [ChatOptionsBuilder].
 * @return The message object representing sent message.
 */
fun ChatStepContext.options(
    question: String,
    parseMode: ParseMode? = MARKDOWN_V2,
    saveResponseId: Boolean = true,
    block: ChatOptionsBuilder.() -> Unit,
): Message {
    val select = ChatOptionsBuilder(question)
        .also {
            it.block()
        }.build()
    ChatStepContext.log.debug { "Sending options to chat [${state.chatId}]: ${select.items.joinToString { it.joinToString { item -> item.value } }}" }

    val callbackDataPrefix = "${step.fullName}${if (step.suspendable) "" else CALLBACK_SUSPENDED_STEP_MARKER}${CALLBACK_DATA_DELIMITER}"
    val callbacks = select
        .items
        .map { list ->
            list.map {
                CallbackData(callbackData = "$callbackDataPrefix${it.value}", text = it.label)
            }
        }

    return bot
        .sendMessage(
            chatId = state.botChatId,
            text = select.question,
            parseMode = parseMode,
            replyMarkup = InlineKeyboardMarkup.create(*callbacks.toTypedArray()),
        ).storeSuccessful(saveResponseId)
}

/**
 * Sends an invoice message to the current chat.
 *
 * @param identifier A unique identifier representing the invoice payload.
 * @param title The title of the invoice.
 * @param description A brief description of the invoice.
 * @param price The price of the invoice item.
 * @param currency The currency of the invoice (e.g., `USD`, `EUR`).
 * @param providerToken The provider token required for payment processing.
 * @param saveResponseId Specifies whether to save the response message ID in the current chat state. Default is `true`.
 * @return The message containing the invoice that was sent to the chat.
 */
fun ChatStepContext.sendInvoice(
    identifier: Any,
    title: String,
    description: String,
    price: BigDecimal,
    currency: String,
    providerToken: String,
    saveResponseId: Boolean = true,
): Message {
    ChatStepContext.log.debug { "Sending invoice to chat [${state.chatId}]..." }
    return bot
        .sendInvoice(
            chatId = state.botChatId,
            paymentInvoiceInfo = PaymentInvoiceInfo(
                title = title,
                description = description,
                payload = identifier.toString(),
                providerToken = providerToken,
                prices = listOf(
                    LabeledPrice(
                        amount = price.multiply(100.toBigDecimal()).toBigInteger(),
                        label = "$currency ${price.stripTrailingZeros()}",
                    ),
                ),
                currency = currency,
                startParameter = identifier.toString(),
                isFlexible = false,
            ),
        ).storeSuccessful(saveResponseId)
}

/**
 * Sends a PDF document within the current chat context.
 *
 * @param name The name of the PDF file to be sent.
 * @param data The byte array containing the PDF content.
 */
fun ChatStepContext.sendPdf(name: String, data: ByteArray) =
    bot.sendPdf(
        chatId = state.botChatId,
        name = name,
        data = data,
    )

/**
 * Confirms a successful pre-checkout query in the chat flow.
 * This method sends a positive acknowledgment to the Telegram API for the pre-checkout query,
 * indicating that the payment process can proceed.
 *
 * @param preCheckoutQueryId The unique identifier for the pre-checkout query to be confirmed.
 */
fun ChatStepContext.confirmCheckout(preCheckoutQueryId: String) =
    bot.answerPreCheckoutQuery(
        preCheckoutQueryId = preCheckoutQueryId,
        ok = true,
    )

/**
 * Rejects a pre-checkout query, indicating that the checkout process cannot proceed.
 *
 * @param preCheckoutQueryId The unique identifier of the pre-checkout query to be rejected.
 * @param errorMessageBuilder A lambda function that constructs the error message to explain why
 * the checkout was rejected.
 */
fun ChatStepContext.rejectCheckout(preCheckoutQueryId: String, errorMessageBuilder: () -> String) =
    bot.answerPreCheckoutQuery(
        preCheckoutQueryId = preCheckoutQueryId,
        ok = false,
        errorMessage = errorMessageBuilder(),
    )

/**
 * Redirects the chat flow to the specified step using its name.
 *
 * @param stepName The name of the step to navigate to. Must be non-blank.
 * @throws IllegalArgumentException if the step name is blank.
 * @throws Goto to signal the chat flow engine to redirect to the specified step.
 */
context(ChatStepContext)
fun goto(stepName: String) {
    require(stepName.isNotBlank()) {
        "Step name cannot be blank."
    }
    throw Goto(stepName)
}

/**
 * Skips the current chat step and instructs the chat flow to proceed to the next step.
 *
 * @return This method does not return a value and always throws the [GoNext] exception
 *         to signal the flow to move to the next step in the chat.
 */
context(ChatStepContext)
fun goNext(): Unit = throw GoNext

/**
 * Moves to the previous step within the current chat flow context. This function is executed
 * in the scope of a [ChatStepContext] and utilizes the contextual information to manage the
 * flow progression.
 *
 * @return This method does not return a value and always throws the [GoPrevious] exception
 *         to signal the flow to move to the previous step in the chat.
 */
context(ChatStepContext)
fun goPrevious(): Unit = throw GoPrevious

/**
 * Initiates a new chat flow by its name within the current chat step context.
 * Throws a [StartFlow] exception to transition to the specified flow.
 *
 * @param name The name of the flow to start.
 * @return This method does not return a value, as it throws a [StartFlow] exception.
 */
context(ChatStepContext)
fun startFlow(name: String): Unit = throw StartFlow(flowName = name)

/**
 * Terminates the current chat flow execution.
 *
 * @throws StopFlow This exception is always thrown to indicate the flow termination.
 */
context(ChatStepContext)
fun stopFlow(): Unit = throw StopFlow

/**
 * Executes the given block of code within the context of the current chat step and provides a fallback mechanism
 * in case of an exception. If an unexpected exception occurs, it triggers a redirection to the specified step.
 *
 * @param stepName The name of the current step, extracted by removing the suspended step marker. Defaults to the
 *                 current step's name unless explicitly provided.
 * @param block The block of code to be executed within the context of the current step.
 * @throws Goto When an exception occurs that requires a redirection to a specific step.
 * @throws GoPrevious Indicates a transition to the previous step.
 * @throws GoNext Indicates a transition to the next step.
 * @throws StopFlow Indicates the termination of the current flow.
 * @throws StartFlow Indicates the start of a new flow execution.
 */
context(T)
inline fun <T : SuspendableChatStepContext> withFallback(stepName: String = step.name.removeSuffix(SUSPENDED_STEP_MARKER), block: () -> Unit) =
    try {
        block()
    } catch (throwable: Throwable) {
        when (throwable) {
            is Goto, GoPrevious, GoNext, StopFlow -> {
                throw throwable
            }

            is StartFlow -> {
                throw throwable
            }

            else -> {
                ChatStepContext.log.debug(throwable) { "Step fallback to step [$stepName]. Message: ${throwable.message}" }
                throw Goto(stepName)
            }
        }
    }

/**
 * Stores the result of a successful Telegram bot operation and optionally saves the response message ID
 * to the current chat flow data. In case of an error, a [TelegramException] is thrown.
 *
 * @param saveResponseId A boolean that determines whether to save the response message ID in the flow data.
 * @return The resulting [Message] obtained from the successful Telegram bot operation.
 */
context(ChatStepContext)
fun TelegramBotResult<Message>.storeSuccessful(saveResponseId: Boolean): Message {
    onSuccess {
        if (saveResponseId) {
            state.flowInfo?.flowData += (step.name to ServerMessageId(it.messageId))
            ChatStepContext.log.debug {
                val messageIds = state
                    .flowInfo
                    ?.flowData
                    ?.stepMessageIds[step.name]
                    ?.map { id -> id.toString() } ?: emptyList()
                "New response message [${it.messageId}] has been registered for step [${step.name}]. Registered massages: $messageIds"
            }
        }
    }
    return when (this) {
        is TelegramBotResult.Success -> value
        is Error -> throw TelegramException(error = this)
    }
}

/**
 * Deletes messages associated with a specific step and message type from the chat state.
 *
 * @param T The type of messages to clear, which must implement [MessageId].
 * @param stepName The name of the step whose messages should be cleared. Defaults to the name of the current step.
 */
inline fun <reified T : MessageId> ChatStepContext.clearStepMessages(stepName: String = step.name) {
    with(
        state
            .flowInfo
            ?.flowData
            ?.stepMessageIds[stepName] ?: return,
    ) {
        filterIsInstance<T>().forEach {
            bot.deleteMessage(chatId = state.botChatId, messageId = it.value)
            ChatStepContext.log.debug { "Message [${it.value}] has been deleted for chat: [${state.chatId}]" }
        }
        clear()
    }
}

/**
 * Clears messages associated with the previous step in the current chat flow.
 *
 * This function identifies the previous step in the flow relative to the current step
 * and deletes all messages of type [T], which implements [MessageId], associated
 * with that step in the chat state.
 *
 * @param T The type of messages to clear, which must implement [MessageId].
 */
inline fun <reified T : MessageId> ChatStepContext.clearPreviousStepMessages() =
    step.flow
        .stepMap[step.name]
        ?.previous
        ?.name
        ?.apply { clearStepMessages<T>(this) }

/**
 * Clears all messages related to the current chat flow by invoking the `clearMessages` function
 * on the flow data stored in the chat state. This is typically used to manage the cleanup of
 * messages generated during chat flow execution.
 *
 * This method ensures that all messages tied to the current context are deleted and the
 * associated message identifiers are removed from the flow data.
 */
fun ChatStepContext.clearFlowMessages() {
    state.flowInfo
        ?.flowData
        ?.clearMessages()
}

/**
 * Clears all messages associated with the current steps of a chat flow.
 */
context(ChatStepContext)
fun ChatFlowData.clearMessages() {
    stepMessageIds.values.flatten().forEach {
        bot.deleteMessage(chatId = state.botChatId, messageId = it.value)
        ChatStepContext.log.debug { "Message [$it] has been deleted for chat: [${state.chatId}]" }
    }
    stepMessageIds.clear()
}

/**
 * Retrieves a localized text string based on a key specific to the chat's state and language.
 *
 * @param key The key used to identify the desired text string.
 * @param paramsBuilder A lambda function to define key-value pairs of parameters for placeholder replacement
 * in the localized text. Defaults to an empty parameters map.
 */
context(ChatStepContext)
@Suppress("FunctionName")
fun T(key: String, paramsBuilder: MutableMap<String, String>.() -> Unit = {}) = T(key, state.language, paramsBuilder)
