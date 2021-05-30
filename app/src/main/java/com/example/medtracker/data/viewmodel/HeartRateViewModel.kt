package com.example.medtracker.data.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.medtracker.data.entity.ActivityLocation
import com.example.medtracker.data.entity.HeartRate
import com.example.medtracker.data.repository.HeartRateRepository
import kotlinx.coroutines.launch
import java.util.*

class HeartRateViewModel(private val repository: HeartRateRepository): ViewModel() {

    // Using LiveData and caching what allHeartRates returns has several benefits:
    // - We can put an observer on the data (instead of polling for changes) and only update the
    //   the UI when the data actually changes.
    // - Repository is completely separated from the UI through the ViewModel.
    val allHeartRates: LiveData<List<HeartRate>> = repository.allHeartRates.asLiveData()

    fun getHeartRatesBetweenDates(from: Date, to: Date):  LiveData<List<HeartRate>>{
        return repository.getHeartRatesBetweenDates(from, to).asLiveData()
    }

    /**
     * Launching a new coroutine to insert the data in a non-blocking way
     */
    fun insert(heartRate: HeartRate) = viewModelScope.launch {
        repository.insert(heartRate)
    }
}

class HeartRateViewModelFactory(private val repository: HeartRateRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HeartRateViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return HeartRateViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}