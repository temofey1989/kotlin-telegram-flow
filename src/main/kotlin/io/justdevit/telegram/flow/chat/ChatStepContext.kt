package io.justdevit.telegram.flow.chat

import com.github.kotlintelegrambot.Bot
import com.github.kotlintelegrambot.entities.CallbackQuery
import com.github.kotlintelegrambot.entities.Update
import com.github.kotlintelegrambot.entities.payments.PreCheckoutQuery
import com.github.kotlintelegrambot.entities.payments.SuccessfulPayment
import io.justdevit.kotlin.boost.logging.Logging
import io.justdevit.telegram.flow.extension.value

/**
 * Represents the context of a chat step during a chat flow execution.
 * Provides access to the chat's state, the current bot instance, the incoming update,
 * and the associated chat flow and step. This interface is used to manage the flow's
 * progression and execute actions in the context of the current step.
 *
 * @property bot The bot instance handling the chat.
 * @property update The incoming update triggering this step in the flow.
 * @property state The current state of the chat, capturing details like chat ID, language, and metadata.
 * @property step The current step being executed within the chat flow.
 * @property flow The chat flow that the current step belongs to.
 */
sealed interface ChatStepContext {
    val bot: Bot
    val update: Update
    val state: ChatState
    val step: ChatStep

    companion object : Logging()

    val flow: ChatFlow
        get() = step.flow
}

/**
 * A specialized implementation of [ChatStepContext] that represents the context of a chat flow step
 * triggered by a command. This class provides additional information specific to command-based interactions,
 * such as the command used and its associated arguments.
 *
 * @property command The command that triggered this step in the chat flow.
 * @property args The list of arguments provided with the command. Defaults to an empty list if no arguments are supplied.
 *
 * @param bot The bot instance handling the chat.
 * @param update The update object representing the incoming event or message.
 * @param state The current state of the chat, including flow and step details.
 * @param step The current step being executed in the chat flow.
 * @param command The command that initiated the chat flow transition to this step.
 * @param args The list of arguments associated with the command.
 */
data class CommandChatStepContext(
    override val bot: Bot,
    override val update: Update,
    override val state: ChatState,
    override val step: ChatStep,
    val command: String,
    val args: List<String> = emptyList(),
) : ChatStepContext {

    /**
     * Initializes a new instance of [CommandChatStepContext] from a [CommandChatContext]
     * and a [ChatStep]. The flow name and step name in the state are updated as part of this initialization.
     *
     * @param context The [CommandChatContext] containing the base information for this step.
     * @param step The [ChatStep] that represents the context of the current flow step.
     */
    constructor(context: CommandChatContext, step: ChatStep) : this(
        bot = context.bot,
        update = context.update,
        state = context.state.copy(
            flowName = step.flow.id,
            stepName = step.name,
        ),
        step = step,
        command = context.command,
        args = context.args,
    )
}

/**
 * Represents the context of a chat step during the continuation of a chat flow.
 *
 * @property bot The bot instance handling the chat for this step.
 * @property update The incoming update triggering this step in the flow.
 * @property state The updated state of the chat reflecting the current flow and step.
 * @property step The specific step being executed within the chat flow.
 */
data class ContinuationChatStepContext(
    override val bot: Bot,
    override val update: Update,
    override val state: ChatState,
    override val step: ChatStep,
) : ChatStepContext {

    /**
     * Initializes a new instance of [ContinuationChatStepContext] from a [ChatStepContext]
     * and a [ChatStep]. The flow name and step name in the state are updated as part of this initialization.
     *
     * @param context The [ChatStepContext] containing the base information for this step.
     * @param nextStep The [ChatStep] that represents the context of the current flow step.
     */
    constructor(context: ChatStepContext, nextStep: ChatStep) : this(
        bot = context.bot,
        update = context.update,
        state = context.state.copy(
            flowName = nextStep.flow.id,
            stepName = nextStep.name,
        ),
        step = nextStep,
    )
}

/**
 * Represents a suspendable context in the execution of a chat flow step.
 */
sealed interface SuspendableChatStepContext : ChatStepContext

/**
 * Represents the context of a chat step executed in response to a callback query.
 *
 * @property bot The bot instance managing the interaction.
 * @property update The update object containing details of the incoming update.
 * @property state The current state of the chat, including flow, step, and metadata.
 * @property step The [ChatStep] representing the specific step being executed in the flow.
 * @property callbackQuery The callback query that triggered the current interaction.
 * @property value The value associated with the callback query.
 */
