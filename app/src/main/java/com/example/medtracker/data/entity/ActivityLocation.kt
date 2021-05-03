package com.example.medtracker.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity
data class ActivityLocation(@PrimaryKey val date: Date, val latitude: Double, val longitude: Double)
