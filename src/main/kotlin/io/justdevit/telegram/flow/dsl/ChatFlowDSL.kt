@file:Suppress("unused")

package io.justdevit.telegram.flow.dsl

import com.github.kotlintelegrambot.dispatcher.Dispatcher
import io.justdevit.telegram.flow.model.ChatFlow
import io.justdevit.telegram.flow.model.ChatMenu

/**
 * Creates a [ChatFlow] using the given string as its ID and applying the configuration defined in the specified block.
 *
 * @param block A lambda with a receiver of type [ChatFlowBuilder] that defines the steps and optional menu configuration of the chat flow.
 * @return A [ChatFlow] instance initialized with the provided ID and the configuration defined in the block.
 */
context(_: Dispatcher)
operator fun String.minus(block: ChatFlowBuilder.() -> Unit): ChatFlow = chatFlow(this, null, block)

/**
 * A utility function to create and build a chat flow using the provided configuration.
 *
 * @param name The name of the chat flow, which is used as the command for the associated menu.
 * @param description An optional description for the chat flow menu item. Defaults to the same value as the `name`.
 * @param order An optional integer specifying the order of the chat flow in the menu. Defaults to 0.
 * @param block A lambda with a receiver of type [ChatFlowBuilder] used to define the structure and behavior of the chat flow.
 * @return A completed and configured [ChatFlow] instance based on the specified parameters and builder configuration.
 */
fun chatFlow(
    name: String,
    description: String = name,
    order: Int = 0,
    block: ChatFlowBuilder.() -> Unit,
) = chatFlow(
    name = name,
    menu = ChatMenu(command = name, description = description, order = order),
    block = block,
)

/**
 * Creates and builds a chat flow using the specified configuration and steps defined in the builder block.
 *
 * @param name The name or identifier of the chat flow, which should be unique and descriptive.
 * @param menu An optional menu of type [ChatMenu] that can be associated with the chat flow for user interactions.
 *             Defaults to `null` if not provided.
 * @param block A lambda with a receiver of [ChatFlowBuilder] to define the steps and behavior of the chat flow.
 *              This block is invoked to configure the chat flow before it is built.
 * @return A completed and configured [ChatFlow] instance based on the specified parameters and builder configuration.
 */
fun chatFlow(
    name: String,
    menu: ChatMenu? = null,
    block: ChatFlowBuilder.() -> Unit,
) = ChatFlowBuilder(name, menu).also(block).build()
