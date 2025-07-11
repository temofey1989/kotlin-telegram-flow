package io.justdevit.telegram.flow.extension

import com.github.kotlintelegrambot.Bot
import com.github.kotlintelegrambot.entities.ChatId
import com.github.kotlintelegrambot.entities.ChatId.Companion.fromChannelUsername
import com.github.kotlintelegrambot.entities.ChatId.Companion.fromId
import com.github.kotlintelegrambot.entities.InlineKeyboardMarkup
import com.github.kotlintelegrambot.entities.Message
import com.github.kotlintelegrambot.entities.ParseMode
import com.github.kotlintelegrambot.entities.ParseMode.MARKDOWN_V2
import com.github.kotlintelegrambot.entities.TelegramFile
import com.github.kotlintelegrambot.entities.inlinequeryresults.MimeType.APPLICATION_PDF
import com.github.kotlintelegrambot.entities.keyboard.InlineKeyboardButton
import io.justdevit.kotlin.boost.logging.logger
import io.justdevit.telegram.flow.DEFAULT_LINK_LABEL
import io.justdevit.telegram.flow.model.GeoCoordinates
import java.net.URL

/**
 * Sends a message in [MARKDOWN_V2] format to the specified chat.
 *
 * @param chatId The unique identifier for the target chat.
 * @param text The message text to be sent, formatted using Markdown (v2).
 */
fun Bot.sendMarkdown(
    chatId: Long,
    text: String,
    image: TelegramFile? = null,
) = sendMarkdown(
    chatId = fromId(chatId),
    text = text,
    image = image,
)

/**
 * Sends a message in [MARKDOWN_V2] format to the specified chat.
 *
 * @param username The username for the target channel username.
 * @param text The message text to be sent, formatted using Markdown (v2).
 */
fun Bot.sendMarkdown(
    username: String,
    text: String,
    image: TelegramFile? = null,
) = sendMarkdown(
    chatId = fromChannelUsername(username),
    text = text,
    image = image,
)

/**
 * Sends a message in [MARKDOWN_V2] format to the specified chat.
 *
 * @param chatId Unique identifier for the target chat or username of the target channel.
 * @param text The text of the message to be sent, formatted in Markdown (v2).
 */
fun Bot.sendMarkdown(
    chatId: ChatId,
    text: String,
    image: TelegramFile? = null,
) = if (image == null) {
    sendMessage(
        chatId = chatId,
        text = text,
        parseMode = MARKDOWN_V2,
    )
} else {
    sendPhoto(
        chatId = chatId,
        photo = image,
        caption = text,
        parseMode = MARKDOWN_V2,
    )
}

/**
 * Sends a link to the specified chat. The link can optionally be accompanied by an image and custom label.
 *
 * @param username The username of the chat where the link should be sent.
 * @param url The URL to be shared in the chat.
 * @param label The label for the clickable link. Default is [DEFAULT_LINK_LABEL].
 * @param image An optional image to be sent alongside the link.
 * @param parseMode The parse mode for the message text. Default is [MARKDOWN_V2].
 * @param supplier A lambda function to supply the link description or message text. Defaults to the string representation of the URL.
 *
 * @return The message sent as a result of invoking this method.
 */
fun Bot.sendLink(
    username: String,
    url: URL,
    label: String = DEFAULT_LINK_LABEL,
    image: TelegramFile? = null,
    parseMode: ParseMode? = MARKDOWN_V2,
    supplier: () -> String = { url.toString() },
) = sendLink(
    chatId = fromChannelUsername(username),
    url = url,
    label = label,
    image = image,
    parseMode = parseMode,
    supplier = supplier,
)

