package com.muhammadkaleemakhtar.aiden.data.models

data class CrisisInput(
    val rawText: String,
    val weatherEnabled: Boolean,
    val trafficEnabled: Boolean,
    val socialEnabled: Boolean,
    val timestamp: Long
)
