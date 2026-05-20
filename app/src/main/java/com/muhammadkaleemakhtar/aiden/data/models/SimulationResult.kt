package com.muhammadkaleemakhtar.aiden.data.models

data class SimulationResult(
    val actionId: String,
    val beforeState: CrisisState,
    val afterState: CrisisState,
    val success: Boolean,
    val message: String
)
