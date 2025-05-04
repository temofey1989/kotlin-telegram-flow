package io.justdevit.telegram.flow.model

/**
 * Defines a mapping of step names to their respective message IDs.
 */
typealias StepMessageIds = MutableMap<String, MutableList<MessageId>>

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
    val stepMessageIds: StepMessageIds
}

/**
 * Data class representing the implementation of [ChatFlowData] for managing chat flow steps.
 *
 * @property stepMessageIds A mutable map associating step identifiers with their respective
 * lists of message identifiers ([MessageId]).
 */
data class SimpleChatFlowData(override val stepMessageIds: StepMessageIds = mutableMapOf()) : ChatFlowData
