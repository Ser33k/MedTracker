package com.example.medtracker.fragment

import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import com.example.medtracker.R

class SettingsFragment:PreferenceFragmentCompat() {
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.settings, rootKey)
    }
}
