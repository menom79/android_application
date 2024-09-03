package com.example.e08employeesapp

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

// https://ptm.fi/data/android_employees.json
private val apiRetrofit = Retrofit.Builder()
    .baseUrl("https://ptm.fi/data/")
    .addConverterFactory(GsonConverterFactory.create())
    .build()

val employeesService = apiRetrofit.create(ApiService::class.java)

interface ApiService {
    @GET("android_employees.json")
    suspend fun getEmployees(): EmployeesResponse
}