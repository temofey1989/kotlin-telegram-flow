package io.justdevit.telegram.flow

import com.github.kotlintelegrambot.Bot
import com.github.kotlintelegrambot.entities.Update

/**
 * Represents the context used during chat state extraction in Telegram bot interactions.
 * It provides necessary data for extracting and managing chat-related state.
 *
 * @property update The update received from the Telegram Bot API, representing user actions or events in the chat.
 * @property runner The [TelegramFlowRunner] responsible for managing the bot's execution and state lifecycle.
 * @property bot The [Bot] instance associated with the current [TelegramFlowRunner].
 */
data class ChatStateExtractionContext(val update: Update, val runner: TelegramFlowRunner) {
    val bot: Bot = runner.bot
}
