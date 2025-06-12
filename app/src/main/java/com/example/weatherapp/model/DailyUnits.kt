package com.example.weatherapp.model

data class DailyUnits(
    val apparent_temperature_mean: String,
    val sunrise: String,
    val sunset: String,
    val temperature_2m_max: String,
    val temperature_2m_mean: String,
    val temperature_2m_min: String,
    val time: String,
    val uv_index_max: String,
    val visibility_mean: String,
    val weather_code: String
)