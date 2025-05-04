package io.justdevit.telegram.flow.extension

import com.github.kotlintelegrambot.entities.ParseMode

fun ParseMode?.isMarkdown() = this == ParseMode.MARKDOWN || this == ParseMode.MARKDOWN_V2
