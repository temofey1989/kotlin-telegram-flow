package io.justdevit.telegram.flow.chat

/**
 * Represents the state of a step within a chat flow process.
 */
enum class ChatStepState {

    /**
     * Indicates that a chat step execution has been started within a chat flow.
     *
     * This state represents the initial phase of a step where execution begins.
     */
    STARTED,

    /**
     * Indicates that the execution of a chat step has been temporarily paused within a chat flow.
     *
     * This state represents an intermediate point where a step's processing is halted,
     * awaiting further action or external input to resume the flow.
     */
    SUSPENDED,

    /**
     * Indicates that the execution of a chat step has been successfully completed.
     *
     * This state represents a step that has been entirely processed without errors
     * and is ready to transition to the next step in the chat flow.
     */
    COMPLETED,

    /**
     * Indicates that the execution of a chat step has been terminated.
     *
     * This state represents a situation where a step's execution is stopped,
     * either due to an explicit termination request or because continuation
     * is not possible under the current circumstances.
     */
    TERMINATED,

    /**
     * Indicates that the execution of a chat step has failed during a chat flow.
     *
     * This state represents a scenario where the step could not be completed successfully,
     * often due to an error or unexpected condition.
     */
    FAILED,
}