/**
 * Sends a link to the specified chat. The link can optionally be accompanied by an image and custom label.
 *
 * @param chatId The [Long] identifier of the chat where the link should be sent.
 * @param url The URL to be shared in the chat.
 * @param label The label for the clickable link. Default is [DEFAULT_LINK_LABEL].
 * @param image An optional image to be sent alongside the link.
 * @param parseMode The parse mode for the message text. Default is [MARKDOWN_V2].
 * @param supplier A lambda function to supply the link description or message text. Defaults to the string representation of the URL.
 *
 * @return The message sent as a result of invoking this method.
 */
fun Bot.sendLink(
    chatId: Long,
    url: URL,
    label: String = DEFAULT_LINK_LABEL,
    image: TelegramFile? = null,
    parseMode: ParseMode? = MARKDOWN_V2,
    supplier: () -> String = { url.toString() },
) = sendLink(
    chatId = fromId(chatId),
    url = url,
    label = label,
    image = image,
    parseMode = parseMode,
    supplier = supplier,
)

/**
 * Sends a link to the specified chat. The link can optionally be accompanied by an image and custom label.
 *
 * @param chatId The identifier of the chat where the link should be sent.
 * @param url The URL to be shared in the chat.
 * @param label The label for the clickable link. Default is [DEFAULT_LINK_LABEL].
 * @param image An optional image to be sent alongside the link.
 * @param parseMode The parse mode for the message text. Default is [MARKDOWN_V2].
 * @param supplier A lambda function to supply the link description or message text. Defaults to the string representation of the URL.
 *
 * @return The message sent as a result of invoking this method.
 */
fun Bot.sendLink(
    chatId: ChatId,
    url: URL,
    label: String = DEFAULT_LINK_LABEL,
    image: TelegramFile? = null,
    parseMode: ParseMode? = MARKDOWN_V2,
    supplier: () -> String = { url.toString() },
): Message {
    val text = supplier()
    val replyMarkup = InlineKeyboardMarkup.createSingleButton(
        InlineKeyboardButton.Url(text = label, url = url.toString()),
    )
    logger(Bot::class.java).debug { "Sending Link to chat [$chatId]: $url" }
    return (
        if (image != null) {
            sendPhoto(
                chatId = chatId,
                photo = image,
                caption = text,
                parseMode = parseMode,
                replyMarkup = replyMarkup,
            ).toMessage()
        } else {
            sendMessage(
                chatId = chatId,
                text = text,
                parseMode = parseMode,
                replyMarkup = replyMarkup,
            ).get()
        }
    )
}

/**
 * Sends the geographical location to the specified chat.
 *
 * @param chatId The [Long] identifier of the chat where the location will be sent.
 * @param coordinates The geographical coordinates (latitude and longitude) of the location to send.
 */
fun Bot.sendLocation(chatId: Long, coordinates: GeoCoordinates) =
    sendLocation(
        chatId = fromId(chatId),
        latitude = coordinates.latitude.toFloat(),
        longitude = coordinates.longitude.toFloat(),
    )

/**
 * Sends the geographical location to the specified chat.
 *
 * @param username The username of the chat where the location will be sent.
 * @param coordinates The geographical coordinates (latitude and longitude) of the location to send.
 */
fun Bot.sendLocation(username: String, coordinates: GeoCoordinates) =
    sendLocation(
        chatId = fromChannelUsername(username),
        latitude = coordinates.latitude.toFloat(),
        longitude = coordinates.longitude.toFloat(),
    )

/**
 * Sends the geographical location to the specified chat.
 *
 * @param chatId The identifier of the chat where the location will be sent.
 * @param coordinates The geographical coordinates (latitude and longitude) of the location to send.
 */
fun Bot.sendLocation(chatId: ChatId, coordinates: GeoCoordinates) =
    sendLocation(
        chatId = chatId,
        latitude = coordinates.latitude.toFloat(),
        longitude = coordinates.longitude.toFloat(),
    )

/**
 * Sends the location specified by latitude and longitude to the given chat.
 *
 * @param chatId The [Long] identifier of the chat where the location will be sent.
 * @param latitude The latitude coordinate of the location to send.
 * @param longitude The longitude coordinate of the location to send.
 */
