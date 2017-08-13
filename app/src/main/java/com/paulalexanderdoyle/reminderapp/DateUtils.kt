package com.paulalexanderdoyle.reminderapp

import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

val dateHeaderFormat: DateFormat = SimpleDateFormat("E, MMM d, yyyy", Locale.getDefault())

fun isSameDay(date1: Date?, date2: Date?): Boolean {
    val cal1: Calendar = Calendar.getInstance()
    val cal2: Calendar = Calendar.getInstance()
    cal1.time = date1
    cal2.time = date2

    return cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
            cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR)
}
