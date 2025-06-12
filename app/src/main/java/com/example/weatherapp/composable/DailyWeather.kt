package com.example.weatherapp.composable

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.weatherapp.model.Daily
import kotlin.math.roundToInt


@Composable
fun DailyForecastItem(
    day: String,
    maxTemp: Double,
    minTemp: Double,
    sunrise: String,
    sunset: String,
    weatherCode: Int
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 4.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Gray.copy(alpha = 0.3f))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    text = day,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = Color.Black
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Sunrise: $sunrise",
                    fontSize = 12.sp,
                    color = Color.Black.copy(alpha = 0.8f)
                )
                Text(
                    text = "Sunset: $sunset",
                    fontSize = 12.sp,
                    color = Color.Black.copy(alpha = 0.8f)
                )
            }
            WeatherAnimation(
                description = getWeatherConditionFromCode(weatherCode),
                modifier = Modifier.size(50.dp)
            )

            Text(
                text = "${maxTemp.roundToInt()}° / ${minTemp.roundToInt()}°",
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = Color.Black
            )
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DailyForecastUI(data: Daily) {
    if (data.time.isEmpty() ||
        data.temperature_2m_max.isEmpty() ||
        data.temperature_2m_min.isEmpty() ||
        data.sunrise.isEmpty() ||
        data.sunset.isEmpty() ||
        data.weather_code.isEmpty()
    ) {
        Text("Forecast data not available", color = Color.White)
        return
    }

    // Ensure we don't exceed any array bounds
    val itemCount = listOf(
        data.time.size,
        data.temperature_2m_max.size,
        data.temperature_2m_min.size,
        data.sunrise.size,
        data.sunset.size,
        data.weather_code.size,
    ).minOrNull() ?: 0

    if (itemCount == 0) {
        Text("No forecast data available", color = Color.Black)
        return
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Text(
            text = "7-Day Forecast",
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp,
            color = Color.White,
            modifier = Modifier.padding(16.dp)
        )

        Column {
            repeat(minOf(7, itemCount)) { index ->
                DailyForecastItem(
                    day = formatDayOfWeek(data.time[index]),
                    maxTemp = data.temperature_2m_max[index],
                    minTemp = data.temperature_2m_min[index],
                    sunrise = formatTime(data.sunrise[index]).substring(11, 16),
                    sunset = formatTime(data.sunset[index]).substring(11, 16),
                    weatherCode = data.weather_code[index]
                )
            }
        }
    }
}
