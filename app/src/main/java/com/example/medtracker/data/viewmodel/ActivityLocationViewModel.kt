package com.example.medtracker.data.viewmodel

import androidx.lifecycle.*
import com.example.medtracker.data.entity.ActivityLocation
import com.example.medtracker.data.repository.ActivityLocationRepository
import kotlinx.coroutines.launch

class ActivityLocationViewModel(private val repository: ActivityLocationRepository): ViewModel() {
    val allActivityLocations: LiveData<List<ActivityLocation>> = repository.allActivityLocations.asLiveData()

    fun insert(activityLocation: ActivityLocation) = viewModelScope.launch {
        repository.insert(activityLocation)
    }
}

class ActivityLocationViewModelFactory(private val repository: ActivityLocationRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ActivityLocationViewModel::class.java)) {
            return ActivityLocationViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}