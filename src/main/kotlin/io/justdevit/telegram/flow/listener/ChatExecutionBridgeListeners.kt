package io.justdevit.telegram.flow.listener

import io.justdevit.kotlin.boost.eventbus.EventListener
import io.justdevit.telegram.flow.ChatFlowExecutionListener
import io.justdevit.telegram.flow.chat.ChatExecutionCompleted
import io.justdevit.telegram.flow.chat.ChatExecutionStarted
import io.justdevit.telegram.flow.chat.ChatFlowCompleted
import io.justdevit.telegram.flow.chat.ChatFlowNotFound
import io.justdevit.telegram.flow.chat.ChatFlowStarted
import io.justdevit.telegram.flow.chat.ChatFlowTerminated
import io.justdevit.telegram.flow.chat.ChatStepCompleted
import io.justdevit.telegram.flow.chat.ChatStepFailed
import io.justdevit.telegram.flow.chat.ChatStepNotFound
import io.justdevit.telegram.flow.chat.ChatStepStarted
import io.justdevit.telegram.flow.chat.ChatStepSuspended
import io.justdevit.telegram.flow.chat.ChatStepTerminated

/**
 * Listener for handling [ChatFlowNotFound] events.
 *
 * This class listens for events of type [ChatFlowNotFound] and uses an instance
 * of [ChatFlowExecutionListener] to handle the event when a chat flow cannot be found.
 *
 * @property executionListener An implementation of [ChatFlowExecutionListener] used to
 * handle the [ChatFlowNotFound] event.
 */
class ChatFlowNotFoundListener(private val executionListener: ChatFlowExecutionListener) : EventListener<ChatFlowNotFound> {

    override val supportedClass = ChatFlowNotFound::class.java

    override suspend fun onEvent(event: ChatFlowNotFound) {
        executionListener.onChatFlowNotFound(event)
    }
}

/**
 * Listener for handling [ChatStepNotFound] events.
 *
 * This class listens for events of type [ChatStepNotFound] and uses an instance
 * of [ChatFlowExecutionListener] to handle the event when a chat flow cannot be found.
 *
 * @property executionListener An implementation of [ChatFlowExecutionListener] used to
 * handle the [ChatStepNotFound] event.
 */
class ChatStepNotFoundListener(private val executionListener: ChatFlowExecutionListener) : EventListener<ChatStepNotFound> {

    override val supportedClass = ChatStepNotFound::class.java

    override suspend fun onEvent(event: ChatStepNotFound) {
        executionListener.onChatStepNotFound(event)
    }
}

/**
 * Listener for handling [ChatFlowStarted] events.
 *
 * This class listens for events of type [ChatFlowStarted] and uses an instance
 * of [ChatFlowExecutionListener] to handle the event when a chat flow cannot be found.
 *
 * @property executionListener An implementation of [ChatFlowExecutionListener] used to
 * handle the [ChatFlowStarted] event.
 */
class ChatFlowStartedListener(private val executionListener: ChatFlowExecutionListener) : EventListener<ChatFlowStarted> {

    override val supportedClass = ChatFlowStarted::class.java

    override suspend fun onEvent(event: ChatFlowStarted) {
        executionListener.onChatFlowStarted(event)
    }
}

/**
 * Listener for handling [ChatFlowCompleted] events.
 *
 * This class listens for events of type [ChatFlowCompleted] and uses an instance
 * of [ChatFlowExecutionListener] to handle the event when a chat flow cannot be found.
 *
 * @property executionListener An implementation of [ChatFlowExecutionListener] used to
 * handle the [ChatFlowCompleted] event.
 */
class ChatFlowCompletedListener(private val executionListener: ChatFlowExecutionListener) : EventListener<ChatFlowCompleted> {

    override val supportedClass = ChatFlowCompleted::class.java

    override suspend fun onEvent(event: ChatFlowCompleted) {
        executionListener.onChatFlowCompleted(event)
    }
}

/**
 * Listener for handling [ChatFlowTerminated] events.
 *
 * This class listens for events of type [ChatFlowTerminated] and uses an instance
 * of [ChatFlowExecutionListener] to handle the event when a chat flow cannot be found.
 *
 * @property executionListener An implementation of [ChatFlowExecutionListener] used to
 * handle the [ChatFlowTerminated] event.
 */
class ChatFlowTerminatedListener(private val executionListener: ChatFlowExecutionListener) : EventListener<ChatFlowTerminated> {

    override val supportedClass = ChatFlowTerminated::class.java

    override suspend fun onEvent(event: ChatFlowTerminated) {
        executionListener.onChatFlowTerminated(event)
    }
}

