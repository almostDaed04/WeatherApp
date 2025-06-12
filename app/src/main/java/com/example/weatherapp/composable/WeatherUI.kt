package com.example.weatherapp.composable

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.toSize
import com.example.weatherapp.NetworkResponse
import com.example.weatherapp.R
import com.example.weatherapp.WeatherViewModel
import com.example.weatherapp.model.WeatherModel
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun WeatherUI(viewModel: WeatherViewModel) {

    val weatherData = viewModel.weatherData.observeAsState()
    val keyBoardController = LocalSoftwareKeyboardController.current

    var cityName by remember { mutableStateOf("") }

    val currentWeather = weatherData.value
    val weatherCondition = when (currentWeather) {
        is NetworkResponse.Success -> getWeatherConditionFromCode((currentWeather.data).current.weather_code)
        else -> "default"
    }
    var size by remember {
        mutableStateOf(Size.Zero)
    }
    val isRefreshing = remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }
    var showContent by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()
    var isExpanded by remember { mutableStateOf(false) }
    val focusRequester = remember { FocusRequester() }

    LaunchedEffect(isExpanded) {
        if (isExpanded) {
            delay(100)
            focusRequester.requestFocus()
            keyBoardController?.show()
        }
    }
    LaunchedEffect(isLoading) {
        if (isLoading) {
            delay(1500)
            isLoading = false
        }
    }
    LaunchedEffect(cityName) {
        if (cityName.isNotEmpty() && cityName.length >= 2) {
            delay(200) // Debounce search
            viewModel.onSearchQueryChanged(cityName)
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .alpha(0.8f)
            .onSizeChanged { size = it.toSize() }
            .background(getAnimatedBackground(description = weatherCondition, size))

    ) {
        SwipeRefresh(
            state = rememberSwipeRefreshState(isRefreshing.value),
            onRefresh = {
                isRefreshing.value = true

                // 🔄 Trigger your API call
                viewModel.getData(cityName.ifEmpty { "" })  // default fallback city

                // ✅ You can wait for network to complete then stop refreshing
                // but here we just delay for UI feedback
                coroutineScope.launch {
                    delay(1500) // simulate loading
                    isRefreshing.value = false
                }
            }
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(10.dp)
            ) {

                Spacer(modifier = Modifier.height(30.dp))
                Box(
                    contentAlignment = Alignment.CenterEnd,
                    modifier = Modifier.fillMaxWidth()

                ) {
                    if (!isExpanded) {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .clip(CircleShape)
                                .background(Color.Gray.copy(alpha = 0.3f))
                                .padding(5.dp)
                                .size(40.dp)
                                .clickable {
                                    isExpanded = !isExpanded
                                }
                        ) {
                            Icon(
                                painter = painterResource(R.drawable.baseline_search_24),
                                contentDescription = "expand text",
                            )

                        }
                    }}
                Column(
                    horizontalAlignment = Alignment.End,
                    modifier = Modifier.fillMaxWidth()
                ){
                    androidx.compose.animation.AnimatedVisibility(
                        visible = isExpanded,
                        enter = fadeIn() + expandHorizontally(
                            tween(200),
                            expandFrom = Alignment.End
                        ),
                        exit = fadeOut() + shrinkHorizontally(
                            tween(200),
                            shrinkTowards = Alignment.End
                        )
                    ) {
                        Column {
                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(Color.Gray.copy(alpha = 0.3f))
                                    .fillMaxWidth()
                            ) {
                                Row {
                                    Icon(
                                        painter = painterResource(R.drawable.baseline_search_24),
                                        contentDescription = "expand textField",
                                        modifier = Modifier
                                            .padding(start = 10.dp)
                                            .align(Alignment.CenterVertically)
                                    )
                                    TextField(
                                        value = cityName,
                                        onValueChange = {
                                            cityName = it
                                            viewModel.onSearchQueryChanged(cityName = it)
                                        },
                                        label = {
                                            Text(
                                                text = "Enter City Name",
                                                fontWeight = FontWeight.Bold,
                                                color = Color.Black
                                            )
                                        },
                                        modifier = Modifier.focusRequester(focusRequester),
                                        colors = TextFieldDefaults.colors(
                                            focusedTextColor = Color.Black,
                                            unfocusedTextColor = Color.Black,
                                            focusedContainerColor = Color.Transparent,
                                            unfocusedContainerColor = Color.Transparent,
                                            focusedIndicatorColor = Color.Transparent,
                                            unfocusedIndicatorColor = Color.Transparent,
                                        ),
                                        singleLine = true,
                                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                                        keyboardActions = KeyboardActions(
                                            onSearch = {
                                                viewModel.getData(cityName)
                                                keyBoardController?.hide()
                                                isLoading = true
                                                isExpanded = false
                                                viewModel.onSearchQueryChanged("")
                                            }
                                        ),
                                    )
                                    if (cityName.isNotEmpty()) {
                                        Icon(
                                            painter = painterResource(R.drawable.baseline_clear_24),
                                            contentDescription = "clear",
                                            modifier = Modifier
                                                .align(Alignment.CenterVertically)
                                                .padding(end = 10.dp)
                                                .clickable {
                                                    cityName = ""
                                                    keyBoardController?.show()
                                                    viewModel.onSearchQueryChanged("")
                                                }
                                        )
                                    } else {
                                        Icon(
                                            painter = painterResource(R.drawable.baseline_clear_24),
                                            contentDescription = "clear",
                                            tint = Color.Gray,
                                            modifier = Modifier
                                                .align(Alignment.CenterVertically)
                                                .padding(end = 10.dp)
                                                .clickable {
                                                    isExpanded = !isExpanded
                                                }


                                        )

                                    }
                                }
                            }

                        }

                    }
                    if (isExpanded && viewModel.suggestions.isNotEmpty()) {
                        AnimatedVisibility(
                            visible = isExpanded,
                            enter = fadeIn() + expandVertically(
                               tween(200),
                                expandFrom = Alignment.Top
                            ),
                            exit = fadeOut() + shrinkVertically(
                                tween(200),
                                shrinkTowards = Alignment.Top
                            )
                        ){
                            Box(
                                modifier = Modifier
                                    .background(Color.LightGray.copy(alpha = 0.2f))
                                    .fillMaxWidth()
                                    .height(200.dp)
                            ) {
                                LazyColumn {
                                    items(viewModel.suggestions.take(5)) { item ->
                                        Text(
                                            text = "${item.name ?: ""}, ${item.country ?: ""}",
                                            fontSize = 15.sp,
                                            modifier = Modifier
                                                .padding(10.dp)
                                                .fillMaxWidth()
                                                .height(20.dp)
                                                .clickable {
                                                    cityName = "${item.name}"
                                                    viewModel.getData(cityName)
                                                    viewModel.onSearchQueryChanged("")
                                                    isLoading = true
                                                    isExpanded = false
                                                    keyBoardController?.hide()
                                                    showContent = true
                                                })
                                        HorizontalDivider(
                                            modifier = Modifier
                                                .padding(20.dp, 5.dp)
                                                .fillMaxWidth(),
                                            thickness = 1.dp
                                        )
                                    }

                                }
                            }
                        }
                    }
                }

                    Spacer(modifier = Modifier.height(10.dp))
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                    ){
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .verticalScroll(rememberScrollState())
                        ) {
                            if(isLoading){
                                ShimmerEffect()
                            }
                            if (showContent) {

                                when (val result = weatherData.value) {
                                    is NetworkResponse.Error -> {
                                        Text(text = "Something went wrong")
                                    }

                                    NetworkResponse.Loading -> {
                                        ShimmerEffect()
                                        showContent = true
                                    }

                                    is NetworkResponse.Success<*> -> {
                                        val geo = viewModel.geoData.value
                                        val model = result.data as? WeatherModel

                                        if (geo != null && model != null) {
                                            Column {
                                                CurrentWeatherUI(data = model, geoData = geo)
                                                Spacer(modifier = Modifier.height(20.dp))
                                                HourlyWeatherUI(data = model)
                                                DailyForecastUI(data = model.daily)
                                            }
                                        } else {
                                            Text("Location data not available", color = Color.White)
                                        }
                                    }

                                    null -> {}
                                }
                            }
                        }
                    }
                }



        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview
@Composable
fun WeatherUiPreview() {
    WeatherUI(viewModel = WeatherViewModel())

}