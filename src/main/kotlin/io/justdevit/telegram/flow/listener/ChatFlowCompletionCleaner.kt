package io.justdevit.telegram.flow.listener

import io.justdevit.kotlin.boost.eventbus.EventListener
import io.justdevit.kotlin.boost.logging.Logging
import io.justdevit.telegram.flow.chat.ChatFlowCompleted
import io.justdevit.telegram.flow.chat.clearMessages

/**
 * Handles the termination of a chat flow.
 *
 * This class listens for the [ChatFlowCompleted] event and performs cleanup
 * by removing any messages related to the current chat flow. It ensures that
 * the resources used during the flow are cleared to maintain a clean state
 * for further interactions.
 */
class ChatFlowCompletionCleaner : EventListener<ChatFlowCompleted> {

    companion object : Logging()

    override val supportedClass = ChatFlowCompleted::class.java

    override suspend fun onEvent(event: ChatFlowCompleted) {
        with(event.context) {
            log.debug { "Removing flow messages for chat [${state.chatId}]: [${state.flowData.stepMessageIds.values.joinToString()}]" }
            state.flowData.clearMessages()
        }
    }
}
