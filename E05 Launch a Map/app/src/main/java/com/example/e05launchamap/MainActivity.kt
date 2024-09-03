package com.example.e05launchamap

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.example.e05launchamap.ui.theme.E05LaunchAMapTheme
import compose.icons.TablerIcons
import compose.icons.tablericons.Tag
import compose.icons.tablericons.WorldLatitude
import compose.icons.tablericons.WorldLongitude

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val winSize = calculateWindowSizeClass(activity = this)
            E05LaunchAMapTheme {
                App(modifier = Modifier.padding(16.dp, 12.dp), windowSize = winSize)
            }
        }
    }
}

@Composable
fun App(modifier: Modifier = Modifier, windowSize: WindowSizeClass) {
    when (windowSize.widthSizeClass) {
        else -> {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background)
            ) {
                AppTitle(modifier)
                MainSection(modifier)
            }
        }
    }
}


@Composable
fun MainSection(modifier: Modifier = Modifier) {
    var lon by rememberSaveable { mutableStateOf("0.0") }
    var lat by rememberSaveable { mutableStateOf("0.0") }
    var label by rememberSaveable { mutableStateOf("Perfect Destination") }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .then(modifier)
    ) {
        Text(
            text = stringResource(R.string.input_field_description),
            color = MaterialTheme.colorScheme.onBackground
        )

        Row(
            Modifier
                .fillMaxWidth()
                .padding(top = 16.dp)
                .wrapContentHeight(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            TextField(
                value = lat,
                onValueChange = { lat = it },
                placeholder = { Text(stringResource(id = R.string.latitude)) },
                leadingIcon = { Icon(imageVector = TablerIcons.WorldLatitude, "") },
                keyboardOptions = KeyboardOptions().copy(
                    autoCorrect = false,
                    keyboardType = KeyboardType.Decimal
                ),
                modifier = Modifier.weight(1f)
            )
            TextField(
                value = lon,
                onValueChange = { lon = it },
                placeholder = { Text(stringResource(id = R.string.longitude)) },
                leadingIcon = { Icon(imageVector = TablerIcons.WorldLongitude, "") },
                keyboardOptions = KeyboardOptions().copy(
                    autoCorrect = false,
                    keyboardType = KeyboardType.Number
                ),
                modifier = Modifier.weight(1f)
            )
        }
        Row(
            Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(top = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            val context = LocalContext.current

            TextField(
                value = label,
                onValueChange = { label = it },
                placeholder = { Text(stringResource(id = R.string.label)) },
                leadingIcon = { Icon(imageVector = TablerIcons.Tag, "") },
                keyboardOptions = KeyboardOptions().copy(
                    autoCorrect = true,
                    keyboardType = KeyboardType.Text
                ),
                modifier = Modifier
                    .wrapContentHeight()
                    .weight(1f)
            )
            Button(modifier = Modifier.widthIn(min = 65.dp, max = Dp.Unspecified),
                onClick = {
                    if (lat.toFloatOrNull()==null){
                        Toast.makeText(context, "Error in latitude input!", Toast.LENGTH_LONG).show()
                    }
                    if (lon.toFloatOrNull()==null){
                        Toast.makeText(context, "Error in longitude input!", Toast.LENGTH_LONG).show()
                    }
                    val mapUri = Uri.parse("geo:0,0?q=$lat,$lon(${Uri.encode(label)})&z=13")
                    val mapIntent = Intent(Intent.ACTION_VIEW, mapUri)
                    mapIntent.setPackage("com.google.android.apps.maps")
                    context.startActivity(mapIntent)
                }) {
                Text(text = stringResource(id = R.string.open_map))
            }
        }
    }
}

@Composable
fun AppTitle(modifier: Modifier = Modifier) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.primary)
            .then(modifier)
    )
    {
        Text(
            text = stringResource(id = R.string.app_name),
            color = MaterialTheme.colorScheme.onPrimary,
            style = MaterialTheme.typography.titleMedium
        )
    }
}


@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@Preview
@Composable
fun AppPreview() {
    val winSize = WindowSizeClass.calculateFromSize(DpSize(1080.dp, 2350.dp))
    App(Modifier.padding(16.dp, 12.dp), windowSize = winSize)
}

@Preview(showBackground = true)
@Composable
fun MainSectionPreview(){
    MainSection()
}

@Preview(showBackground = true)
@Composable
fun TitlePreview() {
    AppTitle()
}
