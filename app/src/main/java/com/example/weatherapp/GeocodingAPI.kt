package com.example.weatherapp

import com.example.weatherapp.model.GeocodingModel
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface GeocodingAPI {
    @GET("v1/search")
    suspend fun getCoordinatesByCity(
        @Query("name") cityName: String,
    ): Response<GeocodingModel>
}