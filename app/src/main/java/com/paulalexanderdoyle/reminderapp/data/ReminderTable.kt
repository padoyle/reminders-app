package com.paulalexanderdoyle.reminderapp.data

import android.content.ContentValues
import android.provider.BaseColumns

object ReminderTable : BaseColumns {
    val TABLE_NAME: String = "reminder"
    val _ID: String = "_id"
    val COL_TITLE: String = "title"
    val COL_DUE_DATE: String = "dueDate"
    val COL_CREATION_DATE: String = "creationDate"
    val COL_COMPLETION_DATE: String = "completionDate"

    fun getContentValues(title: String, due: Long?, created: Long?, completed: Long?): ContentValues {
        val values: ContentValues = ContentValues()
        values.put(COL_TITLE, title)
        values.put(COL_DUE_DATE, due)
        values.put(COL_CREATION_DATE, created)
        values.put(COL_COMPLETION_DATE, completed)

        return values
    }

    fun getContentValues(reminder: Reminder): ContentValues {
        return getContentValues(reminder.title, reminder.dueDate?.time, reminder.creationDate?.time, reminder.completedDate?.time)
    }
}