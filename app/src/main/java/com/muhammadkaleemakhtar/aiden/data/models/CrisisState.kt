package com.muhammadkaleemakhtar.aiden.data.models

data class CrisisState(
    val blockedRoads: List<String>,
    val strandedVehicles: Int,
    val teamsDispatched: Int,
    val alertsSent: Int,
    val responseTimeMin: Int
)
