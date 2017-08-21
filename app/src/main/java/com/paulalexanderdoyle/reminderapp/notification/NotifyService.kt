package com.paulalexanderdoyle.reminderapp.notification

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.support.v4.app.JobIntentService
import android.util.Log
import com.paulalexanderdoyle.reminderapp.R
import com.paulalexanderdoyle.reminderapp.database.ReminderDbHelper
import com.paulalexanderdoyle.reminderapp.isOverdue
import org.jetbrains.anko.defaultSharedPreferences
import java.util.*

class NotifyService : JobIntentService() {
    companion object {
        private const val NOTIFICATION_ID_PREF_KEY: String = "NOTIFICATION_ID_PREF_KEY"
    }

    override fun onHandleWork(intent: Intent) {
        Log.w(javaClass.simpleName + "PAUL", "Doing the REAL work")
        val prefs: SharedPreferences = defaultSharedPreferences
        val nId: Int = prefs.getInt(NOTIFICATION_ID_PREF_KEY, 0)
        val nm = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        Log.i(javaClass.simpleName + "PAUL", "Sending notification $nId")
        val sender = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)

        val remInfo = getReminderInfo(this)
        if (remInfo != null) {
            nm.notify(nId, NotificationUtils.getInstance(this).getNotification(sender,
                    remInfo.first, remInfo.second))
            prefs.edit().putInt(NOTIFICATION_ID_PREF_KEY, nId + 1).apply()
        }
    }

    private fun getReminderInfo(context: Context): Pair<String, String>? {
        val prefs: SharedPreferences = context.defaultSharedPreferences
        val cutoffDate: Calendar = Calendar.getInstance()
        cutoffDate.time = Date()
        cutoffDate.add(Calendar.DAY_OF_YEAR, (prefs.getString(context.getString(
                R.string.notify_upcoming_pref_key), "0").toIntOrNull() ?: 0))
        val reminders = ReminderDbHelper.getInstance(context).getUpcoming(cutoffDate.timeInMillis)
        if (reminders.isNotEmpty()) {
            val title = when {
                isOverdue(reminders[0].dueDate) -> context.getString(R.string.notification_title_overdue)
                else -> context.getString(R.string.notification_title_upcoming, reminders.size)
            }
            val description = reminders[0].title
            return Pair(title, description)
        }
        return null
    }
}