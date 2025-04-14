package io.justdevit.telegram.flow.dsl

import io.justdevit.kotlin.boost.eventbus.Event
import io.justdevit.kotlin.boost.logging.Logging
import io.justdevit.telegram.flow.CALLBACK_SUSPENDED_STEP_MARKER
import io.justdevit.telegram.flow.DATA_DELIMITER
import io.justdevit.telegram.flow.EVENT_SUSPENDED_STEP_MARKER
import io.justdevit.telegram.flow.PRE_CHECKOUT_SUSPENDED_STEP_MARKER
import io.justdevit.telegram.flow.SUCCESSFUL_PAYMENT_SUSPENDED_STEP_MARKER
import io.justdevit.telegram.flow.SUSPENDED_STEP_MARKER
import io.justdevit.telegram.flow.TEXT_SUSPENDED_STEP_MARKER
import io.justdevit.telegram.flow.model.CallbackChatStepContext
import io.justdevit.telegram.flow.model.ChatFlow
import io.justdevit.telegram.flow.model.ChatMenu
import io.justdevit.telegram.flow.model.ChatStep
import io.justdevit.telegram.flow.model.ChatStepContext
import io.justdevit.telegram.flow.model.EventChatStepContext
import io.justdevit.telegram.flow.model.PreCheckoutChatStepContext
import io.justdevit.telegram.flow.model.SuccessfulPaymentChatStepContext
import io.justdevit.telegram.flow.model.SuspendableChatStepContext
import io.justdevit.telegram.flow.model.TextChatStepContext
import kotlin.reflect.KClass

/**
 * A builder class used to define and construct a [ChatFlow] by specifying its steps and optional menu configuration.
 *
 * @property id The ID of the chat flow being built. It must not be blank or contain whitespaces.
 * @property menu An optional [ChatMenu] that can be associated with this chat flow to define commands or actions for interaction.
 *
 * This class allows adding and configuring chat steps within a flow and provides functionality to build the final [ChatFlow].
 * Each chat step represents a distinct interaction point in the flow and can optionally be suspendable for awaiting specific events.
 */
open class ChatFlowBuilder(var id: String, var menu: ChatMenu? = null) {

    companion object : Logging()

    private val stepMap = mutableMapOf<String, ChatStep>()
    private val steps = mutableListOf<ChatStep>()
    private val lastStep: ChatStep?
        get() = steps.lastOrNull()

    /**
     * Builds a [ChatFlow] instance based on the current configuration of the builder.
     *
     * @return A new [ChatFlow] instance initialized with the builder's state.
     */
    fun build() =
        ChatFlow(id = id, menu = menu, steps = steps.toList()).also {
            if (it.steps.isEmpty()) {
                log.warn { "No steps for chat flow: [${it.id}]." }
            } else {
                it.steps.forEach { step -> step.flow = it }
            }
        }

    /**
     * Declares a step within a chat flow, associating the given execution logic with the step name.
     *
     * @param execution A suspendable function that defines the behavior of the step, executed in the context of [ChatStepContext].
     */
    operator fun String.invoke(execution: suspend ChatStepContext.() -> Unit) = step(this, execution)

    /**
     * Defines a step within the chat flow, adding it to the flow's sequence of steps.
     *
     * @param name The name of the step, which must be a non-blank string.
     * @param execution A suspendable function defining the step's behavior, executed in the context of [ChatStepContext].
     * @return A [ChatStep] instance representing the newly created step.
     */
    fun step(name: String, execution: suspend ChatStepContext.() -> Unit): ChatStep {
        val step = ChatStep(
            name = name,
            action = execution,
        )
        step.previous = lastStep
        addStep(step)
        return step
    }

    /**
     * Awaits a text-based input at the current chat step and executes the specified action within the given context.
     *
     * @param action A suspendable action to be executed within the context, allowing interaction with text input.
     * @return Newly created suspended step.
     */
    infix fun ChatStep.awaitText(action: suspend TextChatStepContext.() -> Unit) = awaitText(true, action)

    /**
     * Awaits a callback-based input at the current chat step and executes the specified action within the given context.
     *
     * @param action A suspendable action to be executed within the context, allowing interaction with text input.
     * @return Newly created suspended step.
     */
    infix fun ChatStep.awaitCallback(action: suspend CallbackChatStepContext.() -> Unit) = awaitCallback(true, action)

    /**
     * Awaits a pre-checkout-based input at the current chat step and executes the specified action within the given context.
     *
     * @param action A suspendable action to be executed within the context, allowing interaction with text input.
     * @return Newly created suspended step.
     */
    infix fun ChatStep.awaitPreCheckout(action: suspend PreCheckoutChatStepContext.() -> Unit) = awaitPreCheckout(true, action)

    /**
     * Awaits a successful-payment-based input at the current chat step and executes the specified action within the given context.
     *
     * @param action A suspendable action to be executed within the context, allowing interaction with text input.
     * @return Newly created suspended step.
     */
    infix fun ChatStep.awaitPayment(action: suspend SuccessfulPaymentChatStepContext.() -> Unit) = awaitPayment(true, action)

