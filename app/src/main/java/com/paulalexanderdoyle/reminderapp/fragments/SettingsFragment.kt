package com.paulalexanderdoyle.reminderapp.fragments

import android.os.Bundle
import android.support.v7.preference.PreferenceFragmentCompat
import com.paulalexanderdoyle.reminderapp.R

class SettingsFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        addPreferencesFromResource(R.xml.app_preferences)
    }
}