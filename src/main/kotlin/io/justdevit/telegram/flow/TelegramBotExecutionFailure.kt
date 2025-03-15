package io.justdevit.telegram.flow

import com.github.kotlintelegrambot.entities.Update
import io.justdevit.kotlin.boost.eventbus.Event

/**
 * Represents an event triggered when an error occurs during the execution of a Telegram Bot update.
 *
 * This event contains information about the update that failed and the associated exception.
 * It can be used for error handling or logging purposes within the Telegram Flow.
 *
 * @property update The Telegram Bot API update that caused the error.
 * @property throwable The exception or error that occurred during the execution of the update.
 */
data class TelegramBotExecutionFailure(val update: Update, val throwable: Throwable) : Event()
