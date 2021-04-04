package com.example.medtracker.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.medtracker.data.dao.HeartRateDao
import com.example.medtracker.data.entity.HeartRate
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.time.Instant
import java.util.*

// Annotates class to be a Room Database with a table (entity) of the HeartRate class
@Database(entities = [HeartRate::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
public abstract class HeartRateDatabase : RoomDatabase() {

    abstract fun heartRateDao(): HeartRateDao

    private class HeartRateDatabaseCallback(
        private val scope: CoroutineScope
    ) : RoomDatabase.Callback() {

        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            INSTANCE?.let { database ->
                scope.launch {
                    populateDatabase(database.heartRateDao())
                }
            }
        }

        suspend fun populateDatabase(heartRateDao: HeartRateDao) {
            // Delete all content here.
            heartRateDao.deleteAll()

            // Add sample heartRates.
            for (i in 1..50) {
                val heartRate = HeartRate(Calendar.getInstance().time,80 + i*2)
                heartRateDao.insert(heartRate)
            }

            // TODO: Add your own heartRates!
        }
    }

    companion object {
        // Singleton prevents multiple instances of database opening at the
        // same time.
        @Volatile
        private var INSTANCE: HeartRateDatabase? = null

        fun getDatabase(context: Context, scope: CoroutineScope): HeartRateDatabase {
            // if the INSTANCE is not null, then return it,
            // if it is, then create the database
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    HeartRateDatabase::class.java,
                    "heartrate_database"
                )
                    .addCallback(HeartRateDatabaseCallback(scope))
                    .build()
                INSTANCE = instance
                // return instance
                instance
            }
        }
    }
}