    /**
     * Awaits a text-based input at the current chat step and executes the specified suspendable action
     * within the provided context.
     *
     * @param fallback Determines whether to fall back to the previous step if the current execution fails. Defaults to `true`.
     * @param action A suspendable action that is executed in the [TextChatStepContext] when the text input is received.
     * @return Newly created suspended step.
     */
    fun ChatStep.awaitText(fallback: Boolean = true, action: suspend TextChatStepContext.() -> Unit) =
        await(
            marker = TEXT_SUSPENDED_STEP_MARKER,
            fallback = fallback,
            action = action,
        )

    /**
     * Awaits a callback-based input at the current chat step and executes the specified suspendable action
     * within the provided context.
     *
     * @param fallback Determines whether to fall back to the previous step if the current execution fails. Defaults to `true`.
     * @param action A suspendable action that is executed in the [CallbackChatStepContext] when the callback input is received.
     * @return Newly created suspended step.
     */
    fun ChatStep.awaitCallback(fallback: Boolean = true, action: suspend CallbackChatStepContext.() -> Unit) =
        await(
            marker = CALLBACK_SUSPENDED_STEP_MARKER,
            fallback = fallback,
            action = action,
        )

    /**
     * Awaits a pre-checkout-based input at the current chat step and executes the specified suspendable action
     * within the provided context.
     *
     * @param fallback Determines whether to fall back to the previous step if the current execution fails. Defaults to `true`.
     * @param action A suspendable action that is executed in the [PreCheckoutChatStepContext] when the pre-checkout input is received.
     * @return Newly created suspended step.
     */
    fun ChatStep.awaitPreCheckout(fallback: Boolean = true, action: suspend PreCheckoutChatStepContext.() -> Unit) =
        await(
            marker = PRE_CHECKOUT_SUSPENDED_STEP_MARKER,
            fallback = fallback,
            action = action,
        )

    /**
     * Awaits a successful-payment at the current chat step and executes the specified suspendable action
     * within the provided context. In case no pre-checkout step, default will be generated.
     *
     * NOTE: Default Pre-Checkout step will just confirm checkout.
     *
     * @param fallback Determines whether to fall back to the previous step if the current execution fails. Defaults to `true`.
     * @param action A suspendable action that is executed in the [SuccessfulPaymentChatStepContext] when the successful payment has received.
     * @return Newly created suspended step.
     */
    fun ChatStep.awaitPayment(fallback: Boolean = true, action: suspend SuccessfulPaymentChatStepContext.() -> Unit): ChatStep {
        if (!name.endsWith(PRE_CHECKOUT_SUSPENDED_STEP_MARKER)) {
            return awaitPreCheckout(fallback) {
                confirmCheckout(preCheckoutQueryId = preCheckoutQuery.id)
            }.awaitPayment(fallback, action)
        }
        return await(SUCCESSFUL_PAYMENT_SUSPENDED_STEP_MARKER, fallback, action)
    }

    /**
     * Awaits an event of the specified type at the current chat step and executes the provided action
     * within the context of the corresponding event type.
     *
     * @param E The type of the event to await.
     * @param eventType The class of the event to await. Only events of this type will trigger the action.
     * @param fallback Determines whether to fall back to the previous step if the current execution fails. Defaults to `true`.
     * @param action A suspendable function that is executed in the context of [EventChatStepContext] specific to the awaited event type.
     * @return Newly created suspended step.
     */
    @Suppress("UNCHECKED_CAST")
    fun <E : Event> ChatStep.awaitEventForType(
        eventType: KClass<E>,
        fallback: Boolean = true,
        action: suspend EventChatStepContext<E>.() -> Unit,
    ) = await<EventChatStepContext<*>>(
        marker = "${EVENT_SUSPENDED_STEP_MARKER}${DATA_DELIMITER}${eventType.qualifiedName}",
        fallback = fallback,
    ) {
        if (eventType.isInstance(event)) {
            action(this as EventChatStepContext<E>)
        }
    }

    /**
     * Awaits an event of the specified type during the execution of a chat step and performs the provided action
     * when the event is received.
     *
     * @param E The type of the event to be awaited. This must extend the [Event] class.
     * @param fallback Determines whether to fall back to the previous step if the execution of the current step fails. Defaults to `true`.
     * @param action A suspendable function that is executed in the context of [EventChatStepContext] specific to the awaited event type.
     * @return Newly created suspended step.
     */
    inline fun <reified E : Event> ChatStep.awaitEvent(fallback: Boolean = true, noinline action: suspend EventChatStepContext<E>.() -> Unit) =
        awaitEventForType(
            eventType = E::class,
            fallback = fallback,
            action = action,
        )

    @Suppress("LABEL_NAME_CLASH", "UNCHECKED_CAST")
    private fun <T : SuspendableChatStepContext> ChatStep.await(
        marker: String,
        fallback: Boolean = true,
        action: suspend T.() -> Unit,
    ): ChatStep {
        require(stepMap[this@ChatStep.name] != null) {
            "No previous step has been added to the await processing."
        }
        val previousStep = this@ChatStep
        val step = ChatStep(
            name = "${previousStep.name.substringBefore(SUSPENDED_STEP_MARKER)}$marker",
            suspendable = true,
            action = {
                val context = this as T
                if (fallback) {
                    withFallback(stepName = previousStep.name) {
                        context.action()
                    }
                } else {
                    context.action()
                }
            },
        )
        step.previous = previousStep
        addStep(step)
        return step
    }

    private fun addStep(step: ChatStep) {
        if (lastStep != null) {
            lastStep!!.next = step
            step.previous = lastStep
        }
        stepMap[step.name] = step
        steps += step
    }
}
