package io.justdevit.telegram.flow.model

import java.time.Instant
import java.time.Instant.now

/**
 * Represents the result of executing a step in a chat flow or pipeline.
 *
 * @property timestamp The point in time when the step execution result was generated.
 */
sealed interface ChatStepExecutionResult {
    val timestamp: Instant
}

/**
 * Represents the result of a completed chat step execution within a chat flow or pipeline.
 *
 * @property termination Indicates whether the chat flow or pipeline should terminate as a result of this step.
 * @property timestamp The point in time when the step execution result was generated.
 */
data class CompletedChatStepExecutionResult(val termination: Boolean = false, override val timestamp: Instant = now()) : ChatStepExecutionResult

/**
 * Represents the result of a suspended execution of a step in a chat flow or pipeline.
 *
 * @param timestamp The point in time when the suspended step execution result was created. Defaults to the current time.
 */
data class SuspendedChatStepExecutionResult(override val timestamp: Instant = now()) : ChatStepExecutionResult

/**
 * Represents the result of a chat step execution where a jump to another chat step is triggered.
 *
 * @property jumpStep The target chat step to which the execution flow should jump.
 * @property timestamp The point in time when this result was generated. Defaults to the current time.
 */
data class StepJumpChatStepExecutionResult(val jumpStep: ChatStep, override val timestamp: Instant = now()) : ChatStepExecutionResult

/**
 * Represents the result of a step execution that involves jumping to a different flow
 * within a chat process or pipeline.
 *
 * @property jumpFlowName The name of the target flow to jump to.
 * @property timestamp The time when the step execution result was created.
 */
data class FlowJumpChatStepExecutionResult(val jumpFlowName: String, override val timestamp: Instant = now()) : ChatStepExecutionResult

/**
 * Represents the result of a chat flow step execution where the flow is explicitly stopped.
 *
 * @property timestamp The point in time when the step execution was stopped.
 */
data class StopFlowChatStepExecutionResult(override val timestamp: Instant = now()) : ChatStepExecutionResult

/**
 * Represents a failed execution result for a chat step in a chat flow or pipeline.
 *
 * @property throwable The exception or error that caused the step execution to fail.
 * @property timestamp The point in time when the failure occurred, defaulting to the current time.
 */
data class FailedChatStepExecutionResult(val throwable: Throwable, override val timestamp: Instant = now()) : ChatStepExecutionResult
