package com.example.weatherapp.model

data class GeocodingModel(
    val results: List<GeocodingResult>
)

data class GeocodingResult(
    val id: Int?,
    val name: String?,
    val latitude: Double,
    val longitude: Double,
    val elevation: Double?,
    val feature_code: String?,
    val country_code: String?,
    val admin1_id: Int?,
    val admin2_id: Int?,
    val admin3_id: Int?,
    val admin4_id: Int?,
    val timezone: String?,
    val population: Int?,
    val postcodes: List<String>?,
    val country_id: Int?,
    val country: String?,
    val admin1: String?,
    val admin2: String?,
    val admin3: String?,
    val admin4: String?
)

data class GeoResult(
        val name: String?,
    val country: String?
        )