fun Bot.sendLocation(
    chatId: Long,
    latitude: Float,
    longitude: Float,
) = sendLocation(
    chatId = fromId(chatId),
    latitude = latitude,
    longitude = longitude,
)

/**
 * Sends the location specified by latitude and longitude to the given chat.
 *
 * @param username The username of the chat where the location will be sent.
 * @param latitude The latitude coordinate of the location to send.
 * @param longitude The longitude coordinate of the location to send.
 */
fun Bot.sendLocation(
    username: String,
    latitude: Float,
    longitude: Float,
) = sendLocation(
    chatId = fromChannelUsername(username),
    latitude = latitude,
    longitude = longitude,
)

/**
 * Sends the location specified by latitude and longitude to the given chat.
 *
 * @param chatId The identifier of the chat where the location will be sent. This can be a numeric chat ID or a channel username.
 * @param latitude The latitude coordinate of the location to send.
 * @param longitude The longitude coordinate of the location to send.
 */
fun Bot.sendLocation(
    chatId: ChatId,
    latitude: Float,
    longitude: Float,
) = sendLocation(
    chatId = chatId,
    latitude = latitude,
    longitude = longitude,
)

/**
 * Sends a PDF document to a specified chat.
 *
 * @param chatId The unique identifier for the target chat.
 * @param name The name of the PDF file to be sent.
 * @param data The byte array containing the PDF file's data.
 */
fun Bot.sendPdf(
    chatId: Long,
    name: String,
    data: ByteArray,
) = sendPdf(
    chatId = fromId(chatId),
    name = name,
    data = data,
)

/**
 * Sends a PDF document to a specified chat.
 *
 * @param username The username for the target channel username.
 * @param name The name of the PDF file to be sent.
 * @param data The byte array containing the PDF file's data.
 */
fun Bot.sendPdf(
    username: String,
    name: String,
    data: ByteArray,
) = sendPdf(
    chatId = fromChannelUsername(username),
    name = name,
    data = data,
)

/**
 * Sends a PDF document to a specified chat.
 *
 * @param chatId The unique identifier for the target chat or username of the target channel.
 * @param name The name of the PDF file to be sent.
 * @param data The byte array containing the PDF file's data.
 */
fun Bot.sendPdf(
    chatId: ChatId,
    name: String,
    data: ByteArray,
) = sendDocument(
    chatId = chatId,
    document = TelegramFile.ByByteArray(
        fileBytes = data,
        filename = name,
    ),
    mimeType = APPLICATION_PDF.rawName,
)

/**
 * Sends a CSV file as a document to a specified chat.
 *
 * @param chatId The identifier of the chat where the CSV file will be sent.
 * @param name The name of the CSV file, including its extension.
 * @param content The content of the CSV file as a string.
 */
fun Bot.sendCsv(
    chatId: Long,
    name: String,
    content: String,
) = sendCsv(
    chatId = fromId(chatId),
    name = name,
    content = content,
)

/**
 * Sends a CSV file as a document to a specified chat.
 *
 * @param username The channel username of the chat where the CSV file will be sent.
 * @param name The name of the CSV file, including its extension.
 * @param content The content of the CSV file as a string.
 */
fun Bot.sendCsv(
    username: String,
    name: String,
    content: String,
) = sendCsv(
    chatId = fromChannelUsername(username),
    name = name,
    content = content,
)

/**
 * Sends a CSV file as a document to a specified chat.
 *
 * @param chatId The identifier of the chat where the CSV file will be sent.
 * @param name The name of the CSV file, including its extension.
 * @param content The content of the CSV file as a string.
 */
fun Bot.sendCsv(
    chatId: ChatId,
    name: String,
    content: String,
) = sendDocument(
    chatId = chatId,
    document = TelegramFile.ByByteArray(
        fileBytes = content.toByteArray(),
        filename = name,
    ),
    mimeType = "text/csv",
)
