package com.paulalexanderdoyle.reminderapp.notification

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.util.Log
import com.paulalexanderdoyle.reminderapp.RemindersActivity
import org.jetbrains.anko.defaultSharedPreferences

class NotifyAlarmReceiver : BroadcastReceiver() {
    companion object {
        private const val NOTIFICATION_ID_PREF_KEY: String = "NOTIFICATION_ID_PREF_KEY"
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        if (context == null) return
        val prefs: SharedPreferences = context.defaultSharedPreferences
        val nId: Int = prefs.getInt(NOTIFICATION_ID_PREF_KEY, 0)
        val nm = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val notifyIntent = Intent(context, RemindersActivity::class.java)
        notifyIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP

        Log.i(javaClass.simpleName, "Sending notification $nId")
        val sender = PendingIntent.getActivity(context, 0, notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT)
        nm.notify(nId, NotificationUtils.getInstance(context).getNotification(sender))
        prefs.edit().putInt(NOTIFICATION_ID_PREF_KEY, nId + 1).apply()
    }
}