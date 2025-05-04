package io.justdevit.telegram.flow

import io.justdevit.telegram.flow.model.ChatState
import io.justdevit.telegram.flow.model.ChatStateExtractionContext
import io.justdevit.telegram.flow.model.runnerName
import java.util.concurrent.ConcurrentHashMap

/**
 * An interface responsible for managing and persisting chat state in the context of Telegram Bot interactions.
 *
 * Implementations of this interface are expected to facilitate storing and retrieving state
 * information for individual chat sessions. This state may include localization, labels,
 * metadata, and flow or step-specific data for managing bot workflows.
 */
interface ChatStateStore {

    /**
     * Extracts the chat state associated with a given chat ID and the provided extraction context.
     *
     * This method facilitates retrieving the persisted or contextual state of a specific chat in a Telegram bot interaction.
     *
     * @param chatId The unique identifier of the chat for which the state needs to be extracted.
     * @param context The context used for extracting the chat state, containing details such as the incoming update and runner information.
     * @return The extracted chat state if available, or null if no state is found.
     */
    suspend fun extract(chatId: Long, context: ChatStateExtractionContext): ChatState?

    /**
     * Stores the specified chat state for persistence or later retrieval.
     *
     * This method is responsible for saving the relevant details of a chat session's state,
     * including localization, labels, metadata, flow information, and step-specific progress.
     * It ensures that the state is maintained and can be reloaded as required in future interactions.
     *
     * @param state The chat state to be stored, containing all the necessary details
     *              to represent the current state of the chat session.
     */
    suspend fun store(state: ChatState)
}

/**
 * An in-memory implementation of the [ChatStateStore] interface for managing and persisting chat states.
 *
 * It is intended for scenarios where chat state management is required without persistent storage.
 */
class InMemoryChatStateStore : ChatStateStore {

    private val states = ConcurrentHashMap<ChatStateKey, ChatState>()

    override suspend fun extract(chatId: Long, context: ChatStateExtractionContext): ChatState? {
        val key = ChatStateKey(chatId, context.runner.name)
        return states.computeIfAbsent(key) {
            ChatState(
                chatId = chatId,
                metadata = mapOf(TELEGRAM_FLOW_RUNNER_NAME_KEY to context.runner.name),
            )
        }
    }

    override suspend fun store(state: ChatState) {
        states[ChatStateKey(state.chatId, state.runnerName)] = state
    }

    data class ChatStateKey(val chatId: Long, val runnerName: String? = null)
}
