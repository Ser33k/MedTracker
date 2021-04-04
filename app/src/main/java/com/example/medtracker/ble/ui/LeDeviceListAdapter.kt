package com.example.medtracker.ble.ui

import android.bluetooth.BluetoothDevice
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.medtracker.R

class LeDeviceListAdapter : RecyclerView.Adapter<LeDeviceListAdapter.ViewHolder>() {

    // Provide a direct reference to each of the views within a data item
    // Used to cache the views within the item layout for fast access
    inner class ViewHolder(listItemView: View) : RecyclerView.ViewHolder(listItemView) {
        // Your holder should contain and initialize a member variable
        // for any view that will be set as you render a row
        val deviceName: TextView = itemView.findViewById(R.id.device_name)
        val deviceAddress: TextView = itemView.findViewById(R.id.device_address)
    }

    private val mLeDevices: ArrayList<BluetoothDevice> = ArrayList()

    fun addDevice(device:  BluetoothDevice) {
        if (!mLeDevices.contains(device)) {
            mLeDevices.add(device)
        }
    }

    fun getDevice(position: Int): BluetoothDevice {
        return mLeDevices[position]
    }

    fun clear() {
        mLeDevices.clear()
    }

    // Usually involves inflating a layout from XML and returning the holder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val context = parent.context
        val inflater = LayoutInflater.from(context)
        // Inflate the custom layout
        val deviceView = inflater.inflate(R.layout.item_device, parent, false)
        // Return a new holder instance
        return ViewHolder(deviceView)
    }

    // Involves populating data into the item through holder
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        // Get the data model based on position
        val device: BluetoothDevice = mLeDevices[position]
        // Set item views based on your views and data model
        val deviceName = holder.deviceName
        deviceName.text = device.name
        val deviceAddress = holder.deviceAddress
        deviceAddress.text = device.address
    }

    override fun getItemCount(): Int {
        return mLeDevices.size
    }
}
