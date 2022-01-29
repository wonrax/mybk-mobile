package com.wonrax.mybk.network.utils

import java.util.regex.Matcher
import java.util.regex.Pattern

object HtmlUtils {
    /**
     * Get value of a HTML element.
     * @param tag Specify the tag of the element
     * @param name Specify the name attribute of the element
     * @param input The string to be parsed
     * @param attr The attribute containing the value
     */
    fun getHtmlElementValue(
        input: String,
        tag: String = "",
        name: String,
        attr: String = "value"
    ): String? {
        val r: Pattern = Pattern.compile("<$tag.*?name=\"${name}\".*?$attr=\"([^\"]*)\"[^>]*?/>")
        val m: Matcher = r.matcher(input)
        if (m.find()) {
            return m.group(1)
        }
        return null
    }
}
