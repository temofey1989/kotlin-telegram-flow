package io.justdevit.telegram.flow.model

/**
 * Represents a unique identifier for a message.
 *
 * Each implementation must provide the `value` property, which holds the
 * unique identifier as a [Long].
 */
sealed interface MessageId {
    val value: Long
}

/**
 * Represents the unique identifier for a server-side message.
 */
data class ServerMessageId(override val value: Long) : MessageId {
    override fun toString() = "S:$value"
}

/**
 * Represents the unique identifier for a user-side message.
 */
data class UserMessageId(override val value: Long) : MessageId {
    override fun toString() = "U:$value"
}
