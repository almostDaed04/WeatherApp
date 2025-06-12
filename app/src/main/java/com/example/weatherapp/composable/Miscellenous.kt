package com.example.weatherapp.composable

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import com.example.weatherapp.model.WeatherModel
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import kotlin.math.pow
import kotlin.math.roundToInt


fun calculateFeelsLikeApprox(data: WeatherModel): Int {
    val tempC = data.current.temperature_2m
    val windSpeedKmph = data.current.wind_speed_10m * 3.6f

    val heatIndex = (-8.784695f +
            1.6113942f * tempC +
            2.338549f * data.current.relative_humidity_2m -
            0.14611605f * tempC * data.current.relative_humidity_2m -
            0.012308094f * tempC * tempC -
            0.016424827f * data.current.relative_humidity_2m * data.current.relative_humidity_2m +
            0.002211732f * tempC * tempC * data.current.relative_humidity_2m +
            0.00072546f * tempC * data.current.relative_humidity_2m * data.current.relative_humidity_2m -
            0.000003582f * tempC * tempC * data.current.relative_humidity_2m * data.current.relative_humidity_2m)

    val windChill = (13.12f +
            0.6215f * tempC -
            11.37f * windSpeedKmph.pow(0.16) +
            0.3965f * tempC * windSpeedKmph.pow(0.16))

    return when {
        tempC >= 27 && data.current.relative_humidity_2m >= 40 -> heatIndex.roundToInt()
        tempC <= 10 && windSpeedKmph >= 4.8 -> windChill.roundToInt()
        else -> tempC.roundToInt() // Use actual temperature if not extreme
    }
}

@Composable
fun getWeatherConditionFromCode(code: Int): String {
    return when (code) {
        0 -> "clear"
        1, 2, 3 -> "partly cloudy"
        45, 48 -> "foggy"
        in 51..57 -> "drizzle"
        in 61..67 -> "rainy"
        in 71..77 -> "snowy"
        in 80..82 -> "showers"
        in 95..99 -> "stormy"
        else -> "default"
    }
}


@RequiresApi(Build.VERSION_CODES.O)
fun formatDayOfWeek(dateString: String): String {
    return try {
        val formatter = DateTimeFormatter.ISO_LOCAL_DATE
        val date = LocalDate.parse(dateString, formatter)
        date.dayOfWeek.toString()
            .substring(0, 3)
            .lowercase()
            .replaceFirstChar { it.uppercase() }
    } catch (e: Exception) {
        Log.e("DateFormat", "Error formatting date: $dateString", e)
        dateString // Fallback to raw string if parsing fails
    }
}

@RequiresApi(Build.VERSION_CODES.O)
fun formatTime(timeString: String): String {
    return try {
        val formatter = DateTimeFormatter.ISO_LOCAL_TIME
        val time = LocalTime.parse(timeString, formatter)
        time.format(DateTimeFormatter.ofPattern("h:mm a"))
    } catch (e: Exception) {
        Log.e("TimeFormat", "Error formatting time: $timeString", e)
        timeString // Fallback to raw string if parsing fails
    }
}

fun getWindDirectionText(degrees: Int): String {
    return when ((degrees % 360 + 22) / 45) {
        0 -> "N"
        1 -> "NE"
        2 -> "E"
        3 -> "SE"
        4 -> "S"
        5 -> "SW"
        6 -> "W"
        7 -> "NW"
        else -> "N"
    }
}
