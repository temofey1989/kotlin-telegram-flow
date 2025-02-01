package io.justdevit.telegram.flow

/**
 * Defines an interface that combines the ability to extract chat state and
 * listen to chat flow execution events.
 *
 * This interface extends [ChatStateExtractor] and [ChatFlowExecutionListener],
 * providing a unified contract for processing chat-related states and events.
 */
interface ChatStateProcessor :
    ChatStateExtractor,
    ChatFlowExecutionListener
