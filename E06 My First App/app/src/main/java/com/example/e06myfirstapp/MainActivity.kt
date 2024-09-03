package com.example.e06myfirstapp

import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.e06myfirstapp.ui.theme.E06MyFirstAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        setContent {
            App()
        }
    }
}

@Composable
fun App(){
    val navController = rememberNavController()

    E06MyFirstAppTheme {
        // A surface container using the 'background' color from the theme
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            NavHost(navController = navController, startDestination = "home"){
                composable("home"){
                    HomeScreen(onNavigateToDetails = {data -> navController.navigate("details/${data}")})
                }
                composable("details/{data}", arguments = listOf(navArgument("data") { defaultValue = "No data provided!" })){
                        backStackEntry ->
                    backStackEntry.arguments?.getString("data")?.let {
                        DetailsScreen(it, onNavigateToHome = {
                            navController.navigate("home") {
                                // Pop up to the start destination of the graph to
                                // avoid building up a large stack of destinations
                                // on the back stack as users select items
                                popUpTo("home") {
                                    saveState = false
                                }
                                // Avoid multiple copies of the same destination when
                                // reselecting the same item
                                launchSingleTop = true
                                // Restore state when reselecting a previously selected item
                                restoreState = true
                            }
                        })
                    }
                }
            }
        }
    }
}

@Composable
fun HomeScreen(onNavigateToDetails:(data: String)-> Unit){
    var textInput by rememberSaveable{ mutableStateOf("lasdf ") }

    Column(modifier= Modifier
        .fillMaxSize()
        .padding(10.dp, 12.dp), verticalArrangement = Arrangement.spacedBy(8.dp), horizontalAlignment = Alignment.End) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
            Text(text = "Home", fontWeight = FontWeight.ExtraBold, fontSize = 24.sp)
            Button(onClick = { onNavigateToDetails(textInput) }) {
                Text(text = "Send to details")
            }
        }

        TextField(value = textInput, onValueChange = {textInput = it}, modifier = Modifier
            .weight(1f)
            .fillMaxWidth())
    }
}

@Composable
fun DetailsScreen(data: String, onNavigateToHome:()-> Unit){
    Column(modifier= Modifier
        .fillMaxSize()
        .padding(8.dp, 12.dp), verticalArrangement = Arrangement.SpaceBetween, horizontalAlignment = Alignment.End) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
            Text(text = "Details", fontWeight = FontWeight.ExtraBold, fontSize = 24.sp)
            Button(onClick = onNavigateToHome) {
                Text(text = "Go to home")
            }
        }

        Text(text=data, modifier = Modifier
            .weight(1f)
            .fillMaxWidth()
            .verticalScroll(
                rememberScrollState()
            ))
    }
}