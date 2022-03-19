package com.example.shoppinglist.utils

import java.text.SimpleDateFormat
import java.util.*

object TimeManager {
    fun getCurrentTime(): String =
        SimpleDateFormat("HH:mm - dd.MM.yy", Locale.getDefault())
            .format(Calendar.getInstance().time)
}