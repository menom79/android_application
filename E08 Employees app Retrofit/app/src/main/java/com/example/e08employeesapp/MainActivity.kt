package com.example.e08employeesapp

import android.os.Bundle
import android.util.JsonReader
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberAsyncImagePainter
import com.example.e08employeesapp.ui.theme.E08EmployeesAppTheme
import compose.icons.TablerIcons
import compose.icons.tablericons.Building
import compose.icons.tablericons.Mail
import compose.icons.tablericons.MailForward
import compose.icons.tablericons.PhoneOutgoing
import org.json.JSONObject
import java.io.Reader
import java.net.URL

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            val appViewModel: EmployeesViewModel = viewModel()
            val appState by appViewModel.employeesState

            E08EmployeesAppTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    App(navController, appState, onEmployeePressed = {
                        appViewModel.setCurrentEmployee(it)
                        navController.navigate("details")
                    })
                }
            }
        }
    }
}


@Composable
fun App(navController: NavHostController, appState:  EmployeesViewModel.EmployeesState, onEmployeePressed: (EmployeeData) -> Unit){

    NavHost(navController = navController, startDestination = "home") {
        composable("home"){
            Box(modifier = Modifier.fillMaxSize()){
                when {
                    appState.isLoading -> {
                        CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                    }
                    appState.error != null -> {
                        Text(text = appState.error)
                    }
                    else -> {
                        HomeScreen(appState.allEmployees, onEmployeePressed)
                    }
                }
            }
        }
        composable("details"){
            if (appState.currentEmployee!=null) DetailsScreen(employee = appState.currentEmployee)
            else Text(text = "No employee selected!")
        }
    }
}

@Composable
fun HomeScreen(allEmployees: List<EmployeeData>, onEmployeePressed: (EmployeeData) -> Unit){
    Column {
        AppTitle(Modifier.padding(horizontal = 8.dp, vertical = 12.dp))
        LazyColumn(){
            items(allEmployees, key={it.id}){
                EmployeeItem(employee = it, onEmployeePressed = onEmployeePressed)
            }
        }
    }
}

@Composable
fun DetailsScreen(employee: EmployeeData){
    val rainbowColorsBrush = remember {
        Brush.sweepGradient(
            listOf(
                Color(0xFF9575CD),
                Color(0xFFBA68C8),
                Color(0xFFE57373),
                Color(0xFFFFB74D),
                Color(0xFFFFF176),
                Color(0xFFAED581),
                Color(0xFF4DD0E1),
                Color(0xFF9575CD)
            )
        )
    }
    val borderWidth = 5.dp

    Column(modifier = Modifier
        .fillMaxWidth()
        .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally) {
        Image(
            painter = rememberAsyncImagePainter(employee.image),
            contentDescription = null,
            modifier = Modifier
                .width(125.dp)
                .aspectRatio(1f)
                .border(
                    BorderStroke(borderWidth, rainbowColorsBrush),
                    CircleShape
                )
                .padding(borderWidth)
                .clip(CircleShape)
        )
        Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = "${employee.firstName} ${employee.lastName}", style=MaterialTheme.typography.titleLarge, modifier = Modifier.wrapContentWidth())
        }
        Column(modifier=Modifier.padding(bottom = 12.dp)) {
            Spacer(modifier = Modifier.height(6.dp))
            Text(text = employee.title, style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(6.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                Icon(imageVector = TablerIcons.Building, contentDescription = null)
                Text(text = employee.department)
            }
            Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                Icon(imageVector = TablerIcons.MailForward, contentDescription = null)
                Text(text = employee.email)
            }
            Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                Icon(imageVector = TablerIcons.PhoneOutgoing, contentDescription = null)
                Text(text = employee.phone)
            }
        }
        Divider(thickness = 2.dp)
        Column(modifier = Modifier.padding(top=12.dp)) {
            Text("Details", style = MaterialTheme.typography.titleMedium, modifier = Modifier.padding(bottom = 4.dp))
            Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                Text(text = stringResource(id = R.string.employee_details), style = MaterialTheme.typography.bodyMedium)
            }
        }
    }
}

@Composable
fun EmployeeItem(employee: EmployeeData, onEmployeePressed: (EmployeeData)->Unit){
    val rainbowColorsBrush = remember {
        Brush.sweepGradient(
            listOf(
                Color(0xFF9575CD),
                Color(0xFFBA68C8),
                Color(0xFFE57373),
                Color(0xFFFFB74D),
                Color(0xFFFFF176),
                Color(0xFFAED581),
                Color(0xFF4DD0E1),
                Color(0xFF9575CD)
            )
        )
    }
    val borderWidth = 5.dp

    Row(modifier = Modifier
            .fillMaxWidth()
            .clickable {
                onEmployeePressed(employee)
            }.padding(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)) {

        Image(
            painter = rememberAsyncImagePainter(employee.image),
            contentDescription = null,
            modifier = Modifier
                .width(125.dp)
                .aspectRatio(1f)
                .border(
                    BorderStroke(borderWidth, rainbowColorsBrush),
                    CircleShape
                )
                .padding(borderWidth)
                .clip(CircleShape)
            )

        Column(modifier=Modifier.padding(top = 10.dp, bottom = 10.dp)) {
            Text(text = "${employee.firstName} ${employee.lastName}", style=MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(6.dp))
            Text(text = employee.title, style = MaterialTheme.typography.titleSmall)
            Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                Icon(imageVector = TablerIcons.MailForward, contentDescription = null)
                Text(text = employee.email, style = MaterialTheme.typography.bodyMedium)
            }
            Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                Icon(imageVector = TablerIcons.PhoneOutgoing, contentDescription = null)
                Text(text = employee.phone, style=MaterialTheme.typography.bodyMedium)
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
            style = MaterialTheme.typography.titleLarge
        )
    }
}
