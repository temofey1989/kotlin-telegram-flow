package io.justdevit.telegram.flow.listener

import io.justdevit.telegram.flow.TelegramBotExecutionFailed

/**
 * A no-operation implementation of the Telegram Bot error handling mechanism.
 *
 * This class extends [TelegramBotErrorHandler] and provides an override for the
 * `onEvent` method. However, the implementation intentionally does nothing.
 *
 * It can be used in scenarios where no specific error handling logic is required,
 * or when a default, passive behavior is preferred for unhandled bot execution errors.
 */
class NoopTelegramBotExecutionHandler : TelegramBotErrorHandler() {

    override suspend fun onEvent(event: TelegramBotExecutionFailed) = Unit
}
