package io.justdevit.telegram.flow.extension

import com.github.kotlintelegrambot.entities.CallbackQuery
import io.justdevit.telegram.flow.CALLBACK_DATA_DELIMITER

/**
 * Retrieves the value of the callback query by extracting data following the defined delimiter.
 */
val CallbackQuery.value: String
    get() = data
        .replaceBefore(CALLBACK_DATA_DELIMITER, "")
        .removePrefix(CALLBACK_DATA_DELIMITER)
