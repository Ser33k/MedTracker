package com.example.medtracker.fragment

import android.os.Bundle
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import androidx.preference.PreferenceFragmentCompat
import com.example.medtracker.R

class AccountFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.account, rootKey)

        val result = bundleOf("name" to "Tomek")

        // Insert result in a bundle
        setFragmentResult("ACCOUNT", result)
    }


}
