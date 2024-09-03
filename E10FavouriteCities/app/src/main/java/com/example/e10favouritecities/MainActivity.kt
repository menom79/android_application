package com.example.e10favouritecities

import android.content.Context
import android.graphics.Bitmap
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.DrawableRes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Place
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import coil.compose.SubcomposeAsyncImage
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.example.e10favouritecities.ui.theme.E10FavouriteCitiesTheme
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.clustering.ClusterItem
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapsComposeExperimentalApi
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerInfoWindow
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.clustering.Clustering
import com.google.maps.android.compose.rememberCameraPositionState
import compose.icons.TablerIcons
import compose.icons.tablericons.ExternalLink
import compose.icons.tablericons.MapPin
import compose.icons.tablericons.PhoneOutgoing

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val appViewModel: GolfCoursesViewModel = viewModel()
            val appState by appViewModel.state

            E10FavouriteCitiesTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    App(appState.allGolfCourses)
                }
            }
        }
    }
}


@OptIn(MapsComposeExperimentalApi::class)
@Composable
fun App(golfCourses: List<Course>) {
    var configuration = LocalConfiguration.current
    var camPos = LatLng(62.241046, 25.750684)
    if (golfCourses.isNotEmpty()) camPos = golfCourses[0].getPosition()
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(camPos, 5.5f)
    }

    GoogleMap(
        modifier = Modifier.fillMaxSize(),
        cameraPositionState = cameraPositionState
    ) {
        Clustering(items=golfCourses)

//        for (course in golfCourses) {
//            val painter = rememberAsyncImagePainter(model = course.getImageURL())
//
//            MarkerInfoWindow(
//                state = MarkerState(position = course.getPosition()),
//                title = course.course,
//                snippet = course.text,
//            ) {
//                OutlinedCard(modifier = Modifier
//                    .fillMaxWidth(((configuration.screenWidthDp * 0.85) / configuration.screenWidthDp).toFloat())
//                    .heightIn(max = (configuration.screenHeightDp * 0.75).dp)
//                ) {
//                    Column(
//                        modifier = Modifier
//                            .padding(16.dp),
//                        verticalArrangement = Arrangement.spacedBy(12.dp)
//                    ) {
////                        Image(painter = painter, contentDescription = null)
//                        Text(
//                            text = "${course.course} - ${course.type}",
//                            style = MaterialTheme.typography.titleLarge,
//                            textAlign = TextAlign.Center,
//                            modifier = Modifier
//                                .fillMaxWidth()
//                                .padding(bottom = 6.dp)
//                        )
//
//                        Column {
//                            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
//                                Icon(
//                                    imageVector = TablerIcons.PhoneOutgoing,
//                                    contentDescription = null
//                                )
//                                Text(text = course.phone)
//                            }
//                            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
//                                Icon(imageVector = TablerIcons.MapPin, contentDescription = null)
//                                Text(text = course.address)
//                            }
//                            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
//                                Icon(
//                                    imageVector = TablerIcons.ExternalLink,
//                                    contentDescription = null
//                                )
//                                Text(text = course.web)
//                            }
//                        }
//
//                        Text(
//                            text = course.text,
//                            modifier = Modifier
//                                .fillMaxWidth()
//                                .verticalScroll(
//                                    rememberScrollState()
//                                )
//                        )
//                    }
//                }
//            }
//        }
    }
}


@Composable
fun MapMarker(
    context: Context,
    position: LatLng,
    title: String,
    snippet: String,
    @DrawableRes iconResourceId: Int
) {
    val icon = bitmapDescriptorFromVector(
        context, iconResourceId
    )
    Marker(
        state = MarkerState(position = position),
        title = title,
        snippet = snippet,
        icon = icon,
    )
}

fun bitmapDescriptorFromVector(
    context: Context,
    vectorResId: Int
): BitmapDescriptor? {

    // retrieve the actual drawable
    val drawable = ContextCompat.getDrawable(context, vectorResId) ?: return null
    drawable.setBounds(0, 0, drawable.intrinsicWidth, drawable.intrinsicHeight)
    val bm = Bitmap.createBitmap(
        drawable.intrinsicWidth,
        drawable.intrinsicHeight,
        Bitmap.Config.ARGB_8888
    )

    // draw it onto the bitmap
    val canvas = android.graphics.Canvas(bm)
    drawable.draw(canvas)
    return BitmapDescriptorFactory.fromBitmap(bm)
}
