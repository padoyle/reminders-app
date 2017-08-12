package com.paulalexanderdoyle.reminderapp.database

import android.app.Activity
import android.content.Context
import android.database.Cursor
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.CursorAdapter
import android.widget.TextView
import com.paulalexanderdoyle.reminderapp.R

class RemindersCursorAdapter(context: Context, cursor: Cursor, flags: Int)
    : CursorAdapter(context, cursor, flags) {

    override fun newView(context: Context?, cursor: Cursor?, parent: ViewGroup?): View {
        val inflater: LayoutInflater? = LayoutInflater.from(context)

        return inflater?.inflate(R.layout.reminder_list_item, parent, false)
                ?: throw RuntimeException("Problem with inflation")
    }

    override fun bindView(view: View?, context: Context?, cursor: Cursor?) {
        val textView: TextView? = view?.findViewById<TextView>(R.id.reminder_name)
        val completed: CheckBox? = view?.findViewById<CheckBox>(R.id.checkbox)

        textView?.text = cursor?.getString(cursor.getColumnIndex(ReminderEntry.COLUMN_NAME_TITLE))
                ?: "<Unnamed>"
        completed?.isChecked = cursor?.getLong(cursor.getColumnIndex(
                ReminderEntry.COLUMN_NAME_COMPLETION_DATE)) != null
        view?.tag = cursor?.getString(cursor.getColumnIndex(ReminderEntry._ID))

        view?.setOnLongClickListener(
                fun (selectedView: View): Boolean {
                    Log.i("PAULDEBUG", "here's a looooong muthafuckin click")
                    (context as? Activity)?.openContextMenu(selectedView)
                    return true
                }
        )
    }
}