package com.paulalexanderdoyle.reminderapp

import android.content.ContentValues
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.paulalexanderdoyle.reminderapp.database.ReminderDbHelper
import com.paulalexanderdoyle.reminderapp.database.ReminderEntry
import kotlinx.android.synthetic.main.dialog_add_reminder.*
import java.util.*

class EditReminderDialog : DialogFragment() {

    private val MILLIS_PER_DAY: Long = 24 * 3600 * 1000
    private var addRowCallback: (Long) -> Unit = {}

    private val mDatabaseHelper: SQLiteOpenHelper by lazy {
        ReminderDbHelper(context)
    }

    fun init(addRowCallback: (Long) -> Unit) {
        this.addRowCallback = addRowCallback
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view: View? = inflater?.inflate(R.layout.dialog_add_reminder, container, false)

        return view
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        confirmButton.setOnClickListener {
            val db: SQLiteDatabase = mDatabaseHelper.writableDatabase
            val values: ContentValues = ReminderEntry.getContentValues(
                    titleText.text.toString(), getDateSelection(), Date().time, null)

            val res: Long = db.insert(ReminderEntry.TABLE_NAME, null, values)
            addRowCallback(res)

            dismiss()
        }
    }

    fun getDateSelection(): Long {
        when (radioGroup.checkedRadioButtonId) {
            todayButton.id -> return Date().time
            tomorrowButton.id -> return Date().time + MILLIS_PER_DAY
            else -> return 0
        }
    }
}