package com.example.shoppinglist.utils

import android.content.SharedPreferences
import java.text.SimpleDateFormat
import java.util.*

object TimeManager {
    private const val DEFAULT_TIME_FORMAT = "HH:mm a - dd.MM.yyyy"

    fun getCurrentTime(): String =
        SimpleDateFormat(DEFAULT_TIME_FORMAT, Locale.getDefault())
            .format(Calendar.getInstance().time)

    fun getTimeFormat(time: String, defaultPreferences: SharedPreferences): String {
        val defaultFormatter = SimpleDateFormat(DEFAULT_TIME_FORMAT, Locale.getDefault())
        val defaultDate = defaultFormatter.parse(time)
        val newFormat = defaultPreferences.getString("time_format_key", DEFAULT_TIME_FORMAT)
        val newFormatter = SimpleDateFormat(newFormat, Locale.getDefault())

        return if (defaultDate != null) newFormatter.format(defaultDate) else time
    }
}