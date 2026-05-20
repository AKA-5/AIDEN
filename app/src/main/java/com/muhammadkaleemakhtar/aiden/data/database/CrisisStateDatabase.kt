package com.muhammadkaleemakhtar.aiden.data.database

import com.muhammadkaleemakhtar.aiden.data.models.Action
import com.muhammadkaleemakhtar.aiden.data.models.CrisisState
import com.muhammadkaleemakhtar.aiden.data.models.SimulationResult

object CrisisStateDatabase {
    private var _currentState = CrisisState(
        blockedRoads = listOf("Main Street", "G-10 Sector Road"),
        strandedVehicles = 45,
        teamsDispatched = 0,
        alertsSent = 0,
        responseTimeMin = 45
    )

    fun getCurrentState(): CrisisState = _currentState

    fun updateState(newState: CrisisState) {
        _currentState = newState
    }

    fun resetToBefore() {
        _currentState = CrisisState(
            blockedRoads = listOf("Main Street", "G-10 Sector Road"),
            strandedVehicles = 45,
            teamsDispatched = 0,
            alertsSent = 0,
            responseTimeMin = 45
        )
    }

    fun simulateAction(action: Action): SimulationResult {
        val before = _currentState
        val after = when (action.name) {
            "Traffic Rerouting" -> before.copy(
                blockedRoads = emptyList(),
                strandedVehicles = 0,
                responseTimeMin = (before.responseTimeMin * 0.5).toInt()
            )
            "Emergency Dispatch" -> before.copy(
                teamsDispatched = 3,
                responseTimeMin = (before.responseTimeMin * 0.7).toInt()
            )
            "Public Alerts" -> before.copy(
                alertsSent = 245
            )
            else -> before
        }
        _currentState = after
        return SimulationResult(action.id, before, after, true, "${action.name} simulated successfully")
    }
}
