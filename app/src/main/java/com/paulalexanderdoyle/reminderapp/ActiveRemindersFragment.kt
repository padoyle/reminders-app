package com.paulalexanderdoyle.reminderapp

import android.app.Activity
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
import android.view.*
import android.widget.AdapterView
import com.paulalexanderdoyle.reminderapp.adapters.ActiveRemindersCursorAdapter
import com.paulalexanderdoyle.reminderapp.data.Reminder
import com.paulalexanderdoyle.reminderapp.data.ReminderTable
import com.paulalexanderdoyle.reminderapp.database.ReminderDbHelper
import kotlinx.android.synthetic.main.fragment_active_reminders.*
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async
import org.jetbrains.anko.coroutines.experimental.asReference
import org.jetbrains.anko.coroutines.experimental.bg
import java.util.*

class ActiveRemindersFragment : Fragment(), LoaderManager.LoaderCallbacks<Cursor> {

    class ReminderCursorLoader(val dbHelper: SQLiteOpenHelper, context: Context) : CursorLoader(context) {
        override fun loadInBackground(): Cursor {
            val db: SQLiteDatabase = dbHelper.readableDatabase

            val cursor: Cursor = db.query(ReminderTable.TABLE_NAME, null,
                    ReminderTable.COL_COMPLETION_DATE + " IS NULL", null, null, null,
                    "${ReminderTable.COL_DUE_DATE} ASC")
            return cursor
        }
    }

    private val databaseHelper: ReminderDbHelper by lazy {
        ReminderDbHelper.getInstance(context)
    }
    private val cursorAdapter: ActiveRemindersCursorAdapter by lazy {
        ActiveRemindersCursorAdapter(context, null, 0, { reminder ->
            val ref = this.asReference()
            async(UI) {
                databaseHelper.setCompletion(reminder.id, Date())
                ref().updateAdapter()
            }
        })
    }
    private var loader: ReminderCursorLoader? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_active_reminders, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        registerForContextMenu(reminders_list)

        loaderManager.initLoader(0x01, null, this)
        reminders_list.adapter = cursorAdapter
        fab.setOnClickListener { _ ->
            val editReminderDialog: EditReminderDialog = EditReminderDialog()
            editReminderDialog.init({
                updateAdapter()
            }, null)
            editReminderDialog.show(fragmentManager, "EditReminderDialog")
        }
    }

    override fun onCreateContextMenu(menu: ContextMenu?, v: View?, menuInfo: ContextMenu.ContextMenuInfo?) {
        val menuInflater: MenuInflater? = (context as? Activity)?.menuInflater
        menuInflater?.inflate(R.menu.context_menu_active, menu)
    }

    override fun onContextItemSelected(item: MenuItem?): Boolean {
        val itemPosition: Int? = (item?.menuInfo as? AdapterView.AdapterContextMenuInfo)?.position
        val cursor: SQLiteCursor? = reminders_list.getItemAtPosition(itemPosition ?: -1) as? SQLiteCursor
        if (cursor == null || cursor.count == 0 || cursor.isAfterLast || cursor.isBeforeFirst) {
            return false
        }
        val reminderId: Long? = cursor?.getLong(cursor.getColumnIndex(ReminderTable._ID))
        if (item?.itemId == R.id.action_delete) {
            val ref = this.asReference()
            if (reminderId != null) {
                async(UI) {
                    bg { databaseHelper.deleteItem(reminderId) }.await()
                    ref().updateAdapter()
                }
            }
            return true
        } else if (item?.itemId == R.id.action_edit) {
            val editReminderDialog = EditReminderDialog()
            editReminderDialog.init({ updateAdapter() }, Reminder(cursor))
            editReminderDialog.show(fragmentManager, "EditReminderDialog")
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

}