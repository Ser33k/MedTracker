package com.example.medtracker.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.github.tehras.charts.line.LineChartData
import com.github.tehras.charts.line.renderer.point.FilledCircularPointDrawer
import com.github.tehras.charts.line.renderer.point.HollowCircularPointDrawer
import com.github.tehras.charts.line.renderer.point.NoPointDrawer
import com.github.tehras.charts.line.renderer.point.PointDrawer

class LineChartDataModel {
    var lineChartData by mutableStateOf(
        LineChartData(
            points = listOf(
                LineChartData.Point(randomYValue(), "10:23"),
                LineChartData.Point(randomYValue(), "10:24"),
                LineChartData.Point(randomYValue(), "10:25"),
                LineChartData.Point(randomYValue(), "10:26"),
                LineChartData.Point(randomYValue(), "10:27"),
                LineChartData.Point(randomYValue(), "10:28"),
                LineChartData.Point(randomYValue(), "10:29")
            )
        )
    )
    var horizontalOffset by mutableStateOf(5f)
    var pointDrawerType by mutableStateOf(PointDrawerType.Filled)
    val pointDrawer: PointDrawer
        get() {
            return when (pointDrawerType) {
                PointDrawerType.None -> NoPointDrawer
                PointDrawerType.Filled -> FilledCircularPointDrawer()
                PointDrawerType.Hollow -> HollowCircularPointDrawer()
            }
        }

    private fun randomYValue(): Float = (100f * Math.random()).toFloat() + 45f

    enum class PointDrawerType {
        None,
        Filled,
        Hollow
    }
}