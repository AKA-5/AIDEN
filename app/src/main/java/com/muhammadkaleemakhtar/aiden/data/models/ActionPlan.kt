package com.muhammadkaleemakhtar.aiden.data.models

data class ActionPlan(
    val actions: List<Action>,
    val priority: String
)

data class Action(
    val id: String,
    val name: String, // "Traffic Rerouting", "Emergency Dispatch", "Public Alerts"
    val description: String,
    val isSimulated: Boolean = false
)
