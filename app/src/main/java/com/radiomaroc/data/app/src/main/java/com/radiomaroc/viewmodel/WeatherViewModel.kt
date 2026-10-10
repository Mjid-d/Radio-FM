package com.radiomaroc.viewmodel

import android.Manifest
import android.annotation.SuppressLint
import android.app.Application
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.location.LocationServices
import com.radiomaroc.data.WeatherData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class WeatherViewModel(app: Application) : AndroidViewModel(app) {

    private val fusedLocationClient = LocationServices.getFusedLocationProviderClient(app)
    private val _weather = MutableStateFlow<WeatherData?>(null)
    val weather: StateFlow<WeatherData?> = _weather

    fun hasLocationPermission(): Boolean {
        val context = getApplication<Application>()
        return ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
    }

    @SuppressLint("MissingPermission")
    fun fetchWeather() {
        if (!hasLocationPermission()) return

        fusedLocationClient.lastLocation.addOnSuccessListener { location ->
            if (location != null) {
                viewModelScope.launch {
                    try {
                        val data = withContext(Dispatchers.IO) {
                            fetchWeatherData(location.latitude, location.longitude)
                        }
                        _weather.value = data
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }
        }
    }

    private fun fetchWeatherData(lat: Double, lon: Double): WeatherData {
        val weatherUrl = "https://api.open-meteo.com/v1/forecast?latitude=$lat&longitude=$lon&current=temperature_2m,weather_code&timezone=auto"
        val weatherJson = JSONObject(httpGet(weatherUrl))
        val current = weatherJson.getJSONObject("current")
        val temp = current.getDouble("temperature_2m").toInt()
        val code = current.getInt("weather_code")

        val cityUrl = "https://api.bigdatacloud.net/data/reverse-geocode-client?latitude=$lat&longitude=$lon&localityLanguage=ar"
        val cityJson = JSONObject(httpGet(cityUrl))
        val city = cityJson.optString("city").ifEmpty { cityJson.optString("locality").ifEmpty { cityJson.optString("principalSubdivision") } }

        val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
        val currentTime = timeFormat.format(Date())

        return WeatherData(
            cityName = city.ifEmpty { "Unknown" },
            temperature = temp,
            weatherDescription = weatherCodeToString(code),
            weatherEmoji = weatherCodeToEmoji(code),
            currentTime = currentTime
        )
    }

    private fun httpGet(url: String): String {
        val connection = URL(url).openConnection() as HttpURLConnection
        connection.requestMethod = "GET"
        connection.connectTimeout = 10000
        connection.readTimeout = 10000
        return connection.inputStream.bufferedReader().use { it.readText() }
    }

    private fun weatherCodeToString(code: Int): String = when (code) {
        0 -> "صافٍ"
        1, 2 -> "غائم جزئياً"
        3 -> "غائم"
        45, 48 -> "ضباب"
        51, 53, 55 -> "رذاذ"
        61, 63, 65 -> "مطر"
        71, 73, 75 -> "ثلج"
        80, 81, 82 -> "زخات مطر"
        95, 96, 99 -> "عاصفة"
        else -> "غير معروف"
    }

    private fun weatherCodeToEmoji(code: Int): String = when (code) {
        0 -> "☀️"
        1, 2 -> "⛅"
        3 -> "☁️"
        45, 48 -> "🌫️"
        51, 53, 55, 61, 63, 65, 80, 81, 82 -> "🌧️"
        71, 73, 75 -> "❄️"
        95, 96, 99 -> "⛈️"
        else -> "🌡️"
    }
}
