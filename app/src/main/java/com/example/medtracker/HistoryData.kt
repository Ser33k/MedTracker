package com.example.medtracker

import android.icu.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class HistoryData(val date: Date, val maxHr: Int, val minHr: Int){
    companion object {
        fun createRandomDataList(n: Int): ArrayList<HistoryData> {
            val data = ArrayList<HistoryData>()

            for(i in 1..n) {
                val c:Calendar = Calendar.getInstance()
                val min:Int = (Math.random()*100).toInt()
                val max:Int = (Math.random()*100).toInt()
                data.add(HistoryData(c.time, min, max))
            }
            return data
        }
    }
}