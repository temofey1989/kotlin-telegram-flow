package io.justdevit.telegram.flow.listener

import io.justdevit.kotlin.boost.eventbus.EventListener
import io.justdevit.kotlin.boost.logging.Logging
import io.justdevit.telegram.flow.dsl.clearMessages
import io.justdevit.telegram.flow.model.ChatFlowTerminated

/**
 * Handles the termination of a chat flow.
 *
 * This class listens for the [ChatFlowTerminated] event and performs cleanup
 * by removing any messages related to the terminated chat flow. It ensures that
 * all messages associated with the flow are deleted, maintaining a clean state
 * and preparing the chat for new interactions.
 */
class ChatFlowTerminationCleaner : EventListener<ChatFlowTerminated> {

    companion object : Logging()

    override val supportedClass = ChatFlowTerminated::class.java

    override suspend fun onEvent(event: ChatFlowTerminated) {
        with(event.context) {
            log.debug {
                val stepMessageIds = state.flowInfo
                    ?.data
                    ?.stepMessageIds
                    ?.values ?: emptyList()
                "Removing flow messages for chat [${state.chatId}]: [${stepMessageIds.joinToString()}]"
            }
            state.flowInfo
                ?.data
                ?.clearMessages()
        }
    }
}
