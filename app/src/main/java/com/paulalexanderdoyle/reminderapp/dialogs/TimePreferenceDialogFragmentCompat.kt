package com.paulalexanderdoyle.reminderapp.dialogs

import android.content.Context
import android.support.v7.preference.DialogPreference
import android.support.v7.preference.Preference
import android.support.v7.preference.PreferenceDialogFragmentCompat
import android.view.View
import android.widget.TimePicker
import com.paulalexanderdoyle.reminderapp.notification.TimePreference

class TimePreferenceDialogFragmentCompat : PreferenceDialogFragmentCompat(),
        DialogPreference.TargetFragment {
    private val timePicker: TimePicker by lazy {
        TimePicker(context)
    }

    override fun onCreateDialogView(context: Context?): View {
        return timePicker
    }

    override fun onBindDialogView(view: View?) {
        super.onBindDialogView(view)
        timePicker.setIs24HourView(false)
        val pref = preference as TimePreference
        timePicker.currentHour = pref.hour
        timePicker.currentMinute = pref.minute
    }

    override fun onDialogClosed(positiveResult: Boolean) {
        if (positiveResult) {
            val pref = preference as TimePreference
            pref.hour = timePicker.currentHour
            pref.minute = timePicker.currentMinute

            val newTime = TimePreference.getTimeString(pref.hour, pref.minute)
            if (pref.callChangeListener(newTime)) {
                pref.persistStringValue(newTime)
            }
        }
    }

    override fun findPreference(key: CharSequence?): Preference {
        return preference
    }
}