package io.justdevit.telegram.flow.model

/**
 * @property flowName Name of the current flow being executed in the chat.
 * @property flowState State of the current flow being executed in the chat.
 * @property flowData Object implementing [ChatFlowData] to manage the flow step information and associated messages. Defaults to an instance of [SimpleChatFlowData].
 */
data class ChatFlowInfo(
    var flowName: String,
    var flowState: ChatFlowState,
    var flowData: ChatFlowData = SimpleChatFlowData(),
)
