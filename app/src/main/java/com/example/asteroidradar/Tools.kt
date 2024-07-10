package com.example.asteroidradar

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun dateMillis2String(timeMillis: Long): String {
    val format = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    val date = Date(timeMillis)
    return format.format(date)
}

fun dateString2Millis(dateString: String): Long {
    val format = SimpleDateFormat("yyyy-MMM-dd HH:mm", Locale.ENGLISH)
    return format.parse(dateString)?.time ?: 0L
}