package com.example.asteroidradar

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun dateMillis2String(timeMillis: Long, withTime: Boolean = false): String {
    val pattern = when(withTime) {
        true -> "yyyy-MM-dd HH:mm"
        false -> "yyyy-MM-dd"
    }
    val format = SimpleDateFormat(pattern, Locale.getDefault())
    val date = Date(timeMillis)
    return format.format(date)
}