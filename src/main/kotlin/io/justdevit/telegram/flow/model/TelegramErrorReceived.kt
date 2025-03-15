package io.justdevit.telegram.flow.model

import com.github.kotlintelegrambot.Bot
import com.github.kotlintelegrambot.errors.TelegramError
import io.justdevit.kotlin.boost.eventbus.Event

/**
 * Represents an event triggered when an error occurs while interacting with the Telegram Bot API.
 *
 * This event contains information about the encountered error and the bot instance
 * that raised the error. It is commonly used in error handling mechanisms to
 * process or log errors during bot operation.
 *
 * @property error The details of the Telegram API error that was encountered.
 * @property bot The instance of the bot that triggered the error event.
 */
data class TelegramErrorReceived(val error: TelegramError, val bot: Bot) : Event()
