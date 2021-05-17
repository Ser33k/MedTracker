package com.example.medtracker.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.example.medtracker.MainActivity
import com.example.medtracker.R
import com.example.medtracker.login.LoginActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class SettingsFragment:PreferenceFragmentCompat() {

    private lateinit var auth: FirebaseAuth


    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.settings, rootKey)

        val result = bundleOf("name" to "Tomek")
        auth = Firebase.auth

        // Insert result in a bundle
        setFragmentResult("ACCOUNT", result)
    }


    override fun onPreferenceTreeClick(preference: Preference): Boolean {
        return when (preference.key) {
            getString(R.string.logout) -> {
                auth.signOut()
                Log.d("SETT", "logged out")
                val intent = Intent (activity, LoginActivity::class.java)
                startActivity(intent)
                true
            }
            else -> {
                super.onPreferenceTreeClick(preferenceScreen)
            }
        }
    }
}
