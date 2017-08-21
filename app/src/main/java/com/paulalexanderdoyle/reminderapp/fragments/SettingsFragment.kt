package com.paulalexanderdoyle.reminderapp.fragments

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import com.paulalexanderdoyle.reminderapp.R
import com.paulalexanderdoyle.reminderapp.notification.NotifyAlarmReceiver
import com.paulalexanderdoyle.reminderapp.notification.TimePreference
import java.util.*

class SettingsFragment : PreferenceFragment(), SharedPreferences.OnSharedPreferenceChangeListener {

    private val notificationPrefKey: String by lazy {
        context?.resources?.getString(R.string.notification_pref_key) ?: ""
    }
    private val notifyTimePrefKey: String by lazy {
        context?.resources?.getString(R.string.notify_time_pref_key) ?: ""
    }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        addPreferencesFromResource(R.xml.app_preferences)
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
        if (sharedPreferences == null) return
        when (key) {
            notificationPrefKey -> {
                if (sharedPreferences.getBoolean(notificationPrefKey, false)) {
                    setNotificationTime(sharedPreferences)
                } else {
                    disableNotifications()
                }
            }
            notifyTimePrefKey -> {
                setNotificationTime(sharedPreferences)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        preferenceScreen.sharedPreferences.registerOnSharedPreferenceChangeListener(this)
    }

    override fun onPause() {
        super.onPause()
        preferenceScreen.sharedPreferences.unregisterOnSharedPreferenceChangeListener(this)
    }

    private fun getBroadcastIntent(): PendingIntent {
        val intent = Intent(context, NotifyAlarmReceiver::class.java)
        return PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
    }

    private fun setNotificationTime(sharedPreferences: SharedPreferences) {
        val notifyTime = Calendar.getInstance()
        val notifyTimePref: String = sharedPreferences.getString(notifyTimePrefKey, "00:00")
        notifyTime.set(Calendar.HOUR_OF_DAY, TimePreference.parseHour(notifyTimePref))
        notifyTime.set(Calendar.MINUTE, TimePreference.parseMinute(notifyTimePref))
        notifyTime.set(Calendar.SECOND, 0)
        // Adjust in case time has already passed today
        if (notifyTime.time < Date()) {
            notifyTime.add(Calendar.DAY_OF_MONTH, 1)
        }

        val alarmManager = activity.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.setRepeating(AlarmManager.RTC, notifyTime.timeInMillis, AlarmManager.INTERVAL_DAY, getBroadcastIntent())
    }

    private fun disableNotifications() {
        val alarmManager = activity.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.cancel(getBroadcastIntent())
    }
}