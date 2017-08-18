package com.paulalexanderdoyle.reminderapp

import android.app.Activity
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteCursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.LoaderManager
import android.support.v4.content.CursorLoader
import android.support.v4.content.Loader
import android.util.Log
import android.view.*
import android.widget.AdapterView
import com.paulalexanderdoyle.reminderapp.data.Reminder
import com.paulalexanderdoyle.reminderapp.adapters.CompletedRemindersCursorAdapter
import com.paulalexanderdoyle.reminderapp.database.ReminderDbHelper
import com.paulalexanderdoyle.reminderapp.data.ReminderEntry
import kotlinx.android.synthetic.main.fragment_completed_reminders.*

class CompletedRemindersFragment : Fragment(), LoaderManager.LoaderCallbacks<Cursor> {

    class ReminderCursorLoader(val dbHelper: SQLiteOpenHelper, context: Context) : CursorLoader(context) {
        override fun loadInBackground(): Cursor {
            val db: SQLiteDatabase = dbHelper.readableDatabase

            val cursor: Cursor = db.query(ReminderEntry.TABLE_NAME, null,
                    ReminderEntry.COLUMN_NAME_COMPLETION_DATE + " IS NOT NULL", null, null, null,
                    "${ReminderEntry.COLUMN_NAME_DUE_DATE} ASC")
            return cursor
        }
    }

    private val databaseHelper: SQLiteOpenHelper by lazy {
        ReminderDbHelper(context)
    }
    private val cursorAdapter: CompletedRemindersCursorAdapter by lazy {
        CompletedRemindersCursorAdapter(context, null, flags = 0)
    }
    private var loader: ReminderCursorLoader? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_completed_reminders, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        registerForContextMenu(reminders_list)

        loaderManager.initLoader(0x01, null, this)
        reminders_list.adapter = cursorAdapter
    }

    override fun onCreateContextMenu(menu: ContextMenu?, v: View?, menuInfo: ContextMenu.ContextMenuInfo?) {
        val menuInflater: MenuInflater? = (context as? Activity)?.menuInflater
        menuInflater?.inflate(R.menu.context_menu_completed, menu)
    }

    override fun onContextItemSelected(item: MenuItem?): Boolean {
        val itemPosition: Int? = (item?.menuInfo as? AdapterView.AdapterContextMenuInfo)?.position
        val cursor: SQLiteCursor? = reminders_list.getItemAtPosition(itemPosition ?: -1) as? SQLiteCursor
        if (itemPosition != null) {
            if (item?.itemId == R.id.action_delete) {
                deleteItem(cursor?.getInt(cursor.getColumnIndex(ReminderEntry._ID)))
                return true
            } else if (item?.itemId == R.id.action_mark_incomplete && cursor != null) {
                markIncomplete(cursor)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateLoader(id: Int, args: Bundle?): Loader<Cursor> {
        loader = ReminderCursorLoader(databaseHelper, context)
        return loader as Loader<Cursor>
    }

    override fun onLoadFinished(loader: Loader<Cursor>?, data: Cursor?) {
        cursorAdapter.swapCursor(data)
    }

    override fun onLoaderReset(loader: Loader<Cursor>?) {
        cursorAdapter.swapCursor(null)
    }

    override fun setMenuVisibility(menuVisible: Boolean) {
        super.setMenuVisibility(menuVisible)
        if (menuVisible) {
            updateAdapter()
        }
    }

    fun updateAdapter() {
        loader?.onContentChanged()
    }

    // TODO: Oh god please kill this horrible code duplication
    private fun deleteItem(id: Int?) {
        // TODO: Threadsafe way to do this in background
        val db: SQLiteDatabase = databaseHelper.writableDatabase
        if (id != null) {
            db.delete(ReminderEntry.TABLE_NAME, "${ReminderEntry._ID}=$id", null)
        } else {
            Log.w(javaClass.simpleName, "Can't delete null id")
        }
        updateAdapter()
    }

    private fun markIncomplete(cursor: Cursor) {
        val db: SQLiteDatabase = databaseHelper.writableDatabase
        val reminder: Reminder = Reminder(cursor)
        reminder.completedDate = null
        val values: ContentValues = ReminderEntry.getContentValues(reminder)
        db.update(ReminderEntry.TABLE_NAME, values,
                ReminderEntry._ID + "=" + reminder.id, null)
        updateAdapter()
    }
}