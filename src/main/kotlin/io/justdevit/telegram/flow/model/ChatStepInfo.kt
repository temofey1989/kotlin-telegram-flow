package io.justdevit.telegram.flow.model

import java.time.Instant

/**
 * Represents a state of the executed step.
 *
 * @property name Name of the current step in the flow.
 * @property state State of the current step in the flow.
 * @property started Represents a start timestamp of the step.
 * @property finished Represents a finish timestamp of the step. If not present, then step is not terminal state.
 * @property errorMessage Error message. Relevant only for failed step executions.
 */
data class ChatStepInfo(
    var name: String,
    var state: ChatStepState,
    val started: Instant,
    var finished: Instant? = null,
    var errorMessage: String? = null,
)
