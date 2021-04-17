package com.example.medtracker.ui

import android.icu.text.DateFormat
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.medtracker.data.entity.HeartRate
import com.example.medtracker.data.viewmodel.HeartRateViewModel
import java.util.*

@Composable
fun StatsScreen(
    heartRateViewModel: HeartRateViewModel
) {
    val heartRates: List<HeartRate> by heartRateViewModel.allHeartRates.observeAsState(listOf())
    HeartRateList(heartRates = heartRates)
}

@Composable
fun HeartRateList(
    modifier: Modifier = Modifier,
    heartRates: List<HeartRate>
) {
    LazyColumn(modifier = modifier) {
        items(heartRates) { heartRate ->
            HeartRateItem(heartRate = heartRate)
        }
    }
}

// The UI for each list item can be generated by a reusable composable
@Composable
fun HeartRateItem(heartRate: HeartRate) {
    val formatter = remember { DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT)}
    Row(
        modifier = Modifier
        .padding(4.dp)
        .height(IntrinsicSize.Min)
    ) {
        Text(text = formatter.format(heartRate.date), Modifier.padding(4.dp), color = Color.Green)
        Divider(
            color = Color.Blue,
            modifier = Modifier
                .fillMaxHeight()
                .width(1.dp)
        )
        Text(text = heartRate.value.toString(), Modifier.padding(4.dp), color = Color.Red)
    }
}

private val heartRates = listOf(
    HeartRate(Calendar.getInstance().time,80),
    HeartRate(Calendar.getInstance().time,120)
)

@Preview
@Composable
fun HeartRateListPreview() {
    HeartRateList(heartRates = heartRates)
}

