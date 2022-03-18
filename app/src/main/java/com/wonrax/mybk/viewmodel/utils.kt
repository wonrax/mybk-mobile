package com.wonrax.mybk.viewmodel

import android.content.Context
import android.content.Intent
import android.net.Uri
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun formatDateToDisplay(
    dateString: String,
    dateFormat: String = "yyyy-MM-dd HH:mm:ss"
): String? {
    var displayDate: String? = null
    try {
        var date: Date? = SimpleDateFormat(
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

fun openBrowser(context: Context, url: String) {
    val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
    context.startActivity(browserIntent)
}
