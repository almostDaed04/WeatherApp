package com.example.weatherapp

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.model.GeoResult
import com.example.weatherapp.model.GeocodingModel
import com.example.weatherapp.model.WeatherModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class WeatherViewModel : ViewModel() {

    private val weatherAPI = RetrofitInstance.weatherAPI

    private var _weatherData = MutableLiveData<NetworkResponse<WeatherModel>>()
    val weatherData: LiveData<NetworkResponse<WeatherModel>> = _weatherData

    private val _geoData = MutableLiveData<GeocodingModel>()
    val geoData: LiveData<GeocodingModel> = _geoData

    var suggestions by mutableStateOf<List<GeoResult>>(emptyList())
        private set
    private var searchJob: Job? = null

    fun onSearchQueryChanged(cityName: String) {
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            delay(300)
            if (cityName.isNotEmpty()) {
                try {
                    val response = RetrofitInstance.geocodingAPI.getCoordinatesByCity(cityName)
                    suggestions = if (response.isSuccessful) {
                        response.body()?.results.orEmpty().mapNotNull { result ->
                            val name = result.name
                            val country = result.country
                            if (!name.isNullOrBlank() && !country.isNullOrBlank()) {
                                GeoResult(name, country)
                            }else null
                        }
                    } else {
                        emptyList()
                    }

                } catch (e: Exception) {
                    suggestions = emptyList()
                }
            } else {
                suggestions = emptyList()
            }
        }

    }

    fun getData(cityName: String) {
        _weatherData.value = NetworkResponse.Loading

        viewModelScope.launch {
            try {
                val geoResponse = RetrofitInstance.geocodingAPI.getCoordinatesByCity(cityName)

                if (geoResponse.isSuccessful) {
                    val geoBody = geoResponse.body()

                    // ✅ Post to LiveData only if geoBody is not null
                    geoBody?.let { _geoData.postValue(it) }

                    val location = geoBody?.results?.firstOrNull()
                    if (location != null) {
                        val lat = location.latitude
                        val lon = location.longitude

                        val response = weatherAPI.getWeatherForecast(lat, lon)

                        if (response.isSuccessful) {
                            response.body()?.let {
                                _weatherData.value = NetworkResponse.Success(it)
                            } ?: run {
                                _weatherData.value = NetworkResponse.Error("Empty weather response")
                            }
                        } else {
                            _weatherData.value =
                                NetworkResponse.Error("Failed To Load Weather: ${response.message()}")
                        }
                    } else {
                        _weatherData.value = NetworkResponse.Error("City not found")
                    }
                } else {
                    _weatherData.value =
                        NetworkResponse.Error("Failed To Load Location: ${geoResponse.message()}")
                }
            } catch (e: Exception) {
                _weatherData.value = NetworkResponse.Error("Something went wrong: ${e.message}")
            }
        }
    }
}

