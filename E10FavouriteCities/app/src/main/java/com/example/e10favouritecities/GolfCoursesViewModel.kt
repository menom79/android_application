package com.example.e10favouritecities

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.clustering.ClusterItem
import kotlinx.coroutines.launch

class GolfCoursesViewModel: ViewModel() {
    data class GolfCoursesState(
        val isLoading: Boolean = false,
        val allGolfCourses: List<Course> = emptyList(),
        val error: String? = null
    )

    private val _golfCoursesState = mutableStateOf(GolfCoursesState())
    val state: State<GolfCoursesState> = _golfCoursesState
    init {
        if (!_golfCoursesState.value.isLoading) fetchGolfCourses()
    }

    private fun updateAllGolfCoursesState(allGolfCourses: List<Course> = emptyList(), error: String? = null){
        if (error == null) {
            _golfCoursesState.value = _golfCoursesState.value.copy(
                isLoading = false,
                allGolfCourses = allGolfCourses,
                error = null
            )
        } else{
            _golfCoursesState.value = _golfCoursesState.value.copy(
                isLoading = false,
                allGolfCourses = emptyList(),
                error = error
            )
        }
    }

    private fun fetchGolfCourses(){
        viewModelScope.launch {
            try{
                _golfCoursesState.value = _golfCoursesState.value.copy(isLoading = true)
                val response = GolfCoursesApiService.getGolfCourses()
                updateAllGolfCoursesState(allGolfCourses = response.courses)
            } catch(e: Exception){
                updateAllGolfCoursesState(error="Error occurred while fetching data: ${e.message}")
            } finally {
                _golfCoursesState.value = _golfCoursesState.value.copy(isLoading = false)
            }
        }
    }
}


data class GolfCoursesResponse(
    val info: String,
    val courses: List<Course>
)
data class Course(
    val lat: Double,
    val lng: Double,
    val type: String,
    val course: String,
    val address: String,
    val phone: String,
    val email: String,
    val web: String,
    val image: String,
    val text: String
): ClusterItem {
    override fun getPosition(): LatLng {
        return LatLng(lat, lng)
    }
    fun getImageURL(): String {
        println("${golfCoursesApiBaseURL}${image}")
        return "${golfCoursesApiBaseURL}${image}"
    }

    override fun getTitle(): String =
        "$course - $type"

    override fun getSnippet(): String =
        text

    override fun getZIndex(): Float =
        1F
}
// Sample api response
//{
//    "info": "SGKY:N JÄSENKENTÄT 2016",
//    "courses": [
//    {
//        "type": "Kulta",
//        "lat": 62.2653926,
//        "lng": 22.6415612,
//        "course": "Alastaro Golf",
//        "address": "Golfkentäntie 195, 32560 Virttaa",
//        "phone": "(02) 724 7824",
//        "email": "minna.nenonen@alastarogolf.fi",
//        "web": "http://alastarogolf.fi/",
//        "image": "kuvat/kulta.jpg",
//        "text": "Alastaro Golfin Virttaankankaan golfkenttä kunnioittaa alustansa puolesta perinteitä. Virttaan kenttä on alkuperäisten merenranta-linksien tavoin kokonaan hiekkapohjainen. Pelaaminen on säästä riippumatta miellyttävää, kun pallo rullaa väylillä ja kentän pohja imee sateet nopeasti sisäänsä."
//    },
//    {
//        "type": "Kulta/Etu",
//        "lat": 60.3113719,
//        "lng": 22.2653926,
//        "course": "Archipelagia Golf Oy",
//        "address": "Finbyntie 87, 21600 Parainen",
//        "phone": "(02) 458 2001",
//        "email": "nina.katajainen@argc.fi",
//        "web": "http://www.archipelagiagolf.fi/fi/www/",
//        "image": "kuvat/kulta.jpg",
//        "text": "Paraisten kenttä täyttää kaikki vaatimukset mitä odotetaan hyvältä 18-väyläiseltä golfkentältä. Sopivasti vettä ja hiekkaa kierroksen aikana.... tekevät pelistä mielenkiintoisen ja haastavan!"
//    },
//    ]
//}
