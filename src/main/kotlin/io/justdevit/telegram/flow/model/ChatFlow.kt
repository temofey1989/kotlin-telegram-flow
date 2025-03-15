@file:Suppress("unused", "MemberVisibilityCanBePrivate")

package io.justdevit.telegram.flow.model

import io.justdevit.kotlin.boost.extension.hasWhitespaces

/**
 * Represents a chat flow, defining a sequence of steps and an optional menu for interaction.
 *
 * @property id The ID of the chat flow, which must not be blank and has no whitespaces.
 * @property menu An optional [ChatMenu] associated with this chat flow, providing additional commands or actions.
 * @property steps A list of [ChatStep] objects that form the sequence of steps in the chat flow. Defaults to an empty list.
 * @constructor Initializes the chat flow and validates that the name is not blank.
 */
data class ChatFlow(
    val id: String,
    val menu: ChatMenu? = null,
    val steps: List<ChatStep> = emptyList(),
) {

    init {
        require(id.isNotBlank()) {
            "Chat Flow ID cannot be blank."
        }
        require(!id.hasWhitespaces()) {
            "Chat Flow ID cannot have whitespaces."
        }
    }

    val stepMap: Map<String, ChatStep>
        get() = steps.associateBy { it.name }

    val firstStep: ChatStep
        get() = steps.first()

    val lastStep: ChatStep
        get() = steps.last()

    override fun toString() = "ChatFlow(name='$id', steps=${steps.map { it.name }})"
}
