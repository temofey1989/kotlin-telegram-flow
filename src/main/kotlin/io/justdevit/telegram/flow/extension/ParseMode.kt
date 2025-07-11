package io.justdevit.telegram.flow.extension

import com.github.kotlintelegrambot.entities.ParseMode

/**
 * Checks whether the current [ParseMode] is Markdown mode.
 *
 * @return `true` if the [ParseMode] is Markdown mode, `false` otherwise.
 */
fun ParseMode?.isMarkdown() = this == ParseMode.MARKDOWN || this == ParseMode.MARKDOWN_V2
