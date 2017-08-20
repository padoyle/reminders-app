package com.paulalexanderdoyle.reminderapp.fragments

import android.os.Bundle
import android.support.v7.preference.Preference
import android.support.v7.preference.PreferenceFragmentCompat
import com.paulalexanderdoyle.reminderapp.notification.TimePreference
import com.paulalexanderdoyle.reminderapp.dialogs.TimePreferenceDialogFragmentCompat

abstract class PreferenceFragment: PreferenceFragmentCompat() {

    override fun onDisplayPreferenceDialog(preference: Preference?) {
        if (preference is TimePreference) {
            val dialogFragment = TimePreferenceDialogFragmentCompat()
            val bundle = Bundle(1)
            bundle.putString("key", preference.key)

            dialogFragment.arguments = bundle
            dialogFragment.setTargetFragment(this, 0)
            dialogFragment.show(fragmentManager, "android.support.v7.preference.PreferenceFragment.DIALOG")
        } else {
            super.onDisplayPreferenceDialog(preference)
        }
    }
}