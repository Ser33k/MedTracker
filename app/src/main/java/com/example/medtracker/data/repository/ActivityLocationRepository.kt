package com.example.medtracker.data.repository

import androidx.annotation.WorkerThread
import com.example.medtracker.data.dao.ActivityLocationDao
import com.example.medtracker.data.entity.ActivityLocation
import com.example.medtracker.data.entity.HeartRate
import kotlinx.coroutines.flow.Flow
import java.util.*

class ActivityLocationRepository(private val activityLocationDao: ActivityLocationDao) {
    val allActivityLocations: Flow<List<ActivityLocation>> = activityLocationDao.getChronologicalActivityLocations()

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insert(activityLocation: ActivityLocation) {
        activityLocationDao.insert(activityLocation)
    }

    fun getActivityLocationsBetweenDates(from: Date, to: Date): Flow<List<ActivityLocation>> {
       return activityLocationDao.getActivityLocationsBetweenDates(from, to)
    }
}