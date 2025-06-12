package com.example.weatherapp.composable

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.paddingFrom
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.focusModifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition


@Composable
fun getAnimatedBackground(description: String, size: Size): Brush {
    val infiniteTransition = rememberInfiniteTransition(label = "background_animation")

    val offset by infiniteTransition.animateFloat(
        initialValue = -1f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(4000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "offset"
    )


    val sunnyColorList = listOf(
        Color(0xFFFFF8E1), // Light Cream
        Color(0xFFFFECB3), // Warm Light Yellow
        Color(0xFFFFD54F), // Rich Sunlight Yellow
        Color(0xFFFFB300), // Deeper Amber
        Color(0xFFFFF8E1)  // Light Cream again
    )


    val cloudyColorList = listOf(
        Color(0xFFECEFF1), // Very Light Gray-Blue
        Color(0xFFCFD8DC), // Cool Cloud Gray
        Color(0xFF90A4AE), // Muted Steel Blue
        Color(0xFFB0BEC5), // Softer Gray-Blue
        Color(0xFFECEFF1)  // Light tone again
    )


    val rainyColorList = listOf(
        Color(0xFFE3F2FD), // Light Rain Mist
        Color(0xFF64B5F6), // Mid Rainy Blue
        Color(0xFF1976D2), // Deep Sky Blue
        Color(0xFF455A64), // Stormy Gray-Blue
        Color(0xFFE3F2FD)  // Return to calm
    )


    var defaultColorList = listOf(
        Color(0xFFF5F5F5), // Soft White-Gray
        Color(0xFFE0E0E0)  // Light Neutral Gray
    )
    val isDark = isSystemInDarkTheme()
    if (isDark) {
        defaultColorList = listOf(
            Color(0xFF212121), // Soft White-Gray
            Color(0xFF121212)  // Light Neutral Gray
        )
    }


    return when {
        description.contains("clear", ignoreCase = true) -> {
            Brush.linearGradient(
                colors = sunnyColorList,
                start = Offset(offset * size.width, 0f),
                end = Offset((offset + 1f) * size.width, size.height)
            )
        }

        description.contains("cloud", ignoreCase = true) -> {
            Brush.linearGradient(
                colors = cloudyColorList,
                start = Offset(offset * size.width, 0f),
                end = Offset((offset + 1f) * size.width, size.height)
            )
        }

        description.contains("rain", ignoreCase = true) -> {
            Brush.linearGradient(
                colors = rainyColorList,
                start = Offset(0f, offset * size.height),
                end = Offset(size.width, (offset + 1f) * size.height)
            )
        }

        else -> {
            Brush.linearGradient(
                colors = defaultColorList,
                start = Offset(offset * size.width, offset * size.height),
                end = Offset((offset + 1f) * size.width, (offset + 1f) * size.height)
            )
        }
    }
}

@Composable
fun WeatherAnimation(description: String, modifier: Modifier = Modifier) {
    val animationFile = when {
        description.contains("clear", ignoreCase = true) -> "sunny_icon.json"
        description.contains("partly cloudy", ignoreCase = true) -> "rain_icon.json"
        description.contains("cloudy", ignoreCase = true) -> "rain_icon.json"
        description.contains("rain", ignoreCase = true) -> "rain_icon.json"
        description.contains("drizzle", ignoreCase = true) -> "rain_icon.json"
        description.contains("showers", ignoreCase = true) -> "rain_icon.json"
        description.contains("snow", ignoreCase = true) -> "rain_icon.json"
        description.contains("storm", ignoreCase = true) -> "storm_icon.json"
        description.contains("fog", ignoreCase = true) || description.contains(
            "foggy",
            ignoreCase = true
        ) -> "rain_icon.json"

        else -> "intro_icon.json"
    }

    val composition by rememberLottieComposition(LottieCompositionSpec.Asset(animationFile))
    val progress by animateLottieCompositionAsState(
        composition = composition,
        iterations = LottieConstants.IterateForever
    )

    if (composition != null) {
        LottieAnimation(
            composition = composition,
            progress = progress,
            modifier = modifier
        )
    }
}

@Composable
fun AnimatedIconBox(
    assetName: String, modifier: Modifier = Modifier
) {
    val composition by rememberLottieComposition(LottieCompositionSpec.Asset(assetName))
    val progress by animateLottieCompositionAsState(
        composition,
        iterations = LottieConstants.IterateForever
    )

    if (composition != null) {
        LottieAnimation(
            composition = composition,
            progress = progress,
            modifier = modifier
        )
    } else {
        Box(
            modifier = modifier,
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "☀️", // Fallback emoji or icon
                fontSize = 24.sp
            )
        }
    }
}

@Composable
fun ShimmerEffect() {
    val shimmerColors = listOf(
        Color.LightGray.copy(alpha = 0.6f),
        Color.LightGray.copy(alpha = 0.2f),
        Color.LightGray.copy(alpha = 0.6f)
    )

    val transition = rememberInfiniteTransition(label = "shimmer_animation")
    val translateAnim by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 1000,
                easing = FastOutSlowInEasing
            )
        )
    )
    val brush = Brush.linearGradient(
        shimmerColors,
        start = Offset.Zero,
        end = Offset(translateAnim, translateAnim)
    )
    ShimmerEffectItems(brush)
}

@Composable
fun ShimmerEffectItems(brush: Brush) {
    Box(
        modifier = Modifier
            .fillMaxSize()

    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
        ) {
            Spacer(modifier = Modifier.height(20.dp))
            Card(
                modifier = Modifier
                    .alpha(0.6f)

            ){
                Box(
                    modifier = Modifier
                        .size(200.dp)
                        .background(brush)

                )
            }
            Spacer(modifier = Modifier.height(125.dp))
            Card(
                modifier = Modifier
                    .alpha(0.6f)

            ) {
                Box(
                    modifier = Modifier
                        .size(300.dp, 30.dp)
                        .background(brush)
                )
            }

            Spacer(modifier = Modifier.height(10.dp))
            Row(
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Card(
                    modifier = Modifier
                        .padding(5.dp)
                        .alpha(0.6f)
                ) {
                    Box(
                        modifier = Modifier
                            .size(104.dp)
                            .background(brush)
                    )
                }
                Card(
                    modifier = Modifier
                        .padding(5.dp)
                        .alpha(0.6f)
                ) {
                    Box(
                        modifier = Modifier
                            .size(104.dp)
                            .background(brush)
                    )
                }
                Card(
                    modifier = Modifier
                        .padding(5.dp)
                        .alpha(0.6f)
                ) {
                    Box(
                        modifier = Modifier
                            .size(104.dp)
                            .background(brush)
                    )
                }
            }
            Row(
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Card(
                    modifier = Modifier
                        .padding(5.dp)
                        .alpha(0.6f)
                ) {
                    Box(
                        modifier = Modifier
                            .size(104.dp)
                            .background(brush)
                    )
                }
                Card(
                    modifier = Modifier
                        .padding(5.dp)
                        .alpha(0.6f)

                ) {
                    Box(
                        modifier = Modifier
                            .size(104.dp)
                            .background(brush)
                    )
                }
                Card(
                    modifier = Modifier
                        .padding(5.dp)
                        .alpha(0.6f)
                ) {
                    Box(
                        modifier = Modifier
                            .size(104.dp)
                            .background(brush)
                    )
                }
            }

        }

    }
}


@Preview
@Composable
fun Preview() {
    ShimmerEffect()
}