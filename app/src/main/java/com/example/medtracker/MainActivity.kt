package com.example.medtracker

import Activity
import History
import Home
import Settings
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val homeFragment = Home()
        val activityFragment = Activity()
        val historyFragment = History()
        val settingsFragment = Settings()

        setCurrentFragment(homeFragment)

        val bottomNavigationView :BottomNavigationView = findViewById(R.id.bottomNavigationView);

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
}