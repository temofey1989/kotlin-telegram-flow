package io.justdevit.telegram.flow.listener

import io.justdevit.kotlin.boost.eventbus.EventListener
import io.justdevit.kotlin.boost.extension.ifExists
import io.justdevit.kotlin.boost.logging.Logging
import io.justdevit.telegram.flow.chat.ChatExecutionStarted
import io.justdevit.telegram.flow.chat.UserMessageId
import io.justdevit.telegram.flow.chat.plusAssign

/**
 * Registers the ID of a user's message during the chat execution flow.
 *
 * This class listens for the [ChatExecutionStarted] event and retrieves the
 * current user's message ID from the event context. The message ID is then
 * added to the flow data associated with the current chat step. This ensures
 * that the message IDs are tracked and can be utilized or referenced later
 * within the chat flow execution.
 */
class ChatMessageIdRegistrar : EventListener<ChatExecutionStarted> {

    companion object : Logging()

    override val supportedClass = ChatExecutionStarted::class.java

    override suspend fun onEvent(event: ChatExecutionStarted) {
        with(event.context) {
            update.message?.messageId.ifExists {
                state.flowData += UserMessageId(it)
                log.debug { "User message [$it] has been registered to flow data." }
            }
        }
    }
}
