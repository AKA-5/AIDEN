package com.muhammadkaleemakhtar.aiden.data.models

data class DetectionResult(
    val isCrisis: Boolean,
    val crisisType: String, // "Urban Flooding", "None", etc.
    val confidence: Float, // 0.0 to 1.0
    val location: String,
    val severity: String, // "Low", "Medium", "High"
    val impact: List<String>,
    val reasoning: String
)
