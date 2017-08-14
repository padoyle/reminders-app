package com.paulalexanderdoyle.reminderapp.data

import android.database.Cursor
import com.paulalexanderdoyle.reminderapp.database.ReminderEntry
import com.paulalexanderdoyle.reminderapp.database.getDateCol
import com.paulalexanderdoyle.reminderapp.database.getStringCol
import java.util.*

class Reminder(var id: Long, var title: String, var dueDate: Date?) {
    var creationDate: Date? = null
    var completedDate: Date? = null

    constructor(cursor: Cursor?) : this(cursor?.getLong(cursor.getColumnIndex(ReminderEntry._ID)) ?: 0,
            getStringCol(cursor, ReminderEntry.COLUMN_NAME_TITLE),
            getDateCol(cursor, ReminderEntry.COLUMN_NAME_DUE_DATE)) {
        creationDate = getDateCol(cursor, ReminderEntry.COLUMN_NAME_CREATION_DATE)
        completedDate = getDateCol(cursor, ReminderEntry.COLUMN_NAME_COMPLETION_DATE)
    }
}