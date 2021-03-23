package com.example.medtracker

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.gms.maps.model.Tile
import com.google.android.gms.maps.model.TileOverlay
import kotlinx.android.synthetic.main.fragment_activity.*
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.tileprovider.tilesource.TileSourcePolicy
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapController
import org.osmdroid.views.MapView

class ActivityFragment:Fragment(R.layout.fragment_activity){
    private var mMapView: MapView? = null
    private var mMapController: MapController? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mMapView = mapview;
        mMapView!!.setTileSource(TileSourceFactory.PUBLIC_TRANSPORT)

        mMapController = mMapView!!.controller as MapController
        mMapController!!.setZoom(12)
        val gPt = GeoPoint(52.229676
            , 21.012229)
        mMapController!!.setCenter(gPt)
    }

}