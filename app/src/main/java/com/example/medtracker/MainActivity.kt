package com.example.medtracker


import com.example.medtracker.fragment.HistoryFragment
import com.example.medtracker.fragment.HeartRateFragment
import com.example.medtracker.fragment.SettingsFragment
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.example.medtracker.fragment.ActivityFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity(R.layout.activity_main), PreferenceFragmentCompat.OnPreferenceStartFragmentCallback {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val homeFragment = HeartRateFragment()
        val activityFragment = ActivityFragment()
        val historyFragment = HistoryFragment()
        val settingsFragment = SettingsFragment()

        setCurrentFragment(homeFragment)

        val bottomNavigationView :BottomNavigationView = findViewById(R.id.bottomNavigationView)

        bottomNavigationView.setOnNavigationItemSelectedListener {
            when(it.itemId){
                R.id.bottomNavigationHomeMenuId->setCurrentFragment(homeFragment)
                R.id.bottomNavigationRunningMenuId->setCurrentFragment(activityFragment)
                R.id.bottomNavigationHistoryMenuId->setCurrentFragment(historyFragment)
                R.id.bottomNavigationSettingsMenuId->setCurrentFragment(settingsFragment)

            }
            true
        }

    }



    private fun setCurrentFragment(fragment: Fragment) =
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.flFragment, fragment)
            commit()
        }

    override fun onPreferenceStartFragment(caller: PreferenceFragmentCompat?, pref: Preference): Boolean {
        // Instantiate the new Fragment
        val args: Bundle = pref.extras
        val fragment = supportFragmentManager.fragmentFactory.instantiate(
            classLoader,
            pref.fragment)
        fragment.arguments = args

//        supportFragmentManager.setFragmentResultListener("ACCOUNT", this, { requestKey, result ->
//            if (requestKey == "ACCOUNT") {
//                // Get result from bundle
//                Log.d("MainActivity", result["name"] as String)
//                Toast.makeText(this,result["name"] as String, Toast.LENGTH_SHORT).show()
//            }
//        })

        // Replace the existing Fragment with the new Fragment
        supportFragmentManager.beginTransaction()
            .replace(R.id.flFragment, fragment)
            .addToBackStack(null)
            .commit()
        return true
    }
}
