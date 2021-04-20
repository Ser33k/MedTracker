package com.example.medtracker.ui.screen

import android.bluetooth.BluetoothDevice
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.runtime.Composable

@Composable
fun DeviceScanScreen() {

}

@Composable
fun BleDeviceList(
    bleDevices: List<BluetoothDevice>
) {
    LazyColumn() {
        items(bleDevices) {
            BleDeviceItem(bleDevice = it)
        }
    }
}

@Composable
fun BleDeviceItem(bleDevice: BluetoothDevice) {
    Row {
        Text(text = bleDevice.name)
        Text(text = bleDevice.address)
    }
}