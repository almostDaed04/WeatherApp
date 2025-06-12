package com.example.weatherapp.composable

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.weatherapp.R
import com.example.weatherapp.model.GeocodingModel
import com.example.weatherapp.model.WeatherModel
import kotlin.math.roundToInt


@Composable
fun CurrentWeatherUI(data: WeatherModel, geoData: GeocodingModel) {

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            WeatherAnimation(
                description = getWeatherConditionFromCode(data.current.weather_code),
                modifier = Modifier.size(200.dp)
            )
            Spacer(modifier = Modifier.height(5.dp))
            Row {
                Image(
                    painter = painterResource(R.drawable.outline_location_on_24),
                    contentDescription = "location"
                )
                Text(
                    text = "${geoData.results.firstOrNull()?.name},",
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                )
                Text(
                    text = geoData.results.firstOrNull()?.country_code ?: "Unknown country",
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                )
            }

            Text(
                text = "${(data.current.temperature_2m).roundToInt()}°C",
                fontWeight = FontWeight.SemiBold,
                fontSize = 60.sp,
                color = Color.White
            )
            Text(text = getWeatherConditionFromCode(data.current.weather_code), color = Color.White)
            Row(
                modifier = Modifier
                    .padding(10.dp)
            ) {
                CurrentWeatherCard(
                    icon = { AnimatedIconBox("wee.json", modifier = Modifier.size(30.dp)) },
                    text = "Air Pressure",
                    data = data.current.surface_pressure.roundToInt(),
                    "hPa"
                )
                CurrentWeatherCard(
                    icon = { AnimatedIconBox("qwer.json", modifier = Modifier.size(30.dp)) },
                    text = "Feels like",
                    data = calculateFeelsLikeApprox(data),
                    "°C"
                )
                CurrentWeatherCard(
                    icon = {
                        AnimatedIconBox(
                            "humidity_icon.json",
                            modifier = Modifier.size(30.dp)
                        )
                    },
                    text = "Humidity",
                    data = data.current.relative_humidity_2m,
                    "%"
                )
            }
            Row {
                CurrentWeatherCard(
                    icon = { AnimatedIconBox("wind_icon.json", modifier = Modifier.size(30.dp)) },
                    text = "${getWindDirectionText(data.current.wind_direction_10m)} Wind",
                    data = data.current.wind_speed_10m.roundToInt(),
                    "mph"
                )
                CurrentWeatherCard(
                    icon = { AnimatedIconBox("clouds_icon.json", modifier = Modifier.size(30.dp)) },
                    text = "Clouds",
                    data = data.current.cloud_cover,
                    ""
                )
                CurrentWeatherCard(
                    icon = { AnimatedIconBox("umbrella.json", modifier = Modifier.size(30.dp)) },
                    text = "Rain",
                    data = (data.current.rain),
                    "mm"
                )

            }


        }
    }



@Composable
fun CurrentWeatherCard(icon: @Composable () -> Unit, text: String, data: Any, unit: String) {

    Box(
        modifier = Modifier
            .padding(3.dp)
            .size(106.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(Color.Gray.copy(alpha = 0.5f))
            .padding(7.dp)
    )
    {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(3.dp)
        ) {
            icon()
            Spacer(modifier = Modifier.height(5.dp))
            Text(text = text, fontWeight = FontWeight.Bold, color = Color.White, fontSize = 12.sp)
            Spacer(modifier = Modifier.height(5.dp))
            Text(
                text = "$data $unit",
                fontWeight = FontWeight.Bold,
                color = Color.White,
                fontSize = 20.sp
            )
        }
    }
}
