package com.paulalexanderdoyle.reminderapp

import android.app.DatePickerDialog
import android.content.ContentValues
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import android.widget.TextView
import com.paulalexanderdoyle.reminderapp.data.Reminder
import com.paulalexanderdoyle.reminderapp.database.ReminderDbHelper
import com.paulalexanderdoyle.reminderapp.data.ReminderEntry
import kotlinx.android.synthetic.main.dialog_edit_reminder.*
import java.util.*

class EditReminderDialog : DialogFragment() {

    private val mDatabaseHelper: SQLiteOpenHelper by lazy {
        ReminderDbHelper(context)
    }
    private val dateSelectedListener: DatePickerDialog.OnDateSetListener =
            object: DatePickerDialog.OnDateSetListener {
                override fun onDateSet(view: DatePicker?, year: Int, month: Int, day: Int) {
                    selectedDate.set(Calendar.YEAR, year)
                    selectedDate.set(Calendar.MONTH, month)
                    selectedDate.set(Calendar.DAY_OF_MONTH, day)
                    updateDateLabel()
                }
            }

    private var selectedDate = Calendar.getInstance()

    // Initializable parameters
    private var modifiedDbCallback: (Long) -> Unit = {}
    private var existing: Reminder? = null

    fun init(modifiedDbCallback: (Long) -> Unit, existing: Reminder?) {
        this.modifiedDbCallback = modifiedDbCallback
        this.existing = existing
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view: View? = inflater?.inflate(R.layout.dialog_edit_reminder, container, false)

        return view
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        populateFromExisting()

        dateField.setOnClickListener {
            DatePickerDialog(context, dateSelectedListener, selectedDate.get(Calendar.YEAR),
                    selectedDate.get(Calendar.MONTH), selectedDate.get(Calendar.DAY_OF_MONTH)).show()
        }

        cancelButton.setOnClickListener {
            dismiss()
        }
        confirmButton.setOnClickListener {
            val db: SQLiteDatabase = mDatabaseHelper.writableDatabase
            val values: ContentValues = ReminderEntry.getContentValues(
                    titleText.text.toString(), selectedDate.time.time, Date().time, null)

            if (existing != null) {
                db.update(ReminderEntry.TABLE_NAME, values,
                        ReminderEntry._ID + "=" + existing?.id, null)
                modifiedDbCallback(existing?.id ?: 0)
            } else {
                val res: Long = db.insert(ReminderEntry.TABLE_NAME, null, values)
                modifiedDbCallback(res)
            }

            dismiss()
        }
    }

    fun populateFromExisting() {
        if (existing != null) {
            titleText.setText(existing?.title, TextView.BufferType.EDITABLE)
            selectedDate.time = existing?.dueDate
            updateDateLabel()
        }
    }

    fun updateDateLabel() {
        dateField.setText(defaultDateFormat.format(selectedDate.time))
    }
}