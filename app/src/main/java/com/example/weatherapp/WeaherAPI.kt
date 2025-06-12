package com.example.weatherapp

import com.example.weatherapp.model.WeatherModel
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherAPI {

    @GET("v1/forecast")
    suspend fun getWeatherForecast(
        @Query("latitude") lat: Double,
        @Query("longitude") lon: Double,
        @Query("timezone") timezone: String = "auto",
        @Query("current") current: String = "temperature_2m,relative_humidity_2m,apparent_temperature,is_day,rain,snowfall,wind_speed_10m,surface_pressure,weather_code,cloud_cover,wind_direction_10m",
        @Query("daily") daily: String = "temperature_2m_max,temperature_2m_min,uv_index_max,visibility_mean,temperature_2m_mean,apparent_temperature_mean,sunrise,sunset,rain_sum,weather_code",
        @Query("hourly") hourly: String = "temperature_2m,weather_code,rain",
        @Query("wind_speed_unit") windSpeedUnit: String = "mph"
    ): Response<WeatherModel>

}