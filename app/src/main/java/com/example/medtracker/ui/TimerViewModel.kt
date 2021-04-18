package com.example.medtracker.ui

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class TimerViewModel : ViewModel() {

    private val _running = MutableLiveData(false)
    val running: LiveData<Boolean> = _running

    private var _seconds = MutableLiveData(0)
    val seconds: LiveData<Int> = _seconds

    private val update = object : Runnable {
        override fun run() {
            if (_running.value == true) {
                _seconds.value = _seconds.value?.plus(1)
            }
            handler!!.postDelayed(this, 1000)
        }
    }

    private var handler: Handler? = null
    fun start() {
        handler = Handler(Looper.getMainLooper())

    if(_running.value == false) {
        _running.value = true
        handler!!.post(update)
    }
    }

    fun stop(){
        _running.value = false
        handler!!.removeCallbacks(update)
        _seconds.value = 0
    }

    fun pause(){
        _running.value = false

        handler!!.removeCallbacks(update)
    }
}