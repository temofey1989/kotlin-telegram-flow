package io.justdevit.telegram.flow.listener

import io.justdevit.kotlin.boost.logging.Logging
import io.justdevit.telegram.flow.model.TelegramBotExecutionFailure
import kotlin.Int.Companion.MAX_VALUE

/**
 * Logs errors occurring during the execution of Telegram Bot updates.
 *
 * This class listens for the [TelegramBotExecutionFailure] event and logs detailed
 * information about exceptions encountered. It uses the associated chat and exception
 * details to provide contextual information for debugging. This implementation
 * prioritizes error logging over other handlers with a maximum priority setting.
 */
class TelegramFlowRunnerErrorLogger : TelegramBotErrorHandler() {

    companion object : Logging()

    override val priority = MAX_VALUE

    override suspend fun onEvent(event: TelegramBotExecutionFailure) {
        log.error(event.throwable) {
            "Unexpected exception for chat [${event.update.message?.chat?.id}]. Reason: ${event.throwable.message}"
        }
    }
}
