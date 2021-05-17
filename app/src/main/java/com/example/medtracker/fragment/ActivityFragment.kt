package com.example.medtracker.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.example.medtracker.MedTrackerApplication
import com.example.medtracker.R
import com.example.medtracker.data.entity.ActivityLocation
import com.example.medtracker.data.viewmodel.ActivityLocationViewModel
import com.example.medtracker.data.viewmodel.ActivityLocationViewModelFactory
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions


class ActivityFragment:Fragment(R.layout.fragment_activity){

    private val activityLocationViewModel: ActivityLocationViewModel by viewModels {
        ActivityLocationViewModelFactory((requireActivity().application as MedTrackerApplication).activityLocationRepository)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val supportMapFragment =
            childFragmentManager.findFragmentById(R.id.google_map1) as SupportMapFragment;

        activityLocationViewModel.allActivityLocations.observe(viewLifecycleOwner, Observer { activityLocations: List<ActivityLocation> ->
            // Update the map with locations.
            activityLocations?.let { locList ->
                val polylineOptions = PolylineOptions()
                polylineOptions.addAll(locList.stream().map {
                    LatLng(it.latitude, it.longitude)
                }.iterator().asSequence().asIterable())
                polylineOptions.color(0xffe8642b.toInt())

                val last = locList.size - 1
                if (last >= 0) {
                    val loc = locList[last]
                    val lat = loc.latitude
                    val lon = loc.longitude

                    supportMapFragment.getMapAsync(OnMapReadyCallback { googleMap ->
                        var markerOptions = MarkerOptions()
                        val lating = LatLng(lat, lon)
                        markerOptions.position(lating)
                        markerOptions.title("${lating.latitude} : ${lating.longitude}")

                        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                            lating, 20f
                        ))
                        googleMap.addMarker(markerOptions)

                        googleMap.addPolyline(polylineOptions)
                    })
                }

            }
        })

    }
}


