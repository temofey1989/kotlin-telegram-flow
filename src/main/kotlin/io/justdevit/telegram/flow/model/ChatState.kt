package io.justdevit.telegram.flow.model

import com.github.kotlintelegrambot.entities.ChatId
import io.justdevit.telegram.flow.TELEGRAM_FLOW_RUNNER_NAME_KEY
import java.time.Instant

/**
 * Data class representing the state of a chat.
 *
 * @property chatId Unique identifier for the chat.
 * @property language Language code representing the localization settings for this chat. Defaults to `en` for English.
 * @property labels List of string labels associated with this chat .
 * @property metadata Key-value pairs providing additional metadata for the chat.
 * @property flowInfo Optional info of the current flow being executed in the chat.
 * @property stepInfo Optional info of the current step being executed in the chat.
 */
data class ChatState(
    val chatId: Long,
    val language: String = "en",
    val labels: List<String> = emptyList(),
    val metadata: Map<String, String> = emptyMap(),

    var flowInfo: ChatFlowInfo? = null,
    var stepInfo: ChatStepInfo? = null,

    var lastUpdate: Instant? = null,
)

/**
 * Extension property to retrieve the chat's unique ID as a [ChatId] instance.
 */
val ChatState.botChatId: ChatId
    get() = ChatId.fromId(chatId)

/**
 * Retrieves the name of the runner associated with the Telegram flow, if any.
 */
val ChatState.runnerName: String?
    get() = metadata[TELEGRAM_FLOW_RUNNER_NAME_KEY]
