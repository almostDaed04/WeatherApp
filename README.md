# ☁️ Weather App

A modern, sleek **Android Weather App** built with **Jetpack Compose** and powered by **Retrofit** for live weather data. It features a minimal UI, city-based search, and smooth UX transitions — everything you need in a daily weather companion.

---

## 📱 Features

- 🔍 **Search by city** – Get weather data for any location
- 🌡️ **Current temperature, humidity, wind speed & direction**
- 🧩 **Jetpack Compose UI** – Built entirely with modern UI toolkit
- ⚡ **Retrofit-based REST API integration**
- 💥 **Shimmer effect** for loading screens
- 🛑 **Error handling** for invalid city or network failure
- 🌈 Adaptive icons and weather animations *(Lottie supported)*
---
  ## 🔧 Tech Stack

| Layer           | Library / Tool                     |
|----------------|------------------------------------|
| UI             | Jetpack Compose, Material 3        |
| State          | `mutableStateOf`, `ViewModel`      |
| Network        | Retrofit, Gson                     |
| Architecture   | MVVM (Model-View-ViewModel)        |
| Tools          | Android Studio, Kotlin DSL         |

---

## 🌐 API Reference

### 🛰️ Open-Meteo Weather API('https://api.open-meteo.com')
- Endpoint: `/v1/forecast`
- Parameters: `latitude`, `longitude`, `current_weather=true`, etc.
- Response: Temperature, wind speed, wind direction, weather code

### 🗺️ Open-Meteo Geocoding API('https://geocoding-api.open-meteo.com')
- Endpoint: `/v1/search`
- Parameters: `name`, `count`
- Response: City name, latitude, longitude, etc.

✅ **No API key required** for either service

---

🎨 Screenshots
<img src="screenshots/home.png" alt="Home Screen" width="300"/>

