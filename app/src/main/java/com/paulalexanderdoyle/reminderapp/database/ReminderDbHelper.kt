package com.paulalexanderdoyle.reminderapp.database

import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.util.Log
import com.paulalexanderdoyle.reminderapp.data.Reminder
import com.paulalexanderdoyle.reminderapp.data.ReminderTable
import org.jetbrains.anko.db.*
import java.util.*

val DATABASE_VERSION: Int = 2
val DATABASE_NAME: String = "Reminders.db"

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

val Context.database: ReminderDbHelper
    get() = ReminderDbHelper.getInstance(applicationContext)

class ReminderDbHelper private constructor(context: Context) :
        ManagedSQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    companion object {
        private var instance: ReminderDbHelper? = null

        @Synchronized
        fun getInstance(context: Context): ReminderDbHelper {
            if (instance == null) {
                instance = ReminderDbHelper(context)
            }
            return instance!!
        }
    }

    // Temporary measure to workaround a problem with Anko 0.10.1 SqlTypeImpl
    private open class SqlTypeImpl(override val name: String, val modifiers: String? = null) : SqlType {
        override fun render() = if (modifiers == null) name else "$name $modifiers"

        override fun plus(m: SqlTypeModifier): SqlType {
            return SqlTypeImpl(name, if (modifiers == null) m.modifier else "$modifiers ${m.modifier}")
        }
    }

    override fun onCreate(database: SQLiteDatabase) {
        Log.i(javaClass.simpleName, "Creating that there table")

        database.createTable(ReminderTable.TABLE_NAME, true,
                ReminderTable._ID to SqlTypeImpl(INTEGER.name, "${PRIMARY_KEY.modifier} ${UNIQUE.modifier}"),
                ReminderTable.COL_TITLE to TEXT,
                ReminderTable.COL_DUE_DATE to INTEGER,
                ReminderTable.COL_COMPLETION_DATE to INTEGER,
                ReminderTable.COL_CREATION_DATE to INTEGER)
    }

    override fun onUpgrade(database: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        database.dropTable(ReminderTable.TABLE_NAME)
        onCreate(database)
    }

    fun insertItem(rem: Reminder): Long {
        var id: Long = -1
        use {
            id = insert(ReminderTable.TABLE_NAME,
                    ReminderTable.COL_TITLE to rem.title,
                    ReminderTable.COL_DUE_DATE to rem.dueDate,
                    ReminderTable.COL_COMPLETION_DATE to rem.completedDate,
                    ReminderTable.COL_CREATION_DATE to rem.creationDate)
        }
        return id
    }

    fun deleteItem(id: Long) {
        use {
            delete(ReminderTable.TABLE_NAME, "${ReminderTable._ID}=$id", null)
        }
    }

    fun setCompletion(id: Long, date: Date?) {
        use {
            update(ReminderTable.TABLE_NAME, ReminderTable.COL_COMPLETION_DATE to date?.time)
                    .whereArgs("${ReminderTable._ID}={remId}", "remId" to id).exec()
        }
    }

    // Returns id of item updated if update occurs
    fun updateInfo(rem: Reminder): Long {
        var modified = 0
        use {
            modified = update(ReminderTable.TABLE_NAME,
                    ReminderTable.COL_TITLE to rem.title,
                    ReminderTable.COL_DUE_DATE to rem.dueDate,
                    ReminderTable.COL_COMPLETION_DATE to rem.completedDate,
                    ReminderTable.COL_CREATION_DATE to rem.creationDate)
                    .whereArgs("${ReminderTable._ID}={remId}", "remId" to rem.id).exec()
        }
        return if (modified > 0) rem.id else -1
    }
}