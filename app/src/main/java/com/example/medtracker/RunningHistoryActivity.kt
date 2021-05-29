package com.example.medtracker

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

import android.widget.DatePicker

import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.graphics.Color
import android.os.Build
import android.text.InputType
import android.util.Log
import android.view.View
import android.widget.Button

import android.widget.EditText

import android.widget.TextView
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.fragment.app.clearFragmentResult
import androidx.fragment.app.viewModels
import androidx.lifecycle.LifecycleOwner
import com.example.medtracker.data.entity.ActivityLocation
import com.example.medtracker.data.viewmodel.ActivityLocationViewModel
import com.example.medtracker.data.viewmodel.ActivityLocationViewModelFactory
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.type.Date
import kotlinx.android.synthetic.main.activity_running_history.*
import java.util.*
import kotlin.time.milliseconds


class RunningHistoryActivity : AppCompatActivity(), OnMapReadyCallback {

    private var mapFragment: SupportMapFragment? = null
    private lateinit var mMap: GoogleMap

    private val activityLocationViewModel: ActivityLocationViewModel by viewModels {
        ActivityLocationViewModelFactory((application as MedTrackerApplication).activityLocationRepository)
    }


    private var picker: DatePickerDialog? = null
    private var eText: EditText? = null
    private var btnGet: Button? = null
    private var tvw: TextView? = null
    private var calTv: TextView? = null
    private var timeTv: TextView? = null

    private var day: Int = 0
    private var month: Int = 0
    private var year1: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_running_history)
        tvw = findViewById<View>(R.id.textView5) as TextView
        calTv = findViewById<View>(R.id.textView1) as TextView
        timeTv = findViewById<View>(R.id.textView3) as TextView
        eText = findViewById<View>(R.id.editText1) as EditText
        eText!!.inputType = InputType.TYPE_NULL
        eText!!.setOnClickListener {
            val cldr: Calendar = Calendar.getInstance()
            day = cldr.get(Calendar.DAY_OF_MONTH)
            month = cldr.get(Calendar.MONTH)
            year1 = cldr.get(Calendar.YEAR)
            // date picker dialog
            picker = DatePickerDialog(
                this@RunningHistoryActivity,
                { view, year, monthOfYear, dayOfMonth ->
                    eText!!.setText(dayOfMonth.toString() + "/" + (monthOfYear + 1) + "/" + year)
                    year1 = year
                    month = monthOfYear
                    day = dayOfMonth
                },
                year1,
                month,
                day
            )
            picker!!.show()
        }
        btnGet = findViewById<View>(R.id.button1) as Button
        btnGet!!.setOnClickListener(View.OnClickListener {
//            tvw!!.text = "Selected Date: " + eText!!.text
            onGetRouteClick()
        })

        mapFragment = supportFragmentManager
            .findFragmentById(R.id.google_map2) as SupportMapFragment?
        mapFragment?.getMapAsync(this)
    }


    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
    }

    private val COLOR_BLACK_ARGB = -0x1000000
    private val POLYLINE_STROKE_WIDTH_PX = 12

    private fun stylePolyline(polyline: Polyline) {
        // Get the data object stored with the polyline.
        // Use a custom bitmap as the cap at the start of the line.
//        polyline.startCap = CustomCap(
//            BitmapDescriptorFactory.fromResource(R.drawable.play_button), 30f
//        )
//        polyline.endCap = CustomCap(
//            BitmapDescriptorFactory.fromResource(R.drawable.stop), 30f
//        )
        polyline.width = POLYLINE_STROKE_WIDTH_PX.toFloat()
        polyline.color = Color.BLUE
        polyline.jointType = JointType.ROUND

        polyline.endCap = RoundCap()
        polyline.startCap = RoundCap()
    }

//        polyline.endCap = RoundCap()


    private fun onGetRouteClick() {

        mMap.clear()
        var from: java.util.Date? = null
        var to: java.util.Date? = null
//        var to: java.util.Date? = Date()
        if (day != 0 && month != 0 && year1 != 0) {
            from = Date(year1 - 1900, month, day);
//            to = Date(2021-1900, 4,1);
            val c = Calendar.getInstance()
            c.time = from
            c.add(Calendar.DATE, 1)
            to = c.time


            var distance: Double = 0.0
            var i = 1

            activityLocationViewModel.getActivityLocationsBetweenDates(from, to!!)
                .observe(this, { activityLocations: List<ActivityLocation> ->
                    activityLocations?.let { locList ->

                        while (i <= locList.size - 1) {
                            distance += distance(
                                locList.get(i - 1).latitude,
                                locList.get(i - 1).longitude,
                                locList.get(i).latitude,
                                locList.get(i).longitude
                            )
                            i++
                        }

                        val polyline = PolylineOptions()
                        polyline.addAll(locList.stream().map {
                            LatLng(it.latitude, it.longitude)
                        }.iterator().asSequence().asIterable())
                        polyline.color(0xff0000ee.toInt())

                        if (locList.size - 1 >= 0) {
                            val loc = locList[locList.size - 1]
                            val lat = loc.latitude
                            val lon = loc.longitude

                            mapFragment!!.getMapAsync(OnMapReadyCallback { googleMap ->
                                val lating = LatLng(lat, lon)

                                googleMap.animateCamera(
                                    CameraUpdateFactory.newLatLngZoom(
                                        lating, 17f
                                    )
                                )

                                stylePolyline(googleMap.addPolyline(polyline))
                                tvw!!.text = String.format("%.2f", distance * 1.609344)

                            })
                        }
                    }


                })

//        Log.i("History_TAG", activityLocationViewModel.getActivityLocationsBetweenDates(from, to).let {
//
//        })
        }
    }

    private fun distance(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double {
        val theta = lon1 - lon2
        var dist = (Math.sin(deg2rad(lat1))
                * Math.sin(deg2rad(lat2))
                + (Math.cos(deg2rad(lat1))
                * Math.cos(deg2rad(lat2))
                * Math.cos(deg2rad(theta))))
        dist = Math.acos(dist)
        dist = rad2deg(dist)
        dist = dist * 60 * 1.1515
        return dist
    }

    private fun calcCalories(){
        // http://www.shapesense.com/fitness-exercise/calculators/heart-rate-based-calorie-burn-calculator.shtml
        // Male: ((-55.0969 + (0.6309 x HR) + (0.1988 x W) + (0.2017 x A))/4.184) x 60 x T
        // Female: ((-20.4022 + (0.4472 x HR) - (0.1263 x W) + (0.074 x A))/4.184) x 60 x T

    }

    private fun deg2rad(deg: Double): Double {
        return deg * Math.PI / 180.0
    }

    private fun rad2deg(rad: Double): Double {
        return rad * 180.0 / Math.PI
    }


}