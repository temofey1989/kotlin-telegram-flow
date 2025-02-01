package io.justdevit.telegram.flow.extension

import com.github.kotlintelegrambot.Bot
import com.github.kotlintelegrambot.entities.ChatId
import com.github.kotlintelegrambot.entities.ChatId.Companion.fromChannelUsername
import com.github.kotlintelegrambot.entities.ChatId.Companion.fromId
import com.github.kotlintelegrambot.entities.ParseMode.MARKDOWN_V2
import com.github.kotlintelegrambot.entities.TelegramFile
import com.github.kotlintelegrambot.entities.inlinequeryresults.MimeType.APPLICATION_PDF

/**
 * Sends a message in [MARKDOWN_V2] format to the specified chat.
 *
 * @param chatId The unique identifier for the target chat.
 * @param text The message text to be sent, formatted using Markdown (v2).
 */
fun Bot.sendMarkdown(chatId: Long, text: String) =
    sendMarkdown(
        chatId = fromId(chatId),
        text = text,
    )

/**
 * Sends a message in [MARKDOWN_V2] format to the specified chat.
 *
 * @param username The username for the target channel username.
 * @param text The message text to be sent, formatted using Markdown (v2).
 */
fun Bot.sendMarkdown(username: String, text: String) =
    sendMarkdown(
        chatId = fromChannelUsername(username),
        text = text,
    )

/**
 * Sends a message in [MARKDOWN_V2] format to the specified chat.
 *
 * @param chatId Unique identifier for the target chat or username of the target channel.
 * @param text The text of the message to be sent, formatted in Markdown (v2).
 */
fun Bot.sendMarkdown(chatId: ChatId, text: String) =
    sendMessage(
        chatId = chatId,
        text = text,
        parseMode = MARKDOWN_V2,
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
