package io.justdevit.telegram.flow.extension

import com.github.kotlintelegrambot.entities.ChatId
import com.github.kotlintelegrambot.entities.ChatId.ChannelUsername
import com.github.kotlintelegrambot.entities.ChatId.Id

/**
 * Retrieves the string representation of the chat ID.
 *
 * The value will be the string form of the numeric ID if the chat is identified by an ID.
 * If the chat is identified by a channel username, the value will be the username.
 */
val ChatId.value: String
    get() = when (this) {
        is Id -> id.toString()
        is ChannelUsername -> username
    }
