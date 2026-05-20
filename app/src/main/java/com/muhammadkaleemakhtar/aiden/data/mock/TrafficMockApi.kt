package com.muhammadkaleemakhtar.aiden.data.mock

object TrafficMockApi {
    fun getData(location: String): TrafficResponse {
        return TrafficResponse(
            congestionLevel = 0.85,
            blockedRoads = listOf("Main Street", "G-10 Sector Road"),
            affectedVehicles = 45
        )
    }
}

data class TrafficResponse(
    val congestionLevel: Double,
    val blockedRoads: List<String>,
    val affectedVehicles: Int
)
