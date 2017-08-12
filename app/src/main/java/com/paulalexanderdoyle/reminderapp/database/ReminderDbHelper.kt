package com.paulalexanderdoyle.reminderapp.database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log

val DATABASE_VERSION: Int = 1
val DATABASE_NAME: String = "Reminders.db"

class ReminderDbHelper(context: Context?) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    private val SQL_CREATE_REMINDERS: String =
            "CREATE TABLE IF NOT EXISTS ${ReminderEntry.TABLE_NAME} (" +
            "${ReminderEntry._ID} INTEGER PRIMARY KEY," +
            "${ReminderEntry.COLUMN_NAME_TITLE} TEXT," +
            "${ReminderEntry.COLUMN_NAME_DUE_DATE} INTEGER," +
            "${ReminderEntry.COLUMN_NAME_CREATION_DATE} INTEGER," +
            "${ReminderEntry.COLUMN_NAME_COMPLETION_DATE} INTEGER)"

    private val SQL_DELETE_ENTRIES: String =
            "DROP TABLE IF EXISTS ${ReminderEntry.TABLE_NAME}"

    override fun onCreate(database: SQLiteDatabase?) {
        Log.i("PAULDEBUG", "Creating that there table")
        database?.execSQL(SQL_CREATE_REMINDERS) ?: print("Database object was null")
    }

    override fun onUpgrade(database: SQLiteDatabase?, p1: Int, p2: Int) {
        // No upgrade policy yet
    }
}