package com.paulalexanderdoyle.reminderapp.notification

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.support.v4.app.JobIntentService
import android.util.Log
import com.paulalexanderdoyle.reminderapp.RemindersActivity

class NotifyAlarmReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        Log.w(javaClass.simpleName + "PAUL", "Time for alarms my dude")
        if (context == null) return

        Log.w(javaClass.simpleName + "PAUL", "getting ready to go")
        val notifyIntent = Intent(context, RemindersActivity::class.java)
        notifyIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        JobIntentService.enqueueWork(context, NotifyService::class.java, 0, notifyIntent)
    }
}