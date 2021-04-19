package com.example.medtracker.ui

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import com.vanpra.composematerialdialogs.MaterialDialog
import com.vanpra.composematerialdialogs.datetime.datepicker
import java.time.LocalDate

@Composable
fun ActivityScreen() {
    val TAG = object{}.javaClass.enclosingMethod.name

    val dialog = remember { MaterialDialog() }
    var date = remember {
        LocalDate.now()
    }

    dialog.build {
        datepicker(waitForPositiveButton = false) { selectedDate ->
            val d = Log.i(TAG, selectedDate.toString())
            date = selectedDate
        }
    }

    val onSelectDateClick = {
        dialog.show()
        val d = Log.i(TAG, "Dialog opened")
    }

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        CalendarWidget(onSelectDateClick, date)
        CityMapView(latitude = "51.075103", longitude = "16.992012" )
    }
    
}

@Composable
fun CalendarWidget(
    onSelectDateClick: () -> Unit,
    date: LocalDate) {

    Row {
        Button(
            onClick = onSelectDateClick,
            colors = ButtonDefaults.textButtonColors(
                backgroundColor = Color.Green
            )
        ) {
            Text("Select date")
        }

        Text(text = date.toString())
    }

}