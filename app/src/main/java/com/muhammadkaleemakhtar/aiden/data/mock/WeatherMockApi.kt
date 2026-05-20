package com.muhammadkaleemakhtar.aiden.data.mock

object WeatherMockApi {
    fun getData(location: String): WeatherResponse {
        return WeatherResponse(
            rainfall = "heavy",
            alert = "flood_warning",
            temperature = 28,
            timestamp = System.currentTimeMillis()
        )
    }
}

data class WeatherResponse(
    val rainfall: String,
    val alert: String,
    val temperature: Int,
    val timestamp: Long
)
