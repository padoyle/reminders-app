package com.paulalexanderdoyle.reminderapp.notification

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.ContextWrapper
import android.graphics.Color
import android.media.RingtoneManager
import android.os.Build
import android.support.v4.app.NotificationCompat

class NotificationUtils(base: Context): ContextWrapper(base) {
    init {
        createChannels()
    }
    companion object {
        const val CHANNEL_ID: String = "com.paulalexanderdoyle.reminderapp.MAIN"
        const val CHANNEL_NAME: String = "MAIN CHANNEL"

        private var instance: NotificationUtils? = null

        @Synchronized
        fun getInstance(context: Context): NotificationUtils {
            if (instance == null) {
                instance = NotificationUtils(context)
            }
            return instance!!
        }
    }

    private fun createChannels() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) return
        val channel = NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT)
        channel.enableLights(true)
        channel.enableVibration(true)
        channel.lightColor = Color.GREEN
        channel.lockscreenVisibility = NotificationCompat.VISIBILITY_PUBLIC

        val nm = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        nm.createNotificationChannel(channel)
    }

    fun getNotification(sender: PendingIntent): Notification {
        val alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notificationBuilder = NotificationCompat.Builder(applicationContext, CHANNEL_ID)
                .setContentTitle("We just testin dis shit")
                .setContentText("Mo goodness")
                .setSmallIcon(android.R.drawable.stat_notify_more)
                .setContentIntent(sender)
                .setSound(alarmSound)
                .setAutoCancel(true)
        return notificationBuilder.build()
    }
}