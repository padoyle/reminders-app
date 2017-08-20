package com.paulalexanderdoyle.reminderapp.notification

import android.content.Context
import android.content.res.TypedArray
import android.support.v7.preference.DialogPreference
import android.util.AttributeSet
import com.paulalexanderdoyle.reminderapp.R

class TimePreference : DialogPreference {
    var hour: Int = 0
    var minute: Int = 0

    @Suppress("unused")
    constructor(context: Context) : super(context)
    @Suppress("unused")
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    companion object {
        fun parseHour(time: String): Int {
            try {
                val pieces = time.split(":")
                return pieces[0].toInt()
            } catch (ex: Exception) {
                return 0
            }
        }

        fun parseMinute(time: String): Int {
            try {
                val pieces = time.split(":")
                return pieces[1].toInt()
            } catch (ex: Exception) {
                return 0
            }
        }

        fun getTimeString(h: Int, m: Int): String {
            return String.format("%02d:%02d", h, m)
        }

        private fun formatTimeForDisplay(h: Int, m: Int): String {
            val suffix = if (h / 12 > 0) "PM" else "AM"
            return String.format("%d:%02d %s", if (h % 12 == 0) 12 else h % 12, m, suffix)
        }
    }

    override fun onGetDefaultValue(a: TypedArray?, index: Int): Any {
        return a!!.getString(index)
    }

    override fun onSetInitialValue(restorePersistedValue: Boolean, defaultValue: Any?) {
        val timeString: String
        if (restorePersistedValue) {
            timeString = getPersistedString(defaultValue?.toString() ?: "00:00")
        } else {
            timeString = defaultValue.toString()
        }

        hour = parseHour(timeString)
        minute = parseMinute(timeString)

        if (restorePersistedValue) {
            summary = context.resources.getString(R.string.notification_time_description,
                    formatTimeForDisplay(hour, minute))
        }
    }

    fun persistStringValue(value: String) {
        persistString(value)
        summary = context.resources.getString(R.string.notification_time_description,
                formatTimeForDisplay(hour, minute))
    }
}