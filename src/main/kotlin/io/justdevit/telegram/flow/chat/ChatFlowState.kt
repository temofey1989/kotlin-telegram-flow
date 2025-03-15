package io.justdevit.telegram.flow.chat

/**
 * Represents the different states of a chat flow during its lifecycle.
 */
enum class ChatFlowState {

    /**
     * Indicates that the chat flow is currently ongoing.
     */
    ACTIVE,

    /**
     * Indicates that the chat flow has successfully finished its execution.
     */
    COMPLETED,

    /**
     * Indicates that the chat flow has been interrupted or stopped before completion.
     */
    TERMINATED,
}
