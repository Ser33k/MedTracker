package com.example.medtracker.data.repository

import androidx.annotation.WorkerThread
import com.example.medtracker.data.dao.HeartRateDao
import com.example.medtracker.data.entity.ActivityLocation
import com.example.medtracker.data.entity.HeartRate
import kotlinx.coroutines.flow.Flow
import java.util.*

// Declares the DAO as a private property in the constructor. Pass in the DAO
// instead of the whole database, because you only need access to the DAO
class HeartRateRepository(private val heartRateDao: HeartRateDao) {
    // Room executes all queries on a separate thread.
    // Observed Flow will notify the observer when the data has changed.
    val allHeartRates: Flow<List<HeartRate>> = heartRateDao.getChronologicalHeartRates()

    // By default Room runs suspend queries off the main thread, therefore, we don't need to
    // implement anything else to ensure we're not doing long running database work
    // off the main thread.
    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insert(heartRate: HeartRate) {
        heartRateDao.insert(heartRate)
    }

    fun getHeartRatesBetweenDates(from: Date, to: Date):Flow<List<HeartRate>> {
        return heartRateDao.getHeartRatesBetweenDates(from, to)
    }
}