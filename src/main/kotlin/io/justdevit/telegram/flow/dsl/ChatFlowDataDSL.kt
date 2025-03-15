package io.justdevit.telegram.flow.dsl

import io.justdevit.telegram.flow.model.ChatFlowData
import io.justdevit.telegram.flow.model.ChatStepContext
import io.justdevit.telegram.flow.model.MessageId

/**
 * Adds a message identifier to the list of identifiers associated with a specific step in the chat flow.
 *
 * @param pair A pair consisting of the step identifier as a [String] and the message identifier as a [io.justdevit.telegram.flow.model.MessageId].
 */
operator fun ChatFlowData?.plusAssign(pair: Pair<String, MessageId>) {
    with(this ?: return) {
        stepMessageIds
            .getOrPut(pair.first) { mutableListOf() }
            .add(pair.second)
    }
}

/**
 * Updates the [ChatFlowData] instance by adding the specified [messageId] to the list of message IDs
 * associated with the current step in the chat flow context.
 *
 * @param messageId The ID of the message to be added to the current step in the chat flow.
 */
context(ChatStepContext)
operator fun ChatFlowData?.plusAssign(messageId: MessageId) {
    with(this ?: return) {
        stepMessageIds
            .getOrPut(step.name) { mutableListOf() }
            .add(messageId)
    }
}
