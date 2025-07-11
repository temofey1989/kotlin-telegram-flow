package io.justdevit.telegram.flow.extension

import com.github.kotlintelegrambot.entities.Message
import com.github.kotlintelegrambot.network.Response
import com.github.kotlintelegrambot.types.TelegramBotResult
import retrofit2.Response as RetrofitResponse

/**
 * Converts a [Pair] of `RetrofitResponse<Response<Message>?>?` and `Exception?` into a [Message] object.
 * If the [Message] cannot be extracted, an [IllegalStateException] is thrown.
 *
 * @return The [Message] extracted from the response if available and valid.
 * @throws IllegalStateException if the response does not contain a valid message.
 */
fun Pair<RetrofitResponse<Response<Message>?>?, Exception?>.toMessage(): Message = toMessageOrNull() ?: throw IllegalStateException("No message in response.")

/**
 * Converts a [Pair] of `RetrofitResponse<Response<Message>?>?` and `Exception?` into a [Message] object.
 * If the [Message] cannot be extracted, `null` will be returned.
 *
 * @return The [Message] extracted from the response if available and valid or `null` otherwise.
 */
fun Pair<RetrofitResponse<Response<Message>?>?, Exception?>.toMessageOrNull(): Message? =
    first
        ?.body()
        ?.takeIf { it.ok }
        ?.result

/**
 * Converts the [Pair] containing a `RetrofitResponse<Response<Message>?>?` and an `Exception?`
 * into a [TelegramBotResult] object.
 *
 * @return A [TelegramBotResult.Success] containing the extracted [Message] if available, or a [TelegramBotResult.Error.Unknown] otherwise.
 */
fun Pair<RetrofitResponse<Response<Message>?>?, Exception?>.toTelegramBotResult(): TelegramBotResult<Message> =
    toMessageOrNull()
        ?.let { TelegramBotResult.Success(it) }
        ?: TelegramBotResult.Error.Unknown(second ?: IllegalStateException("No exception for photo message."))