data class CallbackChatStepContext(
    override val bot: Bot,
    override val update: Update,
    override val state: ChatState,
    override val step: ChatStep,
    val callbackQuery: CallbackQuery,
    val value: String,
) : SuspendableChatStepContext {

    /**
     * Initializes a new instance of [CallbackChatStepContext] from a [ChatStepContext]
     * and a [ChatStep]. The flow name and step name in the state are updated as part of this initialization.
     *
     * @param context The [CallbackChatContext] containing the base information for this step.
     * @param step The [ChatStep] that represents the context of the current flow step.
     */
    constructor(context: CallbackChatContext, step: ChatStep) : this(
        bot = context.bot,
        update = context.update,
        state = context.state.copy(
            flowName = step.flow.id,
            stepName = step.name,
        ),
        step = step,
        callbackQuery = context.callbackQuery,
        value = context.callbackQuery.value,
    )
}

/**
 * Represents the context for handling a specific step in a chat flow during the pre-checkout process.
 *
 * @property bot The bot instance managing the chat interaction.
 * @property update The update object containing details of the interaction.
 * @property state The current state of the chat, including metadata, localization, and flow information.
 * @property step The step within the current chat flow at which this context is being executed.
 * @property preCheckoutQuery The pre-checkout query that is being processed in this context.
 */
data class PreCheckoutChatStepContext(
    override val bot: Bot,
    override val update: Update,
    override val state: ChatState,
    override val step: ChatStep,
    val preCheckoutQuery: PreCheckoutQuery,
) : SuspendableChatStepContext {

    /**
     * Initializes a new instance of [PreCheckoutChatStepContext] from a [PreCheckoutChatContext]
     * and a [ChatStep]. The flow name and step name in the state are updated as part of this initialization.
     *
     * @param context The [PreCheckoutChatContext] containing the base information for this step.
     * @param step The [ChatStep] that represents the context of the current flow step.
     */
    constructor(context: PreCheckoutChatContext, step: ChatStep) : this(
        bot = context.bot,
        update = context.update,
        state = context.state.copy(
            flowName = step.flow.id,
            stepName = step.name,
        ),
        step = step,
        preCheckoutQuery = context.preCheckoutQuery,
    )
}

/**
 * Represents the context of a successfully processed payment within a specific step of a chat flow.
 *
 * @property bot The bot instance responsible for managing the current interaction.
 * @property update The update object containing the update data associated with the interaction.
 * @property state The current state of the chat, including flow and step metadata.
 * @property step The current step within the chat flow that is being processed.
 * @property successfulPayment Details of the successfully processed payment transaction.
 */
data class SuccessfulPaymentChatStepContext(
    override val bot: Bot,
    override val update: Update,
    override val state: ChatState,
    override val step: ChatStep,
    val successfulPayment: SuccessfulPayment,
) : SuspendableChatStepContext {

    /**
     * Initializes a new instance of [SuccessfulPaymentChatStepContext] from a [SuccessfulPaymentChatContext]
     * and a [ChatStep]. The flow name and step name in the state are updated as part of this initialization.
     *
     * @param context The [SuccessfulPaymentChatContext] containing the base information for this step.
     * @param step The [ChatStep] that represents the context of the current flow step.
     */
    constructor(context: SuccessfulPaymentChatContext, step: ChatStep) : this(
        bot = context.bot,
        update = context.update,
        state = context.state.copy(
            flowName = step.flow.id,
            stepName = step.name,
        ),
        step = step,
        successfulPayment = context.successfulPayment,
    )
}

/**
 * Represents the context of a specific step in a text-based chat flow.
 *
 * @property bot The bot instance responsible for handling the current chat interaction.
 * @property update The update object containing the details of the update triggering this step.
 * @property state The current state of the chat, including flow and step contextual information.
 * @property step The current step in the flow being executed.
 * @property text The text content of the message in the chat interaction.
 */
data class TextChatStepContext(
    override val bot: Bot,
    override val update: Update,
    override val state: ChatState,
    override val step: ChatStep,
    val text: String,
) : SuspendableChatStepContext {

    /**
     * Initializes a new instance of [TextChatStepContext] from a [TextChatContext]
     * and a [ChatStep]. The flow name and step name in the state are updated as part of this initialization.
     *
     * @param context The [TextChatContext] containing the base information for this step.
     * @param step The [ChatStep] that represents the context of the current flow step.
     */
    constructor(context: TextChatContext, step: ChatStep) : this(
        bot = context.bot,
        update = context.update,
        state = context.state.copy(
            flowName = step.flow.id,
            stepName = step.name,
        ),
        step = step,
        text = context.text,
    )
}
