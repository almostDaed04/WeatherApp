package com.example.weatherapp

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {
    //https://api.open-meteo.com/v1/forecast?latitude=52.52&longitude=13.41&daily=temperature_2m_max,temperature_2m_min,uv_index_max,visibility_mean,temperature_2m_mean,apparent_temperature_mean,sunrise,sunset&hourly=temperature_2m,weather_code,rain&current=temperature_2m,relative_humidity_2m,apparent_temperature,is_day,rain,snowfall,wind_speed_10m&wind_speed_unit=mph
    //https://geocoding-api.open-meteo.com/v1/search?name={city_name}
    private const val baseURL = "https://api.open-meteo.com/"

    private fun getWeather():Retrofit{
        return Retrofit.Builder()
            .baseUrl(baseURL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
    val weatherAPI: WeatherAPI = getWeather().create(WeatherAPI::class.java)

    private const val geocodingBaseURL = "https://geocoding-api.open-meteo.com/"

    private fun getGeocodingRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(geocodingBaseURL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
    val geocodingAPI: GeocodingAPI = getGeocodingRetrofit().create(GeocodingAPI::class.java)


}