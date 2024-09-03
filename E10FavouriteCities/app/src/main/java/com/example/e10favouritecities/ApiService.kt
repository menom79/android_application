package com.example.e10favouritecities

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET



// https://ptm.fi/materials/golfcourses/golf_courses.json - Golf courses response url
val golfCoursesApiBaseURL = "https://ptm.fi/materials/golfcourses/"
private val apiRetro = Retrofit.Builder()
    .baseUrl(golfCoursesApiBaseURL)
    .addConverterFactory(GsonConverterFactory.create())
    .build()

val GolfCoursesApiService = apiRetro.create(ApiService::class.java)


interface ApiService {
    @GET("golf_courses.json")
    suspend fun getGolfCourses(): GolfCoursesResponse
}

