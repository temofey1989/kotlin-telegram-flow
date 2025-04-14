package io.justdevit.telegram.flow.model

import com.github.kotlintelegrambot.Bot
import com.github.kotlintelegrambot.entities.CallbackQuery
import com.github.kotlintelegrambot.entities.Update
import com.github.kotlintelegrambot.entities.payments.PreCheckoutQuery
import com.github.kotlintelegrambot.entities.payments.SuccessfulPayment
import io.justdevit.kotlin.boost.eventbus.Event

/**
 * Represents the context of an ongoing chat interaction.
 *
 * This interface is used to encapsulate information about the bot, the update being processed,
 * and the current state of the chat.
 *
 * @property bot The bot instance associated with the interaction.
 * @property update The update object containing the event or message triggering the chat interaction.
 * @property state The current state of the chat.
 */
sealed interface ChatContext {
    val bot: Bot
    val update: Update
    val state: ChatState
}

/**
 * Represents the context of a chat interaction specifically triggered by a command.
 *
 * @property bot The bot instance associated with the interaction.
 * @property update The update object containing the event or message triggering the chat interaction.
 * @property state The current state of the chat.
 * @property command The command invoked in the chat.
 * @property args The list of arguments provided with the command. Defaults to an empty list if no arguments are supplied.
 */
data class CommandChatContext(
    override val bot: Bot,
    override val update: Update,
    override val state: ChatState,
    val command: String,
    val args: List<String> = emptyList(),
) : ChatContext

/**
 * Defines a suspendable context for handling chat interactions.
 *
 * This interface extends the [ChatContext] interface, adding support for suspendable
 * operations within chat interactions.
 */
sealed interface SuspendableChatContext : ChatContext

/**
 * Represents the context of a chat interaction initiated by a callback query.
 *
 * @property bot The bot instance handling the interaction.
 * @property update The update object containing the update details.
 * @property state The current state of the chat, including metadata and contextual information.
 * @property callbackQuery The callback query object that triggered this interaction.
 */
data class CallbackChatContext(
    override val bot: Bot,
    override val update: Update,
    override val state: ChatState,
    val callbackQuery: CallbackQuery,
) : SuspendableChatContext

/**
 * Represents the context for handling a pre-checkout query in a chat interaction.
 *
 * @property bot The bot instance handling the interaction.
 * @property update The update object containing the update details.
 * @property state The current state of the chat, including metadata and contextual information.
 * @property preCheckoutQuery The pre-checkout query associated with this interaction.
 */
data class PreCheckoutChatContext(
    override val bot: Bot,
    override val update: Update,
    override val state: ChatState,
    val preCheckoutQuery: PreCheckoutQuery,
) : SuspendableChatContext

/**
 * Represents the context of a successful payment within a chat interaction.
 *
 * @property bot The bot instance handling the interaction.
 * @property update The update object containing the update details.
 * @property state The current state of the chat, including metadata and contextual information.
 * @property successfulPayment Details of the successful payment transaction.
 */
data class SuccessfulPaymentChatContext(
    override val bot: Bot,
    override val update: Update,
    override val state: ChatState,
    val successfulPayment: SuccessfulPayment,
) : SuspendableChatContext

/**
 * Represents the context of a text-based chat interaction.
 *
 * @property bot The bot instance handling the interaction.
 * @property update The update object containing the update details.
 * @property state The current state of the chat, including metadata and contextual information.
 * @property text The text message content within the chat interaction.
 */
data class TextChatContext(
    override val bot: Bot,
    override val update: Update,
    override val state: ChatState,
    val text: String,
) : SuspendableChatContext

/**
 * Represents the context of an event-based chat interaction.
 *
 * @property bot The bot instance handling the interaction.
 * @property update The update object containing the update details.
 * @property state The current state of the chat, including metadata and contextual information.
 * @property event The event content within the chat interaction.
 */
data class EventChatContext<E : Event>(
    override val bot: Bot,
    override val update: Update,
    override val state: ChatState,
    val event: E,
) : SuspendableChatContext
