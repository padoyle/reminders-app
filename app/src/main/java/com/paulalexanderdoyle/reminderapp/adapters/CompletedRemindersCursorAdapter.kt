package com.paulalexanderdoyle.reminderapp.adapters

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

class CompletedRemindersCursorAdapter(context: Context, cursor: Cursor?, flags: Int)
    : CursorAdapter(context, cursor, flags) {

    override fun newView(context: Context?, cursor: Cursor?, parent: ViewGroup?): View {
        val inflater: LayoutInflater? = LayoutInflater.from(context)

        return inflater?.inflate(R.layout.completed_reminder_item, parent, false)
                ?: throw RuntimeException("Problem with inflation")
    }

    override fun bindView(view: View?, context: Context?, cursor: Cursor?) {
        val textView: TextView? = view?.findViewById<TextView>(R.id.reminder_name)
        val completedDate: TextView? = view?.findViewById<TextView>(R.id.completion_date)

        val reminder: Reminder = Reminder(cursor)
        textView?.text = reminder.title
        completedDate?.text = context?.resources?.getString(R.string.completed_on_label,
                defaultDateFormat.format(reminder.completedDate))
        view?.tag = reminder

        view?.setOnLongClickListener(
                fun (selectedView: View): Boolean {
                    (context as? Activity)?.openContextMenu(selectedView)
                    return true
                }
        )
    }
}