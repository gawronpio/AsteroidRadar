package com.example.asteroidradar

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun dateMillis2String(timeMillis: Long): String {
    val format = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    val date = Date(timeMillis)
    return format.format(date)
}