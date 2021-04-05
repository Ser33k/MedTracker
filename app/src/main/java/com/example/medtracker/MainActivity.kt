package com.example.medtracker


import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.medtracker.data.viewmodel.HeartRateViewModel
import com.example.medtracker.data.viewmodel.HeartRateViewModelFactory
import com.example.medtracker.ui.theme.MedTrackerTheme

class MainActivity : AppCompatActivity() {
    private val heartRateViewModel: HeartRateViewModel by viewModels {
        HeartRateViewModelFactory((application as MedTrackerApplication).repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MedTrackerTheme {
                MainScreen(
                    heartRateViewModel
                )
            }
        }
    }
}

