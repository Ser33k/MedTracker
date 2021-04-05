package com.example.medtracker.run

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.fragment.app.Fragment
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.ktx.addMarker
import com.google.maps.android.ktx.awaitMap
import kotlinx.coroutines.launch


class RunFragment:Fragment(){

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = ComposeView(requireContext()).apply {
        setContent {
            MaterialTheme {
                CityMapView(latitude = "51.075103", longitude = "16.992012" )
            }
        }
    }
}



//@Composable
//fun DetailsScreen(
//    args: DetailsActivityArg,
//    viewModelFactory: DetailsViewModelFactory,
//    onErrorLoading: () -> Unit
//) {
//    val viewModel: DetailsViewModel = viewModel(
//        factory = DetailsViewModel.provideFactory(viewModelFactory, args.cityName)
//    )
//
//    val cityDetailsResult = remember(viewModel) { viewModel.cityDetails }
//    if (cityDetailsResult is Result.Success<ExploreModel>) {
//        DetailsContent(cityDetailsResult.data)
//    } else {
//        onErrorLoading()
//    }
//}
//
//@Composable
//fun DetailsContent(exploreModel: ExploreModel) {
//    Column(verticalArrangement = Arrangement.Center) {
//        Spacer(Modifier.height(32.dp))
//        Text(
//            modifier = Modifier.align(Alignment.CenterHorizontally),
//            text = exploreModel.city.nameToDisplay,
//            style = MaterialTheme.typography.h4
//        )
//        Text(
//            modifier = Modifier.align(Alignment.CenterHorizontally),
//            text = exploreModel.description,
//            style = MaterialTheme.typography.h6
//        )
//        Spacer(Modifier.height(16.dp))
//        CityMapView(exploreModel.city.latitude, exploreModel.city.longitude)
//    }
//}

@Composable
fun CityMapView(latitude: String, longitude: String) {
    // The MapView lifecycle is handled by this composable. As the MapView also needs to be updated
    // with input from Compose UI, those updates are encapsulated into the MapViewContainer
    // composable. In this way, when an update to the MapView happens, this composable won't
    // recompose and the MapView won't need to be recreated.
    val mapView = rememberMapViewWithLifecycle()
    MapViewContainer(mapView, latitude, longitude)
}

@Composable
private fun MapViewContainer(
    map: MapView,
    latitude: String,
    longitude: String
) {
    var zoom by rememberSaveable { mutableStateOf(InitialZoom) }
    val coroutineScope = rememberCoroutineScope()

    ZoomControls(zoom) {
        zoom = it.coerceIn(MinZoom, MaxZoom)
    }
    AndroidView({ map }) { mapView ->
        // Reading zoom so that AndroidView recomposes when it changes. The getMapAsync lambda
        // is stored for later, Compose doesn't recognize state reads
        val mapZoom = zoom
        coroutineScope.launch {
            val googleMap = mapView.awaitMap()
            googleMap.setZoom(mapZoom)
            val position = LatLng(latitude.toDouble(), longitude.toDouble())
            googleMap.addMarker {
                position(position)
            }
            googleMap.moveCamera(CameraUpdateFactory.newLatLng(position))

            // Part from onCreateView
            googleMap.setOnMapClickListener {
                googleMap.clear();

                googleMap.animateCamera(
                    CameraUpdateFactory.newLatLng(it)
                )

                googleMap.addMarker {
                    position(it)
                    title("${it.latitude} : ${it.longitude}")
                }

            }


        }
    }
}


@Composable
private fun ZoomControls(
    zoom: Float,
    onZoomChanged: (Float) -> Unit
) {
    Row(Modifier.fillMaxWidth().background(Color.Red), horizontalArrangement = Arrangement.Center) {
        ZoomButton("-", onClick = { onZoomChanged(zoom * 0.8f) })
        ZoomButton("+", onClick = { onZoomChanged(zoom * 1.2f) })
    }
}

@Composable
private fun ZoomButton(text: String, onClick: () -> Unit) {
    Button(
        modifier = Modifier.padding(8.dp),
        colors = ButtonDefaults.buttonColors(
            backgroundColor = MaterialTheme.colors.onPrimary,
            contentColor = MaterialTheme.colors.primary
        ),
        onClick = onClick
    ) {
        Text(text = text, style = MaterialTheme.typography.h5)
    }
}



private const val InitialZoom = 15f
const val MinZoom = 2f
const val MaxZoom = 20f


