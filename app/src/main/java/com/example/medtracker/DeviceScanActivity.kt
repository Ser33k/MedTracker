package com.example.medtracker

import android.Manifest
import android.app.AlertDialog
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.bluetooth.le.BluetoothLeScanner
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanResult
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast

import android.content.ContentValues.TAG
import android.os.Looper
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.medtracker.ble.LeDeviceListAdapter
import kotlinx.android.synthetic.main.activity_device_scan.*


/**
 * Activity for scanning and displaying available Bluetooth LE devices.
 */
class DeviceScanActivity : AppCompatActivity() {
    private var mLeDeviceListAdapter: LeDeviceListAdapter? = null
    private var mBluetoothAdapter: BluetoothAdapter? = null
    private var mScanning: Boolean = false
    private var mBluetoothLeScanner: BluetoothLeScanner? = null
    private var mHandler: Handler? = null

    private val requestBluetooth = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
        if (result.resultCode == RESULT_CANCELED) {
            // User chose not to enable Bluetooth.
            Log.i(TAG, "BLE disabled")
            finish()
        } else {
            Log.i(TAG, "BLE enabled")
        }
    }

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_device_scan)
        setSupportActionBar(findViewById(R.id.my_toolbar))
        supportActionBar!!.setTitle(R.string.title_devices)

        val rvDevices = findViewById<View>(R.id.rvDevices) as RecyclerView
        mLeDeviceListAdapter = LeDeviceListAdapter()

        rvDevices.adapter = mLeDeviceListAdapter
        rvDevices.layoutManager = LinearLayoutManager(this)

        rvDevices.addOnItemClickListener(object: OnItemClickListener {
            override fun onItemClicked(position: Int, view: View) {
                val device = mLeDeviceListAdapter!!.getDevice(position) ?: return
                val intent = Intent(this@DeviceScanActivity, DeviceControlActivity::class.java)
                intent.putExtra(DeviceControlActivity.EXTRAS_DEVICE_NAME, device.name)
                intent.putExtra(DeviceControlActivity.EXTRAS_DEVICE_ADDRESS, device.address)
                if (mScanning) {
                    mBluetoothLeScanner!!.stopScan(mLeScanCallback)
                    mScanning = false
                }
                startActivity(intent)
            }
        })


        mHandler = Handler(Looper.getMainLooper())


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // Android M Permission check
            if (this.checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                val builder = AlertDialog.Builder(this)
                builder.setTitle("This app needs location access")
                builder.setMessage("Please grant location access so this app can detect beacons.")
                builder.setPositiveButton(android.R.string.ok, null)
                builder.setOnDismissListener {
                    requestPermissions(arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION),
                        PERMISSION_REQUEST_COARSE_LOCATION
                    )
                }
                builder.show()
            }
        }


        // Use this check to determine whether BLE is supported on the device.  Then you can
        // selectively disable BLE-related features.
        if (!packageManager.hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            Toast.makeText(this, R.string.ble_not_supported, Toast.LENGTH_SHORT).show()
            finish()
        }

        // Initializes a Bluetooth adapter.  For API level 18 and above, get a reference to
        // BluetoothAdapter through BluetoothManager.
        val bluetoothManager = getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        mBluetoothAdapter = bluetoothManager.adapter

        // Checks if Bluetooth is supported on the device.
        if (mBluetoothAdapter == null) {
            Toast.makeText(this, R.string.error_bluetooth_not_supported, Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        // Get ble scanner
        mBluetoothLeScanner = mBluetoothAdapter!!.bluetoothLeScanner
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_ble, menu)
        if (!mScanning) {
            menu.findItem(R.id.menu_stop).isVisible = false
            menu.findItem(R.id.menu_scan).isVisible = true
            menu.findItem(R.id.menu_refresh).actionView = null
        } else {
            menu.findItem(R.id.menu_stop).isVisible = true
            menu.findItem(R.id.menu_scan).isVisible = false
            menu.findItem(R.id.menu_refresh).setActionView(
                R.layout.actionbar_indeterminate_progress)
        }
        return true
    }


    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>,
                                            grantResults: IntArray) {
        when (requestCode) {
            PERMISSION_REQUEST_COARSE_LOCATION -> {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.d(TAG, "coarse location permission granted")
                } else {
                    val builder = AlertDialog.Builder(this)
                    builder.setTitle("Functionality limited")
                    builder.setMessage("Since location access has not been granted, this app will not be able to discover beacons when in the background.")
                    builder.setPositiveButton(android.R.string.ok, null)
                    builder.setOnDismissListener { }
                    builder.show()
                }
                return
            }
        }
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_scan -> {
                mLeDeviceListAdapter!!.clear()
                scanLeDevice(true)
            }
            R.id.menu_stop -> scanLeDevice(false)
            R.id.menu_skip -> {
                startActivity(Intent(this, MainActivity::class.java))
            }
        }
        return true
    }

    override fun onResume() {
        super.onResume()

        // Ensures Bluetooth is enabled on the device.  If Bluetooth is not currently enabled,
        // fire an intent to display a dialog asking the user to grant permission to enable it.
        if (!mBluetoothAdapter!!.isEnabled) {
            if (!mBluetoothAdapter!!.isEnabled) {
                val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
                requestBluetooth.launch(enableBtIntent)
            }
        }

        // Initializes list view adapter.
        mLeDeviceListAdapter = LeDeviceListAdapter()
        rvDevices.adapter = mLeDeviceListAdapter
        scanLeDevice(true)
    }

    override fun onPause() {
        super.onPause()
        scanLeDevice(false)
        mLeDeviceListAdapter!!.clear()
    }

    private fun scanLeDevice(enable: Boolean) {
        if (enable) {
            // Stops scanning after a pre-defined scan period.
            mHandler!!.postDelayed({
                mScanning = false
                mBluetoothLeScanner?.stopScan(mLeScanCallback)
                invalidateOptionsMenu()
            }, SCAN_PERIOD)

            mScanning = true
            mBluetoothLeScanner?.startScan(mLeScanCallback)
        } else {
            mScanning = false
            mBluetoothLeScanner?.stopScan(mLeScanCallback)
        }
        invalidateOptionsMenu()
    }


    private val mLeScanCallback = object: ScanCallback() {
        override fun onScanResult(callbackType: Int, result: ScanResult?) {
            super.onScanResult(callbackType, result)
            runOnUiThread {
                mLeDeviceListAdapter!!.addDevice(result!!.device)
                mLeDeviceListAdapter!!.notifyDataSetChanged()
            }
        }

        override fun onScanFailed(errorCode: Int) {
            super.onScanFailed(errorCode)
            Log.d(TAG, "Scan failed $errorCode")
        }

    }

    companion object {
        // Stops scanning after 10 seconds.
        private const val SCAN_PERIOD: Long = 10000
        private const val PERMISSION_REQUEST_COARSE_LOCATION = 1
    }
}

interface OnItemClickListener {
    fun onItemClicked(position: Int, view: View)
}

private fun RecyclerView.addOnItemClickListener(onClickListener: OnItemClickListener) {
    this.addOnChildAttachStateChangeListener(object: RecyclerView.OnChildAttachStateChangeListener {
        override fun onChildViewAttachedToWindow(view: View) {
            view.setOnClickListener {
                val holder = getChildViewHolder(view)
                onClickListener.onItemClicked(holder.adapterPosition, view)
            }
        }

        override fun onChildViewDetachedFromWindow(view: View) {
            view.setOnClickListener(null)
        }
    })
}
