package io.justdevit.telegram.flow.chat

/**
 * A custom exception class to signal a control flow that specifies
 * moving to a particular step in a sequential process or workflow.
 *
 * @property stepName The name of the step to which the flow should navigate.
 */
class Goto(val stepName: String) : RuntimeException("Go to step: [$stepName]")

/**
 * Represents an exception used to indicate a "go previous" operation.
 */
object GoPrevious : RuntimeException("Go Previous") {
    private fun readResolve(): Any = GoNext
}

/**
 * Represents an exception used to indicate a "go next" operation.
 */
object GoNext : RuntimeException("Go Next") {
    private fun readResolve(): Any = GoNext
}

/**
 * Represents an exception used to indicate the starting of a flow by its name.
 *
 * @property flowName The name of the flow that is being started.
 */
class StartFlow(val flowName: String) : RuntimeException("Start Flow: [$flowName]")

/**
 * Represents an exception used to indicate a "stop flow" operation.
 */
object StopFlow : RuntimeException() {
    private fun readResolve(): Any = GoNext
}
