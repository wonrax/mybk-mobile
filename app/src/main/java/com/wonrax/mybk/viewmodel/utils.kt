package com.wonrax.mybk.viewmodel

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun formatDateToDisplay(
    dateString: String,
    dateFormat: String = "yyyy-MM-dd HH:mm:ss"
): String? {
    var displayDate: String? = null
    try {
        var date: Date? = null
        date = SimpleDateFormat(
            dateFormat,
            Locale.ENGLISH
        ).parse(dateString)
        if (date != null) {
            displayDate = SimpleDateFormat(
                "dd-MM-yyyy HH:mm",
                Locale.getDefault()
            ).format(date)
        }
    } catch (e: Exception) {
        // Ignore
        e.printStackTrace()
    }

    return displayDate
}
