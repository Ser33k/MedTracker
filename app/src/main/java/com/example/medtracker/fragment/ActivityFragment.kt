package com.example.medtracker.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.example.medtracker.R
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.MarkerOptions


class ActivityFragment:Fragment(R.layout.fragment_activity){

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val supportMapFragment =
            childFragmentManager.findFragmentById(R.id.google_map) as SupportMapFragment;

        supportMapFragment.getMapAsync(OnMapReadyCallback { googleMap ->
            googleMap.setOnMapClickListener(GoogleMap.OnMapClickListener {
                var markerOptions = MarkerOptions()

                markerOptions.position(it)
                markerOptions.title("${it.latitude} : ${it.longitude}");

                googleMap.clear();

                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                    it, 10f
                ))

                googleMap.addMarker(markerOptions);

            })
        })

    }
}


