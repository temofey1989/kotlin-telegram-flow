package io.justdevit.telegram.flow.listener

import io.justdevit.kotlin.boost.eventbus.EventListener
import io.justdevit.kotlin.boost.logging.Logging
import io.justdevit.telegram.flow.ChatStateStore
import io.justdevit.telegram.flow.model.ChatExecutionCompleted
import io.justdevit.telegram.flow.model.ChatExecutionStarted
import io.justdevit.telegram.flow.model.ChatFlowCompleted
import io.justdevit.telegram.flow.model.ChatFlowNotFound
import io.justdevit.telegram.flow.model.ChatFlowStarted
import io.justdevit.telegram.flow.model.ChatFlowTerminated
import io.justdevit.telegram.flow.model.ChatStepCompleted
import io.justdevit.telegram.flow.model.ChatStepFailed
import io.justdevit.telegram.flow.model.ChatStepNotFound
import io.justdevit.telegram.flow.model.ChatStepStarted
import io.justdevit.telegram.flow.model.ChatStepSuspended
import io.justdevit.telegram.flow.model.ChatStepTerminated

/**
 * Listener for handling [ChatFlowNotFound] events.
 *
 * This class listens for events of type [ChatFlowNotFound] and uses an instance of the [ChatStateStore]
 * to store the actual state.
 *
 * @property stateStore A store which will be user do store a state.
 */
class ChatFlowNotFoundListener(private val stateStore: ChatStateStore) : EventListener<ChatFlowNotFound> {

    companion object : Logging()

    override val supportedClass = ChatFlowNotFound::class.java

    override suspend fun onEvent(event: ChatFlowNotFound) {
        log.debug { "Processing event: ${event::class.simpleName}" }
        stateStore.store(event.context.state)
    }
}

/**
 * Listener for handling [ChatStepNotFound] events.
 *
 * This class listens for events of type [ChatFlowNotFound] and uses an instance of the [ChatStateStore]
 * to store the actual state.
 *
 * @property stateStore A store which will be user do store a state.
 */
class ChatStepNotFoundListener(private val stateStore: ChatStateStore) : EventListener<ChatStepNotFound> {

    companion object : Logging()

    override val supportedClass = ChatStepNotFound::class.java

    override suspend fun onEvent(event: ChatStepNotFound) {
        log.debug { "Processing event: ${event::class.simpleName}" }
        stateStore.store(event.context.state)
    }
}

/**
 * Listener for handling [ChatFlowStarted] events.
 *
 * This class listens for events of type [ChatFlowNotFound] and uses an instance of the [ChatStateStore]
 * to store the actual state.
 *
 * @property stateStore A store which will be user do store a state.
 */
class ChatFlowStartedListener(private val stateStore: ChatStateStore) : EventListener<ChatFlowStarted> {

    companion object : Logging()

    override val supportedClass = ChatFlowStarted::class.java

    override suspend fun onEvent(event: ChatFlowStarted) {
        log.debug { "Processing event: ${event::class.simpleName}" }
        stateStore.store(event.context.state)
    }
}

/**
 * Listener for handling [ChatFlowCompleted] events.
 *
 * This class listens for events of type [ChatFlowNotFound] and uses an instance of the [ChatStateStore]
 * to store the actual state.
 *
 * @property stateStore A store which will be user do store a state.
 */
class ChatFlowCompletedListener(private val stateStore: ChatStateStore) : EventListener<ChatFlowCompleted> {

    companion object : Logging()

    override val supportedClass = ChatFlowCompleted::class.java

    override suspend fun onEvent(event: ChatFlowCompleted) {
        log.debug { "Processing event: ${event::class.simpleName}" }
        stateStore.store(event.context.state)
    }
}

/**
 * Listener for handling [ChatFlowTerminated] events.
 *
 * This class listens for events of type [ChatFlowNotFound] and uses an instance of the [ChatStateStore]
 * to store the actual state.
 *
 * @property stateStore A store which will be user do store a state.
 */
class ChatFlowTerminatedListener(private val stateStore: ChatStateStore) : EventListener<ChatFlowTerminated> {

    companion object : Logging()

    override val supportedClass = ChatFlowTerminated::class.java

    override suspend fun onEvent(event: ChatFlowTerminated) {
        log.debug { "Processing event: ${event::class.simpleName}" }
        stateStore.store(event.context.state)
    }
}

