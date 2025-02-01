package io.justdevit.telegram.flow

import io.justdevit.telegram.flow.chat.ChatState

/**
 * Defines a contract for extracting chat state based on the provided chat ID and context.
 *
 * Implementations of this interface are responsible for returning a [ChatState]
 * object that represents the current state of a chat, which includes metadata such
 * as language, labels, and flow state associated with the chat.
 *
 * The extracted state can be used to drive conversation logic, flow execution,
 * or determine the next steps in a Telegram chat interaction.
 */
interface ChatStateExtractor {
    suspend fun extract(chatId: Long, context: ChatStateExtractionContext): ChatState = ChatState(chatId = chatId)
}

/**
 * A singleton implementation of [ChatStateExtractor] that serves as a stub for extracting chat state.
 *
 * This object provides a basic implementation of the [ChatStateExtractor] interface, returning a default
 * or placeholder [ChatState] when its `extract` function is invoked. It is primarily used as a fallback
 * or placeholder for scenarios where a more sophisticated extractor is not configured.
 *
 * This implementation can be replaced or overridden in a [TelegramFlowRunnerBuilder] by setting a custom
 * implementation to the `chatStateExtractor` property. By default, this stub implementation is used
 * when no other extractor is specified.
 *
 * The stub may also serve as a simple utility for development or testing purposes where chat state
 * extraction logic is not yet implemented.
 */
object StubbingChatStateExtractor : ChatStateExtractor
