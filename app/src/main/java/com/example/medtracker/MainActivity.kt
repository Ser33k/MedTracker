package com.example.medtracker

import Activity
import History
import HeartRateIndicator
import Settings
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.registerForActivityResult
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.util.jar.Manifest

class MainActivity : AppCompatActivity(R.layout.activity_main) {

    private val requestBLE = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
        if (result.resultCode == RESULT_OK) {
            Log.i("DEBUG", "BLE enabled")
        } else {
            Log.i("DEBUG", "BLE disabled")
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val homeFragment = HeartRateIndicator()
        val activityFragment = Activity()
        val historyFragment = History()
        val settingsFragment = Settings()

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

    fun toggleBLE(view: View) {
        // Use this check to determine whether BLE is supported on the device.
        // If not, we can selectively disable BLE-related features.
        if(!packageManager.hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            Toast.makeText(this, R.string.ble_not_supported, Toast.LENGTH_SHORT).show()
        } else {
            val bluetoothManager = getSystemService(BluetoothManager::class.java)
            val bluetoothAdapter: BluetoothAdapter? = bluetoothManager?.adapter

            // Ensures Bluetooth is available on the device and it is enabled. If not,
            // displays a dialog requesting user permission to enable Bluetooth.
            if (bluetoothAdapter != null && !bluetoothAdapter.isEnabled) {
                requestBLE.launch(Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE))
            }
        }
    }
}
