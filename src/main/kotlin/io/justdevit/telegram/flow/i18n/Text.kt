package io.justdevit.telegram.flow.i18n

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory
import com.fasterxml.jackson.module.kotlin.readValue
import org.kodein.emoji.Emoji
import org.kodein.emoji.EmojiTemplateCatalog
import org.kodein.emoji.list

private const val UNDEFINED_LANGUAGE_VALUE = ""
private val catalog = EmojiTemplateCatalog(Emoji.list())

/**
 * Represents a text entity with language-specific translations. This class is designed
 * to handle text internationalization (i18n) by providing dynamic access to text templates
 * and replacing placeholders with corresponding parameters.
 *
 * @property language The language of the text. If null, signifies undefined or default language.
 * @property values A map containing key-value pairs of text translations.
 */
data class Text(val language: String? = null, private val values: Map<String, String> = emptyMap()) {

    operator fun invoke(key: String, params: Map<String, String> = emptyMap()): String {
        val text = values[key] ?: return key.escapeMarkdown()
        return params
            .keys
            .fold(text) { acc, k -> acc.replace("{$k}", params[k].toString().escapeMarkdown()) }
            .let { catalog.replace(it) }
    }
}

/**
 * The `Texts` object provides functionality to manage and retrieve localized text resources.
 * It supports internationalization by dynamically loading and accessing text templates for
 * different languages and replacing placeholders with runtime parameters.
 *
 * This object is designed to read texts from YAML files and handle deep key mappings
 * by propagating hierarchical key-value pairs. It also offers Markdown-escaping capabilities
 * to safely use text containing special Markdown characters.
 */
object Texts {

    private val textMap = mutableMapOf<String, Text>()

    operator fun invoke(
        key: String,
        language: String = UNDEFINED_LANGUAGE_VALUE,
        paramsBuilder: MutableMap<String, String>.() -> Unit = {},
    ): String {
        buildText(language)
        val text = textMap[language] ?: return key.escapeMarkdown()
        val params = buildMap {
            paramsBuilder()
        }
        return text(key, params)
    }

    private fun buildText(language: String) {
        if (textMap.containsKey(language)) {
            return
        }
        textMap[language] = Text(
            language,
            buildMap {
                val content = this::class.java
                    .getResource("/text${language.takeIf { it.isNotEmpty() }?.let { "_${it.lowercase()}" } ?: ""}.yaml")!!
                    .readText()
                val map = ObjectMapper(YAMLFactory()).readValue<Map<Any, Any>>(content)
                map.forEach {
                    this.propagate(it.key.toString(), it.value)
                }
            },
        )
    }

    private fun MutableMap<String, String>.propagate(key: String, value: Any?) {
        when (value) {
            is Map<*, *> -> value.forEach {
                this.propagate("$key.${it.key}", it.value ?: "")
            }

            else -> this[key] = value?.toString() ?: ""
        }
    }
}

/**
 * Retrieves a localized text string based on the given key and optional language and parameters.
 * This function simplifies working with internationalized text resources, dynamically replacing
 * placeholders with provided parameter values.
 *
 * @param key The key used to identify the desired text string in the resource files.
 * @param language The language in which the text should be retrieved. Defaults to an undefined language key.
 * @param paramsBuilder A lambda function to define key-value pairs of parameters used to replace placeholders
 * in the text string. Defaults to an empty parameters map.
 *
 * @return Translated test.
 */
@Suppress("FunctionName")
fun T(
    key: String,
    language: String = UNDEFINED_LANGUAGE_VALUE,
    paramsBuilder: MutableMap<String, String>.() -> Unit = {},
) = Texts.invoke(key, language, paramsBuilder)
