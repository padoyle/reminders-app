package com.paulalexanderdoyle.reminderapp.data

import android.database.Cursor
import com.paulalexanderdoyle.reminderapp.database.getDateCol
import com.paulalexanderdoyle.reminderapp.database.getStringCol
import java.util.*

class Reminder(var id: Long, var title: String, var dueDate: Date?, var creationDate: Date?) {
    var completedDate: Date? = null

    constructor(cursor: Cursor?) : this(cursor?.getLong(cursor.getColumnIndex(ReminderTable._ID)) ?: 0,
            getStringCol(cursor, ReminderTable.COL_TITLE),
            getDateCol(cursor, ReminderTable.COL_DUE_DATE),
            getDateCol(cursor, ReminderTable.COL_CREATION_DATE)) {
        completedDate = getDateCol(cursor, ReminderTable.COL_COMPLETION_DATE)
    }
}