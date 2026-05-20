package com.muhammadkaleemakhtar.aiden.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.muhammadkaleemakhtar.aiden.agents.AgentLogger
import com.muhammadkaleemakhtar.aiden.agents.ExecutorAgent
import com.muhammadkaleemakhtar.aiden.agents.PlannerAgent
import com.muhammadkaleemakhtar.aiden.agents.SentinelAgent
import com.muhammadkaleemakhtar.aiden.data.database.CrisisStateDatabase
import com.muhammadkaleemakhtar.aiden.data.models.Action
import com.muhammadkaleemakhtar.aiden.data.models.ActionPlan
import com.muhammadkaleemakhtar.aiden.data.models.CrisisInput
import com.muhammadkaleemakhtar.aiden.data.models.CrisisState
import com.muhammadkaleemakhtar.aiden.data.models.DetectionResult
import com.muhammadkaleemakhtar.aiden.data.models.SimulationResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AidenViewModel : ViewModel() {

    private val _rawText = MutableStateFlow("")
    val rawText: StateFlow<String> = _rawText.asStateFlow()

    private val _weatherEnabled = MutableStateFlow(true)
    val weatherEnabled: StateFlow<Boolean> = _weatherEnabled.asStateFlow()

    private val _trafficEnabled = MutableStateFlow(true)
    val trafficEnabled: StateFlow<Boolean> = _trafficEnabled.asStateFlow()

    private val _socialEnabled = MutableStateFlow(true)
    val socialEnabled: StateFlow<Boolean> = _socialEnabled.asStateFlow()

    private val _isAnalyzing = MutableStateFlow(false)
    val isAnalyzing: StateFlow<Boolean> = _isAnalyzing.asStateFlow()

    private val _detectionResult = MutableStateFlow<DetectionResult?>(null)
    val detectionResult: StateFlow<DetectionResult?> = _detectionResult.asStateFlow()

    private val _actionPlan = MutableStateFlow<ActionPlan?>(null)
    val actionPlan: StateFlow<ActionPlan?> = _actionPlan.asStateFlow()

    private val _simulationResults = MutableStateFlow<List<SimulationResult>>(emptyList())
    val simulationResults: StateFlow<List<SimulationResult>> = _simulationResults.asStateFlow()

    private val _currentCrisisState = MutableStateFlow(CrisisStateDatabase.getCurrentState())
    val currentCrisisState: StateFlow<CrisisState> = _currentCrisisState.asStateFlow()

    private val _simulatedActions = MutableStateFlow<Map<String, SimulationResult>>(emptyMap())
    val simulatedActions: StateFlow<Map<String, SimulationResult>> = _simulatedActions.asStateFlow()

    private val _agentLogs = MutableStateFlow<List<AgentLogger.LogEntry>>(emptyList())
    val agentLogs: StateFlow<List<AgentLogger.LogEntry>> = _agentLogs.asStateFlow()

    init {
        loadLogs()
    }

    fun updateInputText(text: String) {
        _rawText.value = text
    }

    fun toggleWeather(enabled: Boolean) {
        _weatherEnabled.value = enabled
    }

    fun toggleTraffic(enabled: Boolean) {
        _trafficEnabled.value = enabled
    }

    fun toggleSocial(enabled: Boolean) {
        _socialEnabled.value = enabled
    }

    fun submitInput(onDone: () -> Unit) {
        viewModelScope.launch {
            _isAnalyzing.value = true
            
            // 1. Reset Database State
            CrisisStateDatabase.resetToBefore()
            _currentCrisisState.value = CrisisStateDatabase.getCurrentState()
            _simulationResults.value = emptyList()
            _simulatedActions.value = emptyMap()
            AgentLogger.clear()

            val input = CrisisInput(
                rawText = _rawText.value,
                weatherEnabled = _weatherEnabled.value,
                trafficEnabled = _trafficEnabled.value,
                socialEnabled = _socialEnabled.value,
                timestamp = System.currentTimeMillis()
            )

            // 2. Call Sentinel Agent
            val detection = SentinelAgent.analyze(input)
            _detectionResult.value = detection

            // 3. Call Planner Agent
            if (detection.isCrisis) {
                val plan = PlannerAgent.plan(detection)
                _actionPlan.value = plan
            } else {
                _actionPlan.value = null
            }

            loadLogs()
            _isAnalyzing.value = false
            onDone()
        }
    }

    fun simulateAction(action: Action) {
        if (_simulatedActions.value.containsKey(action.id)) return

        viewModelScope.launch {
            val singlePlan = ActionPlan(listOf(action), _actionPlan.value?.priority ?: "Medium")
            val executorResults = ExecutorAgent.execute(singlePlan, _currentCrisisState.value)
            
            if (executorResults.isNotEmpty()) {
                val simResult = executorResults.first()
                val updatedMap = _simulatedActions.value.toMutableMap().apply {
                    put(action.id, simResult)
                }
                _simulatedActions.value = updatedMap
                _simulationResults.value = _simulationResults.value + simResult
                _currentCrisisState.value = CrisisStateDatabase.getCurrentState()
            }
            loadLogs()
        }
    }

    fun simulateAll() {
        val plan = _actionPlan.value ?: return
        viewModelScope.launch {
            val unsimulated = plan.actions.filter { !_simulatedActions.value.containsKey(it.id) }
            if (unsimulated.isNotEmpty()) {
                val bulkPlan = ActionPlan(unsimulated, plan.priority)
                val executorResults = ExecutorAgent.execute(bulkPlan, _currentCrisisState.value)
                
                val updatedMap = _simulatedActions.value.toMutableMap()
                executorResults.forEach { result ->
                    val action = unsimulated.firstOrNull { it.id == result.actionId }
                    if (action != null) {
                        updatedMap[action.id] = result
                    }
                }
                _simulatedActions.value = updatedMap
                _simulationResults.value = _simulationResults.value + executorResults
                _currentCrisisState.value = CrisisStateDatabase.getCurrentState()
            }
            loadLogs()
        }
    }

    fun loadLogs() {
        _agentLogs.value = AgentLogger.getLogs()
    }

    fun reset() {
        _rawText.value = ""
        _detectionResult.value = null
        _actionPlan.value = null
        _simulationResults.value = emptyList()
        _simulatedActions.value = emptyMap()
        CrisisStateDatabase.resetToBefore()
        _currentCrisisState.value = CrisisStateDatabase.getCurrentState()
        AgentLogger.clear()
        loadLogs()
    }
}
