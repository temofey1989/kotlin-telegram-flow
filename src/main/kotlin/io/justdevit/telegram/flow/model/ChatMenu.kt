package io.justdevit.telegram.flow.model

/**
 * Represents a menu item in a chat interface, typically used for defining
 * commands available to users along with their descriptions and optional order.
 *
 * @property command The unique command representing this menu item.
 * @property description An optional description providing details about the command. Default is same as `command` value.
 * @property order The order in which this menu item should appear, defaults to 0.
 */
data class ChatMenu(
    val command: String,
    val description: String = command,
    val order: Int = 0,
)
