package com.example.medtracker.fragment

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.medtracker.R
import com.jjoe64.graphview.GraphView
import com.jjoe64.graphview.series.DataPoint
import com.jjoe64.graphview.series.LineGraphSeries
import java.util.*
import kotlin.random.Random


class HeartRateFragment:Fragment(R.layout.fragment_hr) {

    var hr = 90 // heart rate
    private val random = Random(2)
    private var inc = 1

    var series = LineGraphSeries(arrayOf<DataPoint>(
    ))


    lateinit var mainHandler: Handler

    private lateinit var progressText: TextView
    private lateinit var progressValue: ProgressBar

    private lateinit var graph: GraphView

        private val updateHRTask = object: Runnable {
        override fun run() {
            changeHeartRate()
            updateGraph()
            mainHandler.postDelayed(this, 1000)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        progressText = view.findViewById<TextView>(R.id.text_view_progress)
        progressValue = view.findViewById<ProgressBar>(R.id.progress_bar)
        graph = view.findViewById<GraphView>(R.id.graph)

        graph.addSeries(series)
        graph.viewport.isScalable = true
        graph.viewport.isScrollable = true
        graph.viewport.setMinX(0.0)
        graph.viewport.setMaxX(60.0)
        graph.title = "Last minute HR"


        mainHandler = Handler(Looper.getMainLooper())
    }

    override fun onPause() {
        super.onPause()
        mainHandler.removeCallbacks(updateHRTask)
    }

    override fun onResume() {
        super.onResume()
        mainHandler.post(updateHRTask)
    }

    fun changeHeartRate() {
        if (random.nextInt(0, 2) == 1)
            hr -= random.nextInt(1, 10)
        else
            hr += random.nextInt(1, 10)

        progressText.text = hr.toString()
        progressValue.progress = hr
    }

    private fun updateGraph(){
        series.appendData(DataPoint(inc++.toDouble(), hr.toDouble()), true, 60)
    }
}
