package com.example.medtracker.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.medtracker.data.entity.ActivityLocation
import kotlinx.coroutines.flow.Flow
import java.util.*

@Dao
interface ActivityLocationDao {
    @Query("SELECT * FROM activitylocation ORDER BY date ASC")
    fun getChronologicalActivityLocations(): Flow<List<ActivityLocation>>

    @Query("SELECT * FROM activitylocation WHERE date BETWEEN :from AND :to")
    fun getActivityLocationsBetweenDates(from: Date, to: Date): Flow<List<ActivityLocation>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(activityLocation: ActivityLocation)

    @Query("DELETE FROM activitylocation")
    suspend fun deleteAll()
}