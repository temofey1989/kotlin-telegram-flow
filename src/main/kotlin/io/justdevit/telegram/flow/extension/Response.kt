package io.justdevit.telegram.flow.extension

import com.github.kotlintelegrambot.entities.Message
import com.github.kotlintelegrambot.network.Response
import com.github.kotlintelegrambot.types.TelegramBotResult
import retrofit2.Response as RetrofitResponse

fun Pair<RetrofitResponse<Response<Message>?>?, Exception?>.toTelegramBotResult(): TelegramBotResult<Message> =
    first
        ?.body()
        ?.takeIf { it.ok }
        ?.result
        ?.let { TelegramBotResult.Success(it) }
        ?: TelegramBotResult.Error.Unknown(second ?: IllegalStateException("No exception for photo message."))
