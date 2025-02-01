package io.justdevit.telegram.flow

import io.justdevit.kotlin.boost.eventbus.EventBus
import io.justdevit.kotlin.boost.logging.Logging
import io.justdevit.kotlin.boost.logging.withCoSpanId
import io.justdevit.telegram.flow.chat.CallbackChatContext
import io.justdevit.telegram.flow.chat.CallbackChatStepContext
import io.justdevit.telegram.flow.chat.ChatContext
import io.justdevit.telegram.flow.chat.ChatExecutionCompleted
import io.justdevit.telegram.flow.chat.ChatExecutionStarted
import io.justdevit.telegram.flow.chat.ChatFlow
import io.justdevit.telegram.flow.chat.ChatFlowCompleted
import io.justdevit.telegram.flow.chat.ChatFlowNotFound
import io.justdevit.telegram.flow.chat.ChatFlowStarted
import io.justdevit.telegram.flow.chat.ChatFlowTerminated
import io.justdevit.telegram.flow.chat.ChatStep
import io.justdevit.telegram.flow.chat.ChatStepCompleted
import io.justdevit.telegram.flow.chat.ChatStepContext
import io.justdevit.telegram.flow.chat.ChatStepExecutionResult
import io.justdevit.telegram.flow.chat.ChatStepExecutionSnapshot
import io.justdevit.telegram.flow.chat.ChatStepFailed
import io.justdevit.telegram.flow.chat.ChatStepNotFound
import io.justdevit.telegram.flow.chat.ChatStepStarted
import io.justdevit.telegram.flow.chat.ChatStepSuspended
import io.justdevit.telegram.flow.chat.ChatStepTerminated
import io.justdevit.telegram.flow.chat.CommandChatContext
import io.justdevit.telegram.flow.chat.CommandChatStepContext
import io.justdevit.telegram.flow.chat.CompletedChatStepExecutionResult
import io.justdevit.telegram.flow.chat.ContinuationChatStepContext
import io.justdevit.telegram.flow.chat.FailedChatStepExecutionResult
import io.justdevit.telegram.flow.chat.FlowJumpChatStepExecutionResult
import io.justdevit.telegram.flow.chat.GoNext
import io.justdevit.telegram.flow.chat.GoPrevious
import io.justdevit.telegram.flow.chat.Goto
import io.justdevit.telegram.flow.chat.PreCheckoutChatContext
import io.justdevit.telegram.flow.chat.PreCheckoutChatStepContext
import io.justdevit.telegram.flow.chat.StartFlow
import io.justdevit.telegram.flow.chat.StepJumpChatStepExecutionResult
import io.justdevit.telegram.flow.chat.StopFlow
import io.justdevit.telegram.flow.chat.StopFlowChatStepExecutionResult
import io.justdevit.telegram.flow.chat.SuccessfulPaymentChatContext
import io.justdevit.telegram.flow.chat.SuccessfulPaymentChatStepContext
import io.justdevit.telegram.flow.chat.SuspendableChatStepContext
import io.justdevit.telegram.flow.chat.SuspendedChatStepExecutionResult
import io.justdevit.telegram.flow.chat.TextChatContext
import io.justdevit.telegram.flow.chat.TextChatStepContext
import io.justdevit.telegram.flow.listener.ChatExecutionCompletedListener
import io.justdevit.telegram.flow.listener.ChatExecutionStartedListener
import io.justdevit.telegram.flow.listener.ChatFlowCompletedListener
import io.justdevit.telegram.flow.listener.ChatFlowCompletionCleaner
import io.justdevit.telegram.flow.listener.ChatFlowNotFoundListener
import io.justdevit.telegram.flow.listener.ChatFlowStartedListener
import io.justdevit.telegram.flow.listener.ChatFlowTerminatedListener
import io.justdevit.telegram.flow.listener.ChatFlowTerminationCleaner
import io.justdevit.telegram.flow.listener.ChatMessageIdRegistrar
import io.justdevit.telegram.flow.listener.ChatStepCompletedListener
import io.justdevit.telegram.flow.listener.ChatStepFailedListener
import io.justdevit.telegram.flow.listener.ChatStepNotFoundListener
import io.justdevit.telegram.flow.listener.ChatStepStartedListener
import io.justdevit.telegram.flow.listener.ChatStepSuspendedListener
import io.justdevit.telegram.flow.listener.ChatStepTerminatedListener

/**
 * The [ChatFlowExecutor] class is responsible for managing chat flows and steps.
 *
 * This class handles the execution of different chat flows or steps based on the context provided.
 * It registers various event listeners to the given [EventBus] to handle chat-related events
 * such as flow initialization, step execution, and custom notifications.
 *
 * Key Features:
 * - Executes chat flows or steps based on the type of [ChatContext] received.
 * - Maintains a mapping of flow names to corresponding [ChatFlow] definitions.
 * - Supports execution of different types of steps (e.g., Command, Text, Callback) and flow transitions.
 * - Handles flow and step completion, suspension, failures, and transitions.
 *
 * Constructor Parameters:
 * @param flowsMap A map of chat flow names to their corresponding [ChatFlow] instances.
 * @param eventBus The [EventBus] used for publishing and listening to chat-related events.
 * @param executionListener A listener for tracking execution states across chat flows and steps.
 */
class ChatFlowExecutor(
    private val flowsMap: Map<String, ChatFlow> = emptyMap(),
    private val eventBus: EventBus,
    executionListener: ChatFlowExecutionListener = ChatFlowExecutionListener.EMPTY,
) {

    companion object : Logging()

    constructor(flows: List<ChatFlow>, eventBus: EventBus, executionListener: ChatFlowExecutionListener) : this(flows.associateBy { it.id }, eventBus, executionListener)

    init {
        eventBus.register(ChatFlowCompletionCleaner())
        eventBus.register(ChatFlowTerminationCleaner())
        eventBus.register(ChatMessageIdRegistrar())

        eventBus.register(ChatFlowNotFoundListener(executionListener))
        eventBus.register(ChatStepNotFoundListener(executionListener))
        eventBus.register(ChatFlowStartedListener(executionListener))
        eventBus.register(ChatFlowCompletedListener(executionListener))
        eventBus.register(ChatFlowTerminatedListener(executionListener))
        eventBus.register(ChatStepStartedListener(executionListener))
        eventBus.register(ChatStepCompletedListener(executionListener))
        eventBus.register(ChatStepSuspendedListener(executionListener))
        eventBus.register(ChatStepTerminatedListener(executionListener))
        eventBus.register(ChatStepFailedListener(executionListener))
        eventBus.register(ChatExecutionStartedListener(executionListener))
        eventBus.register(ChatExecutionCompletedListener(executionListener))
    }

    /**
     * Executes the appropriate flow or action based on the type of the provided chat context.
     *
     * @param context The chat context to process, which determines the specific action or flow to execute.
     */
    suspend fun execute(context: ChatContext) {
        when (context) {
            is CommandChatContext -> startFlow(context)
            is TextChatContext -> executeForStep(context)
            is CallbackChatContext -> executeForStep(context)
            is PreCheckoutChatContext -> executeForStep(context)
            is SuccessfulPaymentChatContext -> executeForStep(context)
        }
    }

    private suspend fun startFlow(context: CommandChatContext) {
        val flowName = context.command
        val flow = flowsMap[flowName]
        if (flow == null) {
            publishChatFlowNotFound(flowName, context)
        } else {
            flow.firstStep.execute(context)
        }
    }

    private suspend fun executeForStep(context: ChatContext) {
        with(context) {
            val flowName = state.flowName ?: return
            val stepName = state.stepName ?: return
            val flow = flowsMap[flowName]
            if (flow == null) {
                publishChatFlowNotFound(flowName, this)
            } else {
                val step = flow.stepMap[stepName]
                if (step == null) {
                    log.debug { "Unknown step: [$stepName] of flow [$flowName]" }
                    eventBus.coPublish(
                        ChatStepNotFound(
                            flow = flow,
                            stepName = stepName,
                            context = this,
                        ),
                    )
                } else {
                    step.takeIf { it.acceptable(context) }?.execute(this)
                }
            }
        }
    }

    private fun ChatStep.acceptable(context: ChatContext) =
        when (context) {
            is CommandChatContext -> {
                !suspendable
            }

            is TextChatContext -> {
                suspendable && name.endsWith(TEXT_SUSPENDED_STEP_MARKER)
            }

            is CallbackChatContext -> {
                suspendable &&
                    name.endsWith(CALLBACK_SUSPENDED_STEP_MARKER) &&
                    context
                        .callbackQuery
                        .data
                        .startsWith(fullName)
            }

            is PreCheckoutChatContext -> {
                suspendable && name.endsWith(PRE_CHECKOUT_SUSPENDED_STEP_MARKER)
            }

            is SuccessfulPaymentChatContext -> {
                suspendable && name.endsWith(SUCCESSFUL_PAYMENT_SUSPENDED_STEP_MARKER)
            }
        }

    private suspend fun publishChatFlowNotFound(flowName: String, context: ChatContext) {
        log.debug { "Unknown flow: [$flowName]" }
        eventBus.coPublish(ChatFlowNotFound(flowName, context))
    }

    private suspend fun ChatStep.execute(context: ChatContext) {
        val history = mutableListOf<ChatStepExecutionSnapshot>()
        var next: ChatStep? = this
        var stepContext = context.toStepContext(this)

        eventBus.coPublish(ChatExecutionStarted(stepContext))
        while (next != null) {
            next = next.execute(stepContext, history)?.also {
                stepContext = ContinuationChatStepContext(stepContext, it)
            }
        }
        eventBus.coPublish(
            ChatExecutionCompleted(
                context = context,
                history = history,
            ),
        )
    }

    private fun ChatContext.toStepContext(step: ChatStep) =
        when (this) {
            is CommandChatContext -> CommandChatStepContext(context = this, step = step)
            is TextChatContext -> TextChatStepContext(context = this, step = step)
            is CallbackChatContext -> CallbackChatStepContext(context = this, step = step)
            is PreCheckoutChatContext -> PreCheckoutChatStepContext(context = this, step = step)
            is SuccessfulPaymentChatContext -> SuccessfulPaymentChatStepContext(context = this, step = step)
        }

    private suspend fun ChatStep.execute(context: ChatStepContext, history: MutableList<ChatStepExecutionSnapshot>): ChatStep? {
        val result = invoke(context)
        history += ChatStepExecutionSnapshot(
            executedStep = this,
            context = context,
            result = result,
        )
        return when (result) {
            is CompletedChatStepExecutionResult -> next?.takeIf { !result.termination }
            is StepJumpChatStepExecutionResult -> result.jumpStep
            is FlowJumpChatStepExecutionResult -> flowsMap[result.jumpFlowName]?.firstStep
            is StopFlowChatStepExecutionResult -> null
            is SuspendedChatStepExecutionResult -> null
            is FailedChatStepExecutionResult -> null
        }
    }

    suspend fun ChatStep.invoke(context: ChatStepContext): ChatStepExecutionResult =
        withCoSpanId(forceNew = true) {
            if (isFirst) {
                log.debug { "Flow [${flow.id}] has started." }
                eventBus.coPublish(ChatFlowStarted(context = context))
            }
            try {
                if (suspendable && context !is SuspendableChatStepContext) {
                    log.debug { "Step [$fullName] has suspend for chat [${context.state.chatId}]." }
                    eventBus.coPublish(ChatStepSuspended(context = context))
                    return@withCoSpanId SuspendedChatStepExecutionResult()
                }
                log.debug { "Step [$fullName] invoke has started for chat [${context.state.chatId}]..." }
                eventBus.coPublish(ChatStepStarted(context = context))
                action(context)
                log.debug { "Step [$fullName] invoke has completed for chat [${context.state.chatId}]." }
                eventBus.coPublish(ChatStepCompleted(context = context))
                if (isLast) {
                    log.debug { "Flow [${flow.id}] has finished." }
                    eventBus.coPublish(ChatFlowCompleted(context = context))
                }
                CompletedChatStepExecutionResult()
            } catch (throwable: Throwable) {
                processThrowable(throwable, context)
            }
        }

    private suspend fun ChatStep.processThrowable(throwable: Throwable, context: ChatStepContext) =
        when (throwable) {
            is Goto -> {
                val stepName = throwable.stepName
                log.debug { "Moving to [$stepName] step from [$name]." }
                eventBus.coPublish(ChatStepTerminated(context = context))
                StepJumpChatStepExecutionResult(
                    jumpStep = flow.stepMap[stepName]
                        ?: throw IllegalArgumentException("Step [$stepName] does not exist!"),
                )
            }

            is GoNext -> {
                log.debug { "Moving to next step from [$name]." }
                val nextStep = terminateAndFind(next) { it.next } ?: throw IllegalArgumentException("No next step found for step [$name].")
                eventBus.coPublish(ChatStepTerminated(context = context))
                StepJumpChatStepExecutionResult(jumpStep = nextStep)
            }

            is GoPrevious -> {
                log.debug { "Moving to previous step from [$name]." }
                val previousStep = terminateAndFind(previous) { it.previous } ?: throw IllegalArgumentException("No previous step found for step [$name].")
                eventBus.coPublish(ChatStepTerminated(context = context))
                StepJumpChatStepExecutionResult(jumpStep = previousStep)
            }

            is StartFlow -> {
                log.debug { "Starting new flow [${throwable.flowName}] from [$fullName] step." }
                eventBus.coPublish(ChatStepTerminated(context = context))
                eventBus.coPublish(
                    if (isLast)
                        ChatFlowCompleted(context = context)
                    else
                        ChatFlowTerminated(context = context),
                )
                FlowJumpChatStepExecutionResult(jumpFlowName = throwable.flowName)
            }

            is StopFlow -> {
                log.debug { "Terminating flow [${flow.id}] from [$fullName] step." }
                eventBus.coPublish(ChatStepTerminated(context = context))
                eventBus.coPublish(ChatFlowTerminated(context = context))
                StopFlowChatStepExecutionResult()
            }

            else -> {
                log.debug { "Failed step [$name] step on flow [${flow.id}]." }
                eventBus.coPublish(ChatStepFailed(context = context, throwable = throwable))
                FailedChatStepExecutionResult(throwable = throwable)
            }
        }

    private fun terminateAndFind(from: ChatStep?, extractor: (ChatStep) -> ChatStep?): ChatStep? {
        var nextStep = from
        while (nextStep != null && nextStep.suspendable) {
            nextStep = extractor(nextStep)
        }
        return nextStep
    }
}
