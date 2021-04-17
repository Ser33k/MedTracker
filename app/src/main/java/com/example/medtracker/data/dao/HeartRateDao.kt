package com.example.medtracker.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.medtracker.data.entity.HeartRate
import kotlinx.coroutines.flow.Flow
import java.util.*

@Dao
interface HeartRateDao {
    @Query("SELECT * FROM heartrate ORDER BY date ASC")
    fun getChronologicalHeartRates(): Flow<List<HeartRate>>

    @Query("SELECT * FROM heartrate WHERE date BETWEEN :from AND :to")
    fun getHeartRatesBetweenDates(from: Date, to: Date): Flow<List<HeartRate>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(heartRate: HeartRate)

    @Query("DELETE FROM heartrate")
    suspend fun deleteAll()
}