package com.paulalexanderdoyle.reminderapp

import android.app.Activity
import android.database.Cursor
import android.database.sqlite.SQLiteCursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.*
import android.widget.AdapterView
import com.paulalexanderdoyle.reminderapp.database.ReminderDbHelper
import com.paulalexanderdoyle.reminderapp.database.ReminderEntry
import com.paulalexanderdoyle.reminderapp.database.RemindersCursorAdapter
import kotlinx.android.synthetic.main.fragment_upcoming_reminders.*

class UpcomingRemindersFragment : Fragment() {

    private val mDatabaseHelper: SQLiteOpenHelper by lazy {
        ReminderDbHelper(context)
    }
    private val mCursorAdapter: RemindersCursorAdapter by lazy {
        RemindersCursorAdapter(context, cursor = getCursor(), flags = 0)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_upcoming_reminders, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        registerForContextMenu(reminders_list)
        reminders_list.adapter = mCursorAdapter
    }

    override fun onCreateContextMenu(menu: ContextMenu?, v: View?, menuInfo: ContextMenu.ContextMenuInfo?) {
        val menuInflater: MenuInflater? = (context as? Activity)?.menuInflater
        menuInflater?.inflate(R.menu.menu_long_press, menu)
    }

    override fun onContextItemSelected(item: MenuItem?): Boolean {
        if (item?.itemId == R.id.action_delete) {
            val itemPosition: Int? =
                    (item.menuInfo as? AdapterView.AdapterContextMenuInfo)?.position
            if (itemPosition != null) {
                val cursor: SQLiteCursor? = reminders_list.getItemAtPosition(itemPosition) as? SQLiteCursor
                deleteItem(cursor?.getInt(cursor.getColumnIndex(ReminderEntry._ID)))
            }
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun getCursor(): Cursor {
        val db: SQLiteDatabase = mDatabaseHelper.readableDatabase

        val cursor: Cursor = db.query(ReminderEntry.TABLE_NAME, null, null, null, null, null,
                "${ReminderEntry.COLUMN_NAME_DUE_DATE} DESC")

        return cursor
    }

    private fun deleteItem(id: Int?) {
        val db: SQLiteDatabase = mDatabaseHelper.writableDatabase
        if (id != null) {
            db.delete(ReminderEntry.TABLE_NAME, "${ReminderEntry._ID}=$id", null)
        } else {
            Log.w("PAULDEBUG", "Can't delete null id")
        }
    }
}