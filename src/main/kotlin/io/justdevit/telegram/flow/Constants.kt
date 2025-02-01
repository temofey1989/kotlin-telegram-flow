package io.justdevit.telegram.flow

import io.justdevit.kotlin.boost.environment.property
import kotlin.time.Duration
import kotlin.time.DurationUnit.MILLISECONDS
import kotlin.time.toDuration

/**
 * Represents the delimiter used to separate segments in a chat path.
 *
 * This constant is primarily used in constructing and interpreting hierarchical paths
 * within a chat flow, enabling logical organization and routing of chat events.
 */
const val CHAT_PATH_DELIMITER = "/"

/**
 * A constant marker used to identify or differentiate a suspended step within a chat flow.
 */
const val SUSPENDED_STEP_MARKER = "${CHAT_PATH_DELIMITER}suspended${CHAT_PATH_DELIMITER}"

/**
 * A constant marker used to represent a suspended callback step within the flow.
 */
const val CALLBACK_SUSPENDED_STEP_MARKER = "${SUSPENDED_STEP_MARKER}callback"

/**
 * Represents a marker constant used to identify a suspended step during the pre-checkout phase.
 * This is typically utilized in scenarios where suspended states need to be recorded
 * or managed in Telegram bot workflows related to pre-checkout operations.
 */
const val PRE_CHECKOUT_SUSPENDED_STEP_MARKER = "${SUSPENDED_STEP_MARKER}pre_checkout"

/**
 * Represents a marker constant used to signify a suspended step with a "text" designation.
 */
const val TEXT_SUSPENDED_STEP_MARKER = "${SUSPENDED_STEP_MARKER}text"

/**
 * Marker string constant indicating the "payment" step in a suspended flow.
 */
const val SUCCESSFUL_PAYMENT_SUSPENDED_STEP_MARKER = "${SUSPENDED_STEP_MARKER}successful_payment"

/**
 * A delimiter used for separating parts of callback data in Telegram bot flows.
 */
const val CALLBACK_DATA_DELIMITER = "|"

/**
 * Specifies the lifetime of short messages in the Telegram Flow, represented as a duration.
 *
 * This value determines the time span for retaining short-lived messages,
 * typically used for ephemeral interactions within the flow. It is defined utilizing
 * configurable properties with a default of 2000 milliseconds. The value is lazily initialized.
 */
val SHORT_MESSAGE_LIFETIME: Duration by lazy {
    property<Long>("telegram-flow.short-message-lifetime", 2000L).toDuration(MILLISECONDS)
}

/**
 * Key used to identify the name of the Telegram Flow Runner within the application.
 * This constant serves as a unique identifier for managing and referencing the flow runner's name
 * in the context of Telegram bot operations.
 */
const val TELEGRAM_FLOW_RUNNER_NAME_KEY = "__telegramFlowRunnerName__"
