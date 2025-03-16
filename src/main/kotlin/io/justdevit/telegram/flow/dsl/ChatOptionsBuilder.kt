package io.justdevit.telegram.flow.dsl

import io.justdevit.telegram.flow.model.ChatOption
import io.justdevit.telegram.flow.model.ChatOptions

/**
 * A builder class for constructing a [ChatOptions] object with multiple chat option configurations.
 *
 * @param question The main question or prompt to use in the chat options.
 */
class ChatOptionsBuilder(private val question: String) {

    private val options = mutableListOf<List<ChatOption>>()

    /**
     * Adds a chat option to the list of options with a specified value and label.
     *
     * @param value The value of the chat option. This acts as the identifier or payload for the option.
     * @param label The display label for the chat option. Defaults to the string representation of the value if not provided.
     */
    fun option(value: Any, label: String = value.toString()) {
        options += listOf(ChatOption(value = value.toString(), label = label))
    }

    /**
     * Adds a list of chat options to the existing options. Each option is represented as a pair of a value and an optional label.
     * If the label is null, the value's string representation will be used as the label.
     *
     * @param options A list of options represented by key-value pairs.
     */
    fun option(options: List<Pair<Any, String?>>) {
        if (options.isNotEmpty()) {
            this.options += options.map { ChatOption(it.first.toString(), it.second ?: it.first.toString()) }
        }
    }

    /**
     * Adds a list of chat options to the existing options. Each option is represented as a pair of a value and an optional label.
     * If the label is null, the value's string representation will be used as the label.
     *
     * @param options A variable number of key-value pairs.
     */
    fun option(vararg options: Pair<Any, String?>) = option(options.toList())

    /**
     * Constructs and returns a [ChatOptions] object using the current configuration of the builder.
     *
     * @return A [ChatOptions] instance containing the configured question and list of chat options.
     */
    fun build() =
        ChatOptions(
            question = question,
            items = options.toList(),
        )
}
