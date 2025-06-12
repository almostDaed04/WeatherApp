package com.example.weatherapp.model

data class Current(
    val apparent_temperature: Double,
    val interval: Int,
    val is_day: Int,
    val rain: Double,
    val relative_humidity_2m: Int,
    val snowfall: Double,
    val temperature_2m: Double,
    val time: String,
    val wind_speed_10m: Double,
    val weather_code: Int,
    val wind_direction_10m: Int,
    val surface_pressure: Double,
    val cloud_cover: Int,
    val precipitation: Double
)