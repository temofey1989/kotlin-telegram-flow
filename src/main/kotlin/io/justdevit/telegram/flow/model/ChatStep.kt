package io.justdevit.telegram.flow.model

import io.justdevit.telegram.flow.CHAT_PATH_DELIMITER

/**
 * Represents a single step in a chat flow. A chat step defines a specific action
 * or context within the flow and maintains references to its neighboring steps,
 * as well as its containing chat flow.
 *
 * @property name The name of the step. Must be a non-blank string.
 * @property suspendable Indicates whether the step is suspendable.
 * @property action The action to execute at this step.
 *
 * @throws IllegalArgumentException if [name] is blank.
 * @throws IllegalStateException if accessing [flow], [isFirst], or [isLast] before the flow is defined.
 */
data class ChatStep(
    val name: String,
    val suspendable: Boolean = false,
    val action: suspend ChatStepContext.() -> Unit,
) {

    init {
        require(name.isNotBlank()) {
            "Chat Step name cannot be blank."
        }
    }

    private var _flow: ChatFlow? = null

    /**
     * The [ChatFlow] that this step belongs to. Throws an exception if accessed before being set.
     */
    var flow: ChatFlow
        get() = _flow ?: throw IllegalStateException("Step has no flow defined yet.")
        set(value) {
            if (_flow != null) {
                throw IllegalStateException("Step already has a flow.")
            }
            _flow = value
        }

    /**
     * The preceding [ChatStep] in the flow, if any.
     */
    var previous: ChatStep? = null

    /**
     * The succeeding [ChatStep] in the flow, if any.
     */
    var next: ChatStep? = null

    /**
     * The fully qualified name of the step, combining the flow ID and step name.
     */
    val fullName: String
        get() = if (_flow != null) "${flow.id}$CHAT_PATH_DELIMITER$name" else name

    /**
     * Indicates if this step is the first in the flow. Throws an exception if the flow is not set.
     */
    val isFirst: Boolean
        get() {
            if (_flow == null) {
                throw IllegalStateException("Step has no flow defined.")
            }
            return flow.firstStep === this
        }

    /**
     * Indicates if this step is the last in the flow. Throws an exception if the flow is not set.
     */
    val isLast: Boolean
        get() {
            if (_flow == null) {
                throw IllegalStateException("Step has no flow defined.")
            }
            return flow.lastStep === this
        }

    override fun toString() = "ChatStep(name='$name', flow=${flow.id}, suspendable=$suspendable, previous=${previous?.name}, next=${next?.name})"
}
