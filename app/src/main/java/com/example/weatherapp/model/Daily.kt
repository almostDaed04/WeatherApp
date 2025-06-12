package com.example.weatherapp.model

data class Daily(
    val apparent_temperature_mean: List<Double>,
    val sunrise: List<String>,
    val sunset: List<String>,
    val temperature_2m_max: List<Double>,
    val temperature_2m_mean: List<Double>,
    val temperature_2m_min: List<Double>,
    val time: List<String>,
    val uv_index_max: List<Double>,
    val visibility_mean: List<Double>,
    val weather_code: List<Int>
)