/**
 * Listener for handling [ChatStepStarted] events.
 *
 * This class listens for events of type [ChatFlowNotFound] and uses an instance of the [ChatStateStore]
 * to store the actual state.
 *
 * @property stateStore A store which will be user do store a state.
 */
class ChatStepStartedListener(private val stateStore: ChatStateStore) : EventListener<ChatStepStarted> {

    companion object : Logging()

    override val supportedClass = ChatStepStarted::class.java

    override suspend fun onEvent(event: ChatStepStarted) {
        log.debug { "Processing event: ${event::class.simpleName}" }
        stateStore.store(event.context.state)
    }
}

/**
 * Listener for handling [ChatStepCompleted] events.
 *
 * This class listens for events of type [ChatFlowNotFound] and uses an instance of the [ChatStateStore]
 * to store the actual state.
 *
 * @property stateStore A store which will be user do store a state.
 */
class ChatStepCompletedListener(private val stateStore: ChatStateStore) : EventListener<ChatStepCompleted> {

    companion object : Logging()

    override val supportedClass = ChatStepCompleted::class.java

    override suspend fun onEvent(event: ChatStepCompleted) {
        log.debug { "Processing event: ${event::class.simpleName}" }
        stateStore.store(event.context.state)
    }
}

/**
 * Listener for handling [ChatStepSuspended] events.
 *
 * This class listens for events of type [ChatFlowNotFound] and uses an instance of the [ChatStateStore]
 * to store the actual state.
 *
 * @property stateStore A store which will be user do store a state.
 */
class ChatStepSuspendedListener(private val stateStore: ChatStateStore) : EventListener<ChatStepSuspended> {

    companion object : Logging()

    override val supportedClass = ChatStepSuspended::class.java

    override suspend fun onEvent(event: ChatStepSuspended) {
        log.debug { "Processing event: ${event::class.simpleName}" }
        stateStore.store(event.context.state)
    }
}

/**
 * Listener for handling [ChatStepTerminated] events.
 *
 * This class listens for events of type [ChatFlowNotFound] and uses an instance of the [ChatStateStore]
 * to store the actual state.
 *
 * @property stateStore A store which will be user do store a state.
 */
class ChatStepTerminatedListener(private val stateStore: ChatStateStore) : EventListener<ChatStepTerminated> {

    companion object : Logging()

    override val supportedClass = ChatStepTerminated::class.java

    override suspend fun onEvent(event: ChatStepTerminated) {
        log.debug { "Processing event: ${event::class.simpleName}" }
        stateStore.store(event.context.state)
    }
}

/**
 * Listener for handling [ChatStepFailed] events.
 *
 * This class listens for events of type [ChatFlowNotFound] and uses an instance of the [ChatStateStore]
 * to store the actual state.
 *
 * @property stateStore A store which will be user do store a state.
 */
class ChatStepFailedListener(private val stateStore: ChatStateStore) : EventListener<ChatStepFailed> {

    companion object : Logging()

    override val supportedClass = ChatStepFailed::class.java

    override suspend fun onEvent(event: ChatStepFailed) {
        log.debug { "Processing event: ${event::class.simpleName}" }
        stateStore.store(event.context.state)
    }
}

/**
 * Listener for handling [ChatExecutionStarted] events.
 *
 * This class listens for events of type [ChatFlowNotFound] and uses an instance of the [ChatStateStore]
 * to store the actual state.
 *
 * @property stateStore A store which will be user do store a state.
 */
class ChatExecutionStartedListener(private val stateStore: ChatStateStore) : EventListener<ChatExecutionStarted> {

    companion object : Logging()

    override val supportedClass = ChatExecutionStarted::class.java

    override suspend fun onEvent(event: ChatExecutionStarted) {
        log.debug { "Processing event: ${event::class.simpleName}" }
        stateStore.store(event.context.state)
    }
}

/**
 * Listener for handling [ChatExecutionCompleted] events.
 *
 * This class listens for events of type [ChatFlowNotFound] and uses an instance of the [ChatStateStore]
 * to store the actual state.
 *
 * @property stateStore A store which will be user do store a state.
 */
class ChatExecutionCompletedListener(private val stateStore: ChatStateStore) : EventListener<ChatExecutionCompleted> {

    companion object : Logging()

    override val supportedClass = ChatExecutionCompleted::class.java

    override suspend fun onEvent(event: ChatExecutionCompleted) {
        log.debug { "Processing event: ${event::class.simpleName}" }
        stateStore.store(event.context.state)
    }
}
