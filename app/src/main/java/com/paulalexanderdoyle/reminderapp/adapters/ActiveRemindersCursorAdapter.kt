package com.paulalexanderdoyle.reminderapp.adapters

import android.app.Activity
import android.content.Context
import android.database.Cursor
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.CursorAdapter
import android.widget.ImageView
import android.widget.TextView
import com.paulalexanderdoyle.reminderapp.R
import com.paulalexanderdoyle.reminderapp.data.Reminder
import com.paulalexanderdoyle.reminderapp.defaultDateFormat
import com.paulalexanderdoyle.reminderapp.isOverdue
import com.paulalexanderdoyle.reminderapp.isSameDay

class ActiveRemindersCursorAdapter(context: Context, cursor: Cursor?, flags: Int,
                                   val onChecked: ((Reminder) -> Unit)?)
    : CursorAdapter(context, cursor, flags) {
    companion object {
        private const val NO_HEADER: Int = 0
        private const val OVERDUE_HEADER: Int = 1
        private const val DATE_HEADER: Int = 2
    }

    override fun newView(context: Context?, cursor: Cursor?, parent: ViewGroup?): View {
        val inflater: LayoutInflater? = LayoutInflater.from(context)

        return inflater?.inflate(R.layout.active_reminder_item, parent, false)
                ?: throw RuntimeException("Problem with inflation")
    }

    override fun bindView(view: View?, context: Context?, cursor: Cursor?) {
        val textView: TextView? = view?.findViewById<TextView>(R.id.reminder_name)
        val completed: CheckBox? = view?.findViewById<CheckBox>(R.id.checkbox)

        val reminder: Reminder = Reminder(cursor)
        setupHeader(view, context, reminder)

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

    private fun setupHeader(view: View?, context: Context?, reminder: Reminder) {
        val headerView: View? = view?.findViewById(R.id.header_layout)
        var headerType: Int = NO_HEADER
        if (cursor != null) {
            if (cursor.isFirst) {
                headerType = if (isOverdue(reminder.dueDate)) OVERDUE_HEADER else DATE_HEADER
            } else {
                val prev: Reminder = Reminder(getItem(cursor.position - 1) as Cursor)
                if (!isSameDay(prev.dueDate, reminder.dueDate) && !isOverdue(reminder.dueDate)) {
                    headerType = DATE_HEADER
                }
            }
        }

        val dateHeader = headerView?.findViewById<TextView>(R.id.date_header)
        val alertIcon = headerView?.findViewById<ImageView>(R.id.alert_view)
        when (headerType) {
            NO_HEADER -> headerView?.visibility = View.GONE
            OVERDUE_HEADER -> {
                headerView?.visibility = View.VISIBLE
                alertIcon?.visibility = View.VISIBLE
                dateHeader?.text = context?.resources?.getString(R.string.overdue_header)
            }
            DATE_HEADER -> {
                headerView?.visibility = View.VISIBLE
                alertIcon?.visibility = View.GONE
                headerView?.findViewById<TextView>(R.id.date_header)?.text =
                        defaultDateFormat.format(reminder.dueDate)
            }
        }

    }
}