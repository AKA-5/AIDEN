package com.muhammadkaleemakhtar.aiden.agents

import org.json.JSONArray
import org.json.JSONObject
import com.muhammadkaleemakhtar.aiden.data.models.DetectionResult
import com.muhammadkaleemakhtar.aiden.data.models.ActionPlan
import com.muhammadkaleemakhtar.aiden.data.models.SimulationResult

object AgentLogger {
    private val logs = mutableListOf<LogEntry>()

    data class LogEntry(
        val agent: String,
        val timestamp: Long,
        val input: Any,
        val output: Any,
        val reasoning: String
    )

    @Synchronized
    fun log(agentName: String, input: Any, output: Any, reasoning: String) {
        logs.add(LogEntry(agentName, System.currentTimeMillis(), input, output, reasoning))
    }

    @Synchronized
    fun getLogs(): List<LogEntry> = logs.toList()

    @Synchronized
    fun clear() {
        logs.clear()
    }

    @Synchronized
    fun exportToJson(): String {
        val rootArray = JSONArray()
        for (entry in logs) {
            val obj = JSONObject()
            obj.put("agent", entry.agent)
            obj.put("timestamp", entry.timestamp)

            // Format input based on type
            when (val inputVal = entry.input) {
                is String -> obj.put("input", inputVal)
                is DetectionResult -> {
                    val inputObj = JSONObject()
                    inputObj.put("crisisType", inputVal.crisisType)
                    obj.put("input", inputObj)
                }
                is Map<*, *> -> {
                    val inputObj = JSONObject()
                    inputVal.forEach { (k, v) -> inputObj.put(k.toString(), v) }
                    obj.put("input", inputObj)
                }
                else -> obj.put("input", inputVal.toString())
            }

            // Format output based on type
            when (val outVal = entry.output) {
                is DetectionResult -> {
                    val outObj = JSONObject()
                    outObj.put("crisisType", outVal.crisisType)
                    outObj.put("confidence", outVal.confidence.toDouble())
                    obj.put("output", outObj)
                }
                is ActionPlan -> {
                    val outObj = JSONObject()
                    val actionsArray = JSONArray()
                    outVal.actions.forEach { actionsArray.put(it.name) }
                    outObj.put("actions", actionsArray)
                    obj.put("output", outObj)
                }
                is List<*> -> {
                    val outArray = JSONArray()
                    outVal.forEach { sim ->
                        if (sim is SimulationResult) {
                            val simObj = JSONObject()
                            simObj.put("actionId", sim.actionId)
                            
                            val beforeObj = JSONObject()
                            beforeObj.put("strandedVehicles", sim.beforeState.strandedVehicles)
                            
                            val afterObj = JSONObject()
                            afterObj.put("strandedVehicles", sim.afterState.strandedVehicles)
                            
                            simObj.put("before", beforeObj)
                            simObj.put("after", afterObj)
                            outArray.put(simObj)
                        } else {
                            outArray.put(sim.toString())
                        }
                    }
                    obj.put("output", outArray)
                }
                else -> obj.put("output", outVal.toString())
            }

            obj.put("reasoning", entry.reasoning)
            rootArray.put(obj)
        }
        return rootArray.toString(4)
    }
}
