package io.justdevit.telegram.flow.listener

import io.justdevit.kotlin.boost.eventbus.EventListener
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

    override val supportedClass = ChatFlowNotFound::class.java

    override suspend fun onEvent(event: ChatFlowNotFound) {
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

    override val supportedClass = ChatStepNotFound::class.java

    override suspend fun onEvent(event: ChatStepNotFound) {
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

    override val supportedClass = ChatFlowStarted::class.java

    override suspend fun onEvent(event: ChatFlowStarted) {
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

    override val supportedClass = ChatFlowCompleted::class.java

    override suspend fun onEvent(event: ChatFlowCompleted) {
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

    override val supportedClass = ChatFlowTerminated::class.java

    override suspend fun onEvent(event: ChatFlowTerminated) {
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

    override val supportedClass = ChatStepStarted::class.java

    override suspend fun onEvent(event: ChatStepStarted) {
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

    override val supportedClass = ChatStepCompleted::class.java

    override suspend fun onEvent(event: ChatStepCompleted) {
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

    override val supportedClass = ChatStepSuspended::class.java

    override suspend fun onEvent(event: ChatStepSuspended) {
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

    override val supportedClass = ChatStepTerminated::class.java

    override suspend fun onEvent(event: ChatStepTerminated) {
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

    override val supportedClass = ChatStepFailed::class.java

    override suspend fun onEvent(event: ChatStepFailed) {
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

    override val supportedClass = ChatExecutionStarted::class.java

    override suspend fun onEvent(event: ChatExecutionStarted) {
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

    override val supportedClass = ChatExecutionCompleted::class.java

    override suspend fun onEvent(event: ChatExecutionCompleted) {
        stateStore.store(event.context.state)
    }
}
