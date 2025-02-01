package io.justdevit.telegram.flow

import io.justdevit.telegram.flow.chat.ChatExecutionCompleted
import io.justdevit.telegram.flow.chat.ChatExecutionStarted
import io.justdevit.telegram.flow.chat.ChatFlowCompleted
import io.justdevit.telegram.flow.chat.ChatFlowNotFound
import io.justdevit.telegram.flow.chat.ChatFlowStarted
import io.justdevit.telegram.flow.chat.ChatFlowTerminated
import io.justdevit.telegram.flow.chat.ChatStepCompleted
import io.justdevit.telegram.flow.chat.ChatStepFailed
import io.justdevit.telegram.flow.chat.ChatStepNotFound
import io.justdevit.telegram.flow.chat.ChatStepStarted
import io.justdevit.telegram.flow.chat.ChatStepSuspended
import io.justdevit.telegram.flow.chat.ChatStepTerminated

/**
 * Interface for observing the execution flow of a chat application.
 * This listener provides methods that are invoked during various stages
 * of chat flow and step execution, including initiation, completion,
 * suspension, termination, and other significant events.
 *
 * Implementers can override these methods to handle or log specific events
 * in the chat flow lifecycle.
 *
 * A default no-operation implementation is provided as `ChatFlowExecutionListener.EMPTY`.
 */
interface ChatFlowExecutionListener {

    companion object {

        /**
         * A default, no-op implementation of the [ChatFlowExecutionListener].
         *
         * This object provides an empty implementation of all methods defined in the [ChatFlowExecutionListener]
         * interface. It can be used as a placeholder or a base implementation when no specific behavior
         * is required for the methods.
         */
        val EMPTY = object : ChatFlowExecutionListener {}
    }

    /**
     * Called when the execution of a chat flow starts.
     *
     * @param event An instance of [ChatExecutionStarted] event.
     */
    suspend fun onChatExecutionStarted(event: ChatExecutionStarted) = Unit

    /**
     * Called when the execution of a chat flow is completed.
     *
     * @param event An instance of [ChatExecutionCompleted] event.
     */
    suspend fun onChatExecutionCompleted(event: ChatExecutionCompleted) = Unit

    /**
     * Called when a chat flow cannot be found.
     *
     * @param event An instance of [ChatFlowNotFound] event.
     */
    suspend fun onChatFlowNotFound(event: ChatFlowNotFound) = Unit

    /**
     * Called when the chat flow has started.
     *
     * @param event An instance of [ChatFlowStarted] event.
     */
    suspend fun onChatFlowStarted(event: ChatFlowStarted) = Unit

    /**
     * Called when the chat flow has terminated.
     *
     * @param event An instance of [ChatFlowTerminated] event.
     */
    suspend fun onChatFlowTerminated(event: ChatFlowTerminated) = Unit

    /**
     * Called when the chat flow has completed.
     *
     * @param event An instance of [ChatFlowCompleted] event.
     */
    suspend fun onChatFlowCompleted(event: ChatFlowCompleted) = Unit

    /**
     * Called when the chat step not found.
     *
     * @param event An instance of [ChatStepNotFound] event.
     */
    suspend fun onChatStepNotFound(event: ChatStepNotFound) = Unit

    /**
     * Called when the chat step has started.
     *
     * @param event An instance of [ChatStepStarted] event.
     */
    suspend fun onChatStepStarted(event: ChatStepStarted) = Unit

    /**
     * Called when the chat step has suspended.
     *
     * @param event An instance of [ChatStepSuspended] event.
     */
    suspend fun onChatStepSuspended(event: ChatStepSuspended) = Unit

    /**
     * Called when the chat step has terminated.
     *
     * @param event An instance of [ChatStepTerminated] event.
     */
    suspend fun onChatStepTerminated(event: ChatStepTerminated) = Unit

    /**
     * Called when the chat step has failed.
     *
     * @param event An instance of [ChatStepFailed] event.
     */
    suspend fun onChatStepFailed(event: ChatStepFailed) = Unit

    /**
     * Called when the chat step has completed.
     *
     * @param event An instance of [ChatStepCompleted] event.
     */
    suspend fun onChatStepCompleted(event: ChatStepCompleted) = Unit
}
