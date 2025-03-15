package io.justdevit.telegram.flow.listener

import io.justdevit.kotlin.boost.eventbus.EventListener
import io.justdevit.telegram.flow.TelegramBotExecutionFailure

/**
 * Handles errors that occur during the execution of Telegram Bot updates.
 *
 * This abstract class serves as a base for implementing error handlers for
 * Telegram Bot updates that fail during processing. It listens for the
 * [TelegramBotExecutionFailure] event and provides a way to define custom
 * error-handling logic.
 *
 * Extend this class to handle specific error scenarios, such as logging or
 * retrying failed updates. The derived class is expected to implement the
 * event handling logic by overriding the necessary methods.
 */
abstract class TelegramBotErrorHandler : EventListener<TelegramBotExecutionFailure> {
    override val supportedClass = TelegramBotExecutionFailure::class.java
}
