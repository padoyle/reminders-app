package com.paulalexanderdoyle.reminderapp.database

import android.app.Activity
import android.content.Context
import android.database.Cursor
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.CursorAdapter
import android.widget.TextView
import com.paulalexanderdoyle.reminderapp.R
import com.paulalexanderdoyle.reminderapp.data.Reminder
import com.paulalexanderdoyle.reminderapp.defaultDateFormat
import com.paulalexanderdoyle.reminderapp.isSameDay

class ActiveRemindersCursorAdapter(context: Context, cursor: Cursor?, flags: Int,
                                   val onChecked: ((Reminder) -> Unit)?)
    : CursorAdapter(context, cursor, flags) {

    override fun newView(context: Context?, cursor: Cursor?, parent: ViewGroup?): View {
        val inflater: LayoutInflater? = LayoutInflater.from(context)

        return inflater?.inflate(R.layout.active_reminder_item, parent, false)
                ?: throw RuntimeException("Problem with inflation")
    }

    override fun bindView(view: View?, context: Context?, cursor: Cursor?) {
        val dateHeader: View? = view?.findViewById(R.id.header_layout)
        val textView: TextView? = view?.findViewById<TextView>(R.id.reminder_name)
        val completed: CheckBox? = view?.findViewById<CheckBox>(R.id.checkbox)

        val reminder: Reminder = Reminder(cursor)
        var showDate: Boolean = false
        if (cursor != null) {
            if (cursor.isFirst) {
                showDate = true
            } else {
                val prev: Reminder = Reminder(getItem(cursor.position - 1) as Cursor)
                if (!isSameDay(prev.dueDate, reminder.dueDate)) {
                    showDate = true
                }
            }
        }

        if (showDate) {
            dateHeader?.findViewById<TextView>(R.id.date_header)?.text =
                    defaultDateFormat.format(reminder.dueDate)
            dateHeader?.visibility = View.VISIBLE
        } else {
            dateHeader?.visibility = View.GONE
        }
        textView?.text = reminder.title
        completed?.isChecked = reminder.completedDate != null
        completed?.setOnClickListener {
            onChecked?.invoke(reminder)
        }
        view?.tag = reminder

        view?.setOnLongClickListener(
                fun (selectedView: View): Boolean {
                    (context as? Activity)?.openContextMenu(selectedView)
                    return true
                }
        )
    }
}