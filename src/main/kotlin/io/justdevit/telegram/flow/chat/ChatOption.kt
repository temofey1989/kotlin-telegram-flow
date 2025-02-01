package io.justdevit.telegram.flow.chat

/**
 * Represents an option in a chat system.
 *
 * @property value The underlying value or identifier of the chat option.
 * @property label The display label for the chat option. Defaults to the value if not provided.
 */
data class ChatOption(val value: String, val label: String = value)
