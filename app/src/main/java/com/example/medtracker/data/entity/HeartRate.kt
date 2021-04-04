package com.example.medtracker.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity
data class HeartRate (@PrimaryKey val date: Date, val value: Int)