package com.paulalexanderdoyle.reminderapp.data

import android.content.ContentValues
import android.provider.BaseColumns

object ReminderEntry : BaseColumns {
    val TABLE_NAME: String = "reminder"
    val _ID: String = "_id"
    val COLUMN_NAME_TITLE: String = "title"
    val COLUMN_NAME_DUE_DATE: String = "dueDate"
    val COLUMN_NAME_CREATION_DATE: String = "creationDate"
    val COLUMN_NAME_COMPLETION_DATE: String = "completionDate"

    fun getContentValues(title: String, due: Long?, created: Long?, completed: Long?): ContentValues {
        val values: ContentValues = ContentValues()
        values.put(COLUMN_NAME_TITLE, title)
        values.put(COLUMN_NAME_DUE_DATE, due)
        values.put(COLUMN_NAME_CREATION_DATE, created)
        values.put(COLUMN_NAME_COMPLETION_DATE, completed)

        return values
    }

    fun getContentValues(reminder: Reminder): ContentValues {
        return getContentValues(reminder.title, reminder.dueDate?.time, reminder.creationDate?.time, reminder.completedDate?.time)
    }
}