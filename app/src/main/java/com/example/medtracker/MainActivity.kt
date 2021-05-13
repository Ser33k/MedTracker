package com.example.medtracker


import android.content.Intent
import com.example.medtracker.fragment.HistoryFragment
import com.example.medtracker.fragment.HeartRateFragment
import com.example.medtracker.fragment.SettingsFragment
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.example.medtracker.fragment.*
import com.example.medtracker.login.LoginActivity
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity(R.layout.activity_main), PreferenceFragmentCompat.OnPreferenceStartFragmentCallback {

    private companion object {
        private const val TAG = "MainFrag"
    }

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = Firebase.auth

        val homeFragment = HeartRateFragment()
        val activityFragment = ActivityFragment()
        val historyFragment = HistoryFragment()
        val settingsFragment = SettingsFragment()

//        startActivity(Intent(this, LoginActivity::class.java))

        val bottomNavigationView :BottomNavigationView = findViewById(R.id.bottomNavigationView)

        bottomNavigationView.setOnNavigationItemSelectedListener {
            when(it.itemId){
                R.id.bottomNavigationHomeMenuId->setCurrentFragment(homeFragment)
                R.id.bottomNavigationRunningMenuId-> startActivity(Intent(this, TrackActivity::class.java))//setCurrentFragment(activityFragment)
                R.id.bottomNavigationHistoryMenuId->setCurrentFragment(historyFragment)
                R.id.bottomNavigationSettingsMenuId->setCurrentFragment(settingsFragment)

            }
            true
        }

    }

    override fun onStart() {
        super.onStart()
        setCurrentFragment(HeartRateFragment())
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

    fun onLogoutClick(view: View) {
        auth.signOut()
        Log.d(TAG, "logged out")
        startActivity(Intent(this, LoginActivity::class.java))
    }
}
