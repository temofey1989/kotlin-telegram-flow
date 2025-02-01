package io.justdevit.telegram.flow

import com.github.kotlintelegrambot.types.TelegramBotResult.Error

/**
 * Represents an exception that occurs during the execution of Telegram-related operations.
 *
 * @property error The error associated with this exception.
 */
class TelegramException(val error: Error) : RuntimeException("Telegram Error: $error")
