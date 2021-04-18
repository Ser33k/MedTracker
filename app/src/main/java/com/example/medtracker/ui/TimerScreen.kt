package com.example.medtracker.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.PauseCircleFilled
import androidx.compose.material.icons.outlined.PlayCircleFilled
import androidx.compose.material.icons.outlined.StopCircle
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.util.*

@Composable
fun Counter(viewModel: TimerViewModel = TimerViewModel()) {
    val seconds by viewModel.seconds.observeAsState(initial = 0)

    val hours = seconds / 3600
    val minutes = seconds % 3600 / 60
    val secs = seconds % 60

    val time = String.format(
        Locale.getDefault(),
        "%02d:%02d:%02d", hours, minutes, secs
    )
    Column(
        modifier = Modifier
            .fillMaxHeight(1f)
            .padding(10.dp)
            .fillMaxWidth(1f)
            .padding(0.dp, 30.dp, 0.dp, 0.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top

    ) {
        Text(
            text = time,
            Modifier
                .padding(10.dp)
                .align(Alignment.CenterHorizontally)
                .padding(20.dp)
                .fillMaxWidth(1f),
            textAlign = TextAlign.Center,
            fontSize = 50.sp
        )
        TimerButtons(viewModel)
    }
}


@Composable
fun TimerButtons(viewModel: TimerViewModel){
    Column(modifier = Modifier.fillMaxWidth(1f), horizontalAlignment = Alignment.CenterHorizontally) {
        IconButton(onClick = { viewModel.start() },

            modifier = Modifier.size(200.dp),

        ) {

            Icon(imageVector = Icons.Outlined.PlayCircleFilled, contentDescription = null, tint = Color.Green, modifier = Modifier.size(200.dp))
        }

        Row(){
            IconButton(onClick = { viewModel.stop() },
                modifier = Modifier.size(150.dp)
            ) {
                Icon(imageVector = Icons.Outlined.StopCircle, contentDescription = null, tint = Color.Red , modifier = Modifier.size(150.dp))
            }

            IconButton(onClick = { viewModel.pause() },

                modifier = Modifier.size(150.dp)
            ) {

                Icon(imageVector = Icons.Outlined.PauseCircleFilled, contentDescription = null, tint = Color.Cyan, modifier = Modifier.size(150.dp))
            }
        }


    }
}

@Preview
@Composable
fun TimerScreen(
){
    Counter()
}