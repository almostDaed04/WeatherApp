package com.example.weatherapp

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.ViewModelProvider
import com.example.weatherapp.composable.WeatherUI
import com.example.weatherapp.ui.theme.WeatherAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val viewModel = ViewModelProvider(this)[WeatherViewModel::class.java]
        try{
        setContent {
            WeatherAppTheme {
                WeatherUI(viewModel = viewModel)
            }
        }
        }catch(e:Exception){
            Log.d("Error",e.message.toString())
        }
}
}
