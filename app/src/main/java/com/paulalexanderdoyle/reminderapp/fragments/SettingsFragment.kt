package com.paulalexanderdoyle.reminderapp.fragments

import android.os.Bundle
import com.paulalexanderdoyle.reminderapp.R

class SettingsFragment : PreferenceFragment() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        addPreferencesFromResource(R.xml.app_preferences)
    }
}