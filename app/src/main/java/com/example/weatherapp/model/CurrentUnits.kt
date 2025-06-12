package com.example.weatherapp.model

data class CurrentUnits(
    val apparent_temperature: String,
    val interval: String,
    val is_day: String,
    val rain: String,
    val relative_humidity_2m: String,
    val snowfall: String,
    val temperature_2m: String,
    val time: String,
    val wind_speed_10m: String,
    val weather_code: String,
    val winddirection_10m: String,
    val surface_pressure: String,
    val cloud_cover: String,
    val precipitation: String
)