package io.justdevit.telegram.flow.chat

/**
 * Interface representing the data structure for managing chat flow steps.
 *
 * The [ChatFlowData] interface is responsible for organizing and maintaining
 * the mapping of step identifiers to a list of message identifiers ([MessageId]).
 * This allows tracking and handling messages associated with each step in a chat flow.
 */
interface ChatFlowData {
    /**
     * A mutable map that associates step identifiers (as [String]) with lists of message identifiers ([MessageId]).
     */
    val stepMessageIds: MutableMap<String, MutableList<MessageId>>
}

/**
 * Data class representing the implementation of [ChatFlowData] for managing chat flow steps.
 *
 * @property stepMessageIds A mutable map associating step identifiers with their respective
 * lists of message identifiers ([MessageId]).
 */
data class SimpleChatFlowData(override val stepMessageIds: MutableMap<String, MutableList<MessageId>> = mutableMapOf()) : ChatFlowData

/**
 * Adds a message identifier to the list of identifiers associated with a specific step in the chat flow.
 *
 * @param pair A pair consisting of the step identifier as a [String] and the message identifier as a [MessageId].
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
