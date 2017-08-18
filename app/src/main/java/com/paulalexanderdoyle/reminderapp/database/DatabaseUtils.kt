package com.paulalexanderdoyle.reminderapp.database

import android.database.Cursor
import java.util.*

fun getStringCol(cursor: Cursor?, columnName: String): String {
    return cursor?.getString(cursor.getColumnIndex(columnName)) ?: ""
}

fun getDateCol(cursor: Cursor?, columnName: String): Date? {
    var timestamp: Long? = null
    // If db row value is non-null (and cursor is non-null)
    if (!(cursor?.isNull(cursor.getColumnIndex(columnName)) ?: true)) {
        timestamp = cursor?.getLong(cursor.getColumnIndex(columnName))
    }
    return if (timestamp != null) Date(timestamp) else null
}
