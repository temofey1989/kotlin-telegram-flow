package io.justdevit.telegram.flow.model

import java.time.Instant

/**
 * Represents a state of the executed flow.
 *
 * @property name Name of the current flow being executed in the chat.
 * @property state State of the current flow being executed in the chat.
 * @property data Object implementing [ChatFlowData] to manage the flow step information and associated messages. Defaults to an instance of [SimpleChatFlowData].
 * @property started Represents a start timestamp of the flow.
 * @property finished Represents a finish timestamp of the flow. If not present, then flow is not terminal state.
 */
data class ChatFlowInfo(
    var name: String,
    var state: ChatFlowState,
    var data: ChatFlowData = SimpleChatFlowData(),
    val started: Instant,
    var finished: Instant? = null,
)
