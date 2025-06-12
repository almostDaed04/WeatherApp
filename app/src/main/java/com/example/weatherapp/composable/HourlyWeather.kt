package com.example.weatherapp.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.weatherapp.model.WeatherModel
import kotlin.math.roundToInt


@Composable
fun HourlyWeatherUI(data: WeatherModel) {
    val filterData = remember(data) { getHourlyFromCurrent(data) }
    Column{
        Box(
            modifier = Modifier
                .padding(3.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(Color.Gray.copy(alpha = 0.5f))
                .fillMaxWidth()
                .height(100.dp)
        ) {
            LazyRow {
                items(filterData) { (time, temperature, weatherCode) ->
                    HourlyWeatherItem(
                        time = time.toString(),
                        temperature = temperature,
                        weatherCode = weatherCode
                    )
                }
            }
        }
    }

}

@Composable
fun HourlyWeatherItem(time: String, temperature: Int, weatherCode: Int) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly,
        modifier = Modifier
            .padding(8.dp)
    ) {
        Text(text = time)

        WeatherAnimation(
            description = getWeatherConditionFromCode(weatherCode), modifier = Modifier
                .size(30.dp)
                .padding(3.dp)
        )
        Text(text = "${temperature}°")
    }
}

fun getHourlyFromCurrent(data: WeatherModel): List<Triple<CharSequence, Int, Int>> {
    val currentTime = (data.current.time)
    return data.hourly.time.indices.mapNotNull { i ->
        val time = (data.hourly.time[i])
        if (time >= currentTime) {
            val formatedTime = time.subSequence(11, 16)
            Triple(
                formatedTime,
                (data.hourly.temperature_2m[i]).roundToInt(),
                data.hourly.weather_code[i]
            )
        } else null
    }
}
