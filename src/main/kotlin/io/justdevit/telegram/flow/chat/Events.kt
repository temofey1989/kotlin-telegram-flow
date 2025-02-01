package io.justdevit.telegram.flow.chat

import io.justdevit.kotlin.boost.eventbus.Event
import java.time.Instant
import java.time.Instant.now

/**
 * Represents an event that occurs during a chat flow.
 *
 * All chat events must derive from this class and provide their specific details.
 * Each event contains a timestamp to indicate when the event occurred.
 *
 * @param timestamp The timestamp indicating when the event occurred.
 */
sealed class ChatEvent : Event() {
    abstract val timestamp: Instant
}

/**
 * Represents an event where a specified chat flow cannot be found.
 *
 * @param flowName The name of the chat flow that was not found.
 * @param context The context of the chat interaction where the flow was expected.
 * @param timestamp The timestamp indicating when the event occurred. Defaults to the current moment.
 */
data class ChatFlowNotFound(
    val flowName: String,
    val context: ChatContext,
    override val timestamp: Instant = now(),
) : ChatEvent()

/**
 * Represents an event indicating that a specific step within a chat flow could not be found.
 *
 * @property flow The [ChatFlow] in which the missing step was expected to exist.
 * @property stepName The name of the step that could not be found in the given chat flow.
 * @property context The [ChatContext] providing information about the ongoing chat interaction
 *                   where the missing step occurred.
 * @property timestamp The time at which this event was created. Defaults to the current time.
 */
data class ChatStepNotFound(
    val flow: ChatFlow,
    val stepName: String,
    val context: ChatContext,
    override val timestamp: Instant = now(),
) : ChatEvent()

/**
 * Represents a specific type of chat-related event occurring within a chat flow.
 *
 * @property context The context of the current chat step within the flow.
 */
sealed class ChatFlowEvent : ChatEvent() {
    abstract val context: ChatStepContext
}

/**
 * Represents the event triggered when a chat flow starts in the system.
 *
 * @param context The context of the chat step at the time the flow starts.
 * @param timestamp The time when the chat flow was started. Defaults to the current time.
 */
data class ChatFlowStarted(override val context: ChatStepContext, override val timestamp: Instant = now()) : ChatFlowEvent()

/**
 * Represents the event triggered when a chat flow completes in the system.
 *
 * @param context The context of the chat step at the time the flow completes.
 * @param timestamp The time when the chat flow was started. Defaults to the current time.
 */
data class ChatFlowCompleted(override val context: ChatStepContext, override val timestamp: Instant = now()) : ChatFlowEvent()

/**
 * Represents the event triggered when a chat flow terminates in the system.
 *
 * @param context The context of the chat step at the time the flow terminates.
 * @param timestamp The time when the chat flow was started. Defaults to the current time.
 */
data class ChatFlowTerminated(override val context: ChatStepContext, override val timestamp: Instant = now()) : ChatFlowEvent()

/**
 * Represents an event that occurs during a specific step in a chat flow.
 *
 * This class is part of the hierarchy for events in a chat environment, specifically
 * targeting events that are tied to a distinct step in the chat flow execution.
 * Each event contains contextual information related to the step being executed.
 *
 * @property context The context of the current chat step within the flow.
 */
sealed class ChatStepEvent : ChatEvent() {
    abstract val context: ChatStepContext
}

/**
 * Represents an event indicating the initiation of a specific step within a chat flow.
 *
 * @property context The context of the current chat step within the flow.
 * @property timestamp The moment at which the step started execution.
 */
data class ChatStepStarted(override val context: ChatStepContext, override val timestamp: Instant = now()) : ChatStepEvent()

/**
 * Represents an event indicating the completion of a specific step within a chat flow.
 *
 * @property context The context of the current chat step within the flow.
 * @property timestamp The moment at which the step completed execution.
 */
data class ChatStepCompleted(override val context: ChatStepContext, override val timestamp: Instant = now()) : ChatStepEvent()

/**
 * Represents an event indicating the suspending of a specific step within a chat flow.
 *
 * @property context The context of the current chat step within the flow.
 * @property timestamp The moment at which the step suspended execution.
 */
data class ChatStepSuspended(override val context: ChatStepContext, override val timestamp: Instant = now()) : ChatStepEvent()

/**
 * Represents an event indicating the termination of a specific step within a chat flow.
 *
 * @property context The context of the current chat step within the flow.
 * @property timestamp The moment at which the step terminated execution.
 */
data class ChatStepTerminated(override val context: ChatStepContext, override val timestamp: Instant = now()) : ChatStepEvent()

/**
 * Represents an event indicating the failure of a specific step in a chat flow.
 *
 * @property context The context of the failed chat step within the flow.
 * @property timestamp The time at which the failure event occurred.
 * @property throwable The exception or error that caused the failure, if available.
 */
data class ChatStepFailed(
    override val context: ChatStepContext,
    override val timestamp: Instant = now(),
    val throwable: Throwable? = null,
) : ChatStepEvent()

/**
 * An event that signifies the initiation of a chat execution within a chat flow.
 *
 * @property context The contextual information related to the chat step being executed.
 * @property timestamp The time at which the event occurred. Defaults to the current instant.
 */
data class ChatExecutionStarted(val context: ChatStepContext, override val timestamp: Instant = now()) : ChatEvent()

/**
 * Represents the completion of a chat execution process.
 *
 * @property timestamp The timestamp when the chat execution was completed.
 * @property context The context associated with the completed chat execution.
 * @property history The ordered list of snapshots capturing the execution of each step in the chat flow.
 */
data class ChatExecutionCompleted(
    override val timestamp: Instant = now(),
    val context: ChatContext,
    val history: List<ChatStepExecutionSnapshot> = emptyList(),
) : ChatEvent()
