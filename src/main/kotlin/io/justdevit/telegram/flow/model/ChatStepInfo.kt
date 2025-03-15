package io.justdevit.telegram.flow.model

/**
 * @property stepName Name of the current step in the flow.
 * @property stepState State of the current step in the flow.
 * @property errorMessage Error message. Relevant only for failed step executions.
 */
data class ChatStepInfo(
    var stepName: String,
    var stepState: ChatStepState,
    var errorMessage: String? = null,
)
