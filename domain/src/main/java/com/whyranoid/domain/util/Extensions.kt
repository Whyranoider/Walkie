package com.whyranoid.domain.util

import java.text.SimpleDateFormat
import java.time.Duration
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale

val String.Companion.EMPTY: String get() = ""

val String.Companion.BLANK: String get() = " "

val String.Companion.DATE_FORMAT: String get() = "yyyy-MM-dd HH:mm:ss"

val dateFormatter = SimpleDateFormat(String.DATE_FORMAT)

fun getToday(): String {
    val today = LocalDateTime.now()
    val formatter = DateTimeFormatter.ofPattern(String.DATE_FORMAT)
    return today.format(formatter)
}

fun getDurationDifference(startDate: String, endDate: String): List<Long> {

    val formatter = DateTimeFormatter.ofPattern(String.DATE_FORMAT)

    val startDateTime = LocalDateTime.parse(startDate, formatter)
    val endDateTime = LocalDateTime.parse(endDate, formatter)

    val duration = Duration.between(endDateTime, startDateTime)
    val days = duration.toDays()
    val hours = duration.toHours() % 24
    val minutes = duration.toMinutes() % 60

    return listOf(days, hours, minutes)
}

fun Long.toFormattedTimeStamp(): String {
    val date = Date(this)
    val format = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
    return format.format(date)
}