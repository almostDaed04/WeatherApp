package com.example.weatherapp.model

data class Hourly(
    val rain: List<Double>,
    val temperature_2m: List<Double>,
    val time: List<String>,
    val weather_code: List<Int>
)