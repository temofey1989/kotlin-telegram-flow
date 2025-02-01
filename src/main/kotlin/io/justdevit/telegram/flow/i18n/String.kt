package io.justdevit.telegram.flow.i18n

/**
 * Escapes special Markdown characters in the string, making it safe for usage
 * in Markdown-formatted content. This method handles characters such as
 * backslashes, asterisks, underscores, braces, brackets, parentheses,
 * angle brackets, and several others, ensuring they are properly escaped
 * to prevent undesired Markdown formatting.
 *
 * @return A new string with all special Markdown characters appropriately escaped.
 *         If the original string is null, an empty string is returned.
 */
fun String?.escapeMarkdown(): String =
    if (this == null)
        ""
    else
        replace("\\", "\\\\")
            .replace("`", "\\`")
            .replace("*", "\\*")
            .replace("_", "\\_")
            .replace("{", "\\{")
            .replace("}", "\\}")
            .replace("[", "\\[")
            .replace("]", "\\]")
            .replace("<", "\\<")
            .replace(">", "\\>")
            .replace("(", "\\(")
            .replace(")", "\\)")
            .replace("#", "\\#")
            .replace("+", "\\+")
            .replace("-", "\\-")
            .replace(".", "\\.")
            .replace("!", "\\!")
            .replace("|", "\\|")
