package io.justdevit.telegram.flow.model

/**
 * Represents options configuration for a chat system.
 *
 * @property question A string representing the main question or prompt to display in the chat.
 * @property items A hierarchical list of chat options, where each inner list groups chat options together.
 */
data class ChatOptions(val question: String, val items: List<List<ChatOption>>)
