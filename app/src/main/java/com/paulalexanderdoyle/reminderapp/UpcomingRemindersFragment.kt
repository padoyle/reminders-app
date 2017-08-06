package com.paulalexanderdoyle.reminderapp

import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.widget.CursorAdapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.paulalexanderdoyle.reminderapp.data.Reminder
import com.paulalexanderdoyle.reminderapp.database.ReminderDbHelper
import com.paulalexanderdoyle.reminderapp.database.ReminderEntry
import com.paulalexanderdoyle.reminderapp.database.RemindersCursorAdapter
import kotlinx.android.synthetic.main.fragment_upcoming_reminders.*

/**
 * A placeholder fragment containing a simple view.
 */
class UpcomingRemindersFragment : Fragment() {

//    val listAdapter: ArrayAdapter<String> by lazy {
//        ArrayAdapter<String>(context, R.layout.reminder_list_item, R.id.reminder_name)
//    }
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
        reminders_list.adapter = mCursorAdapter
    }

    fun getCursor(): Cursor {
        val db: SQLiteDatabase = mDatabaseHelper.readableDatabase

        val cursor: Cursor = db.query(ReminderEntry.TABLE_NAME, null, null, null, null, null,
                "${ReminderEntry.COLUMN_NAME_DUE_DATE} DESC")

        return cursor
    }
}

//fun runRandomBS() {
//    var Dave = Person("Dave Davidson",18,"Some college")
//    var Steve = Person("Steve Stevenson",10,null, "steve@steve.com")
//
//    var test = Dave.canVote()
//    var test2 = Steve.isTeenager()
//
//    val doubleString: (String)->String = {str -> str+str }
//    val stringList: List<String> = List(3, {n -> n.toString()})
//
//    doActionToStrings(stringList, doubleString)
//}
//
//fun doActionToStrings(stringList:List<String>, f:(String)->String) {
//    for (str:String in stringList) {
//        print(f(str))
//    }
//}
//
//open class Person(var name: String, var age: Int, var college: String?) {
//
//    var email: String = ""
//
//    constructor(name:String, age:Int, college:String?, email:String) : this(name, age, college) {
//        this.email = email
//    }
//
//    open fun canVote(): Boolean {
//        return age >= 18
//    }
//
//    fun isOctogenarian(): Boolean = age in 80..89
//}
//
//fun Person.isTeenager(): Boolean {
//    return age in 13..19
//}
//
//class Employee(name: String, age: Int, college: String?, var company: String) : Person(name, age, college) {
//    override fun canVote(): Boolean {
//        return true
//    }
//}