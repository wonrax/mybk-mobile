package com.wonrax.mybk.network.utils

object HttpUtils {
    /**
     * Turn an HTTP url to an HTTPS URL
     * @param url The HTTP url to be converted
     */
    fun httpToHttpsURL(url: String): String {
        return url.substring(0, 4) + 's' + url.substring(4)
    }
}