/**
 * Listener for handling [ChatStepStarted] events.
 *
 * This class listens for events of type [ChatStepStarted] and uses an instance
 * of [ChatFlowExecutionListener] to handle the event when a chat flow cannot be found.
 *
 * @property executionListener An implementation of [ChatFlowExecutionListener] used to
 * handle the [ChatStepStarted] event.
 */
class ChatStepStartedListener(private val executionListener: ChatFlowExecutionListener) : EventListener<ChatStepStarted> {

    override val supportedClass = ChatStepStarted::class.java

    override suspend fun onEvent(event: ChatStepStarted) {
        executionListener.onChatStepStarted(event)
    }
}

/**
 * Listener for handling [ChatStepCompleted] events.
 *
 * This class listens for events of type [ChatStepCompleted] and uses an instance
 * of [ChatFlowExecutionListener] to handle the event when a chat flow cannot be found.
 *
 * @property executionListener An implementation of [ChatFlowExecutionListener] used to
 * handle the [ChatStepCompleted] event.
 */
class ChatStepCompletedListener(private val executionListener: ChatFlowExecutionListener) : EventListener<ChatStepCompleted> {

    override val supportedClass = ChatStepCompleted::class.java

    override suspend fun onEvent(event: ChatStepCompleted) {
        executionListener.onChatStepCompleted(event)
    }
}

/**
 * Listener for handling [ChatStepSuspended] events.
 *
 * This class listens for events of type [ChatStepSuspended] and uses an instance
 * of [ChatFlowExecutionListener] to handle the event when a chat flow cannot be found.
 *
 * @property executionListener An implementation of [ChatFlowExecutionListener] used to
 * handle the [ChatStepSuspended] event.
 */
class ChatStepSuspendedListener(private val executionListener: ChatFlowExecutionListener) : EventListener<ChatStepSuspended> {

    override val supportedClass = ChatStepSuspended::class.java

    override suspend fun onEvent(event: ChatStepSuspended) {
        executionListener.onChatStepSuspended(event)
    }
}

/**
 * Listener for handling [ChatStepTerminated] events.
 *
 * This class listens for events of type [ChatStepTerminated] and uses an instance
 * of [ChatFlowExecutionListener] to handle the event when a chat flow cannot be found.
 *
 * @property executionListener An implementation of [ChatFlowExecutionListener] used to
 * handle the [ChatStepTerminated] event.
 */
class ChatStepTerminatedListener(private val executionListener: ChatFlowExecutionListener) : EventListener<ChatStepTerminated> {

    override val supportedClass = ChatStepTerminated::class.java

    override suspend fun onEvent(event: ChatStepTerminated) {
        executionListener.onChatStepTerminated(event)
    }
}

/**
 * Listener for handling [ChatStepFailed] events.
 *
 * This class listens for events of type [ChatStepFailed] and uses an instance
 * of [ChatFlowExecutionListener] to handle the event when a chat flow cannot be found.
 *
 * @property executionListener An implementation of [ChatFlowExecutionListener] used to
 * handle the [ChatStepFailed] event.
 */
class ChatStepFailedListener(private val executionListener: ChatFlowExecutionListener) : EventListener<ChatStepFailed> {

    override val supportedClass = ChatStepFailed::class.java

    override suspend fun onEvent(event: ChatStepFailed) {
        executionListener.onChatStepFailed(event)
    }
}

/**
 * Listener for handling [ChatExecutionStarted] events.
 *
 * This class listens for events of type [ChatExecutionStarted] and uses an instance
 * of [ChatFlowExecutionListener] to handle the event when a chat flow cannot be found.
 *
 * @property executionListener An implementation of [ChatFlowExecutionListener] used to
 * handle the [ChatExecutionStarted] event.
 */
class ChatExecutionStartedListener(private val executionListener: ChatFlowExecutionListener) : EventListener<ChatExecutionStarted> {

    override val supportedClass = ChatExecutionStarted::class.java

    override suspend fun onEvent(event: ChatExecutionStarted) {
        executionListener.onChatExecutionStarted(event)
    }
}

/**
 * Listener for handling [ChatExecutionCompleted] events.
 *
 * This class listens for events of type [ChatExecutionCompleted] and uses an instance
 * of [ChatFlowExecutionListener] to handle the event when a chat flow cannot be found.
 *
 * @property executionListener An implementation of [ChatFlowExecutionListener] used to
 * handle the [ChatExecutionCompleted] event.
 */
class ChatExecutionCompletedListener(private val executionListener: ChatFlowExecutionListener) : EventListener<ChatExecutionCompleted> {

    override val supportedClass = ChatExecutionCompleted::class.java

    override suspend fun onEvent(event: ChatExecutionCompleted) {
        executionListener.onChatExecutionCompleted(event)
    }
}
