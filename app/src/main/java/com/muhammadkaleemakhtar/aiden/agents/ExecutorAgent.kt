package com.muhammadkaleemakhtar.aiden.agents

import com.muhammadkaleemakhtar.aiden.BuildConfig
import com.muhammadkaleemakhtar.aiden.data.database.CrisisStateDatabase
import com.muhammadkaleemakhtar.aiden.data.models.ActionPlan
import com.muhammadkaleemakhtar.aiden.data.models.CrisisState
import com.muhammadkaleemakhtar.aiden.data.models.SimulationResult
import com.muhammadkaleemakhtar.aiden.utils.GeminiClient
import org.json.JSONObject

object ExecutorAgent {

    suspend fun execute(plan: ActionPlan, currentState: CrisisState): List<SimulationResult> {
        val results = mutableListOf<SimulationResult>()
        val apiKey = BuildConfig.GEMINI_API_KEY

        for (action in plan.actions) {
            // Compute simulation on DB
            val simResult = CrisisStateDatabase.simulateAction(action)
            
            var explanation = when (action.name) {
                "Traffic Rerouting" -> "Rerouting clears blocked roads, reducing stranded vehicles to zero and halving response time."
                "Emergency Dispatch" -> "Dispatching professional emergency rescue teams decreases average response times by 30%."
                "Public Alerts" -> "Broadcasting alerts coordinates public movement, notifying over 245 active citizens."
                else -> "Simulating execution steps for ${action.name} successfully."
            }

            if (apiKey.isNotBlank()) {
                val prompt = """
                    You are ExecutorAgent, simulating emergency response actions.
                    Explain the transition for the action: "${action.name}"
                    
                    Before State:
                    Stranded Vehicles: ${simResult.beforeState.strandedVehicles}
                    Blocked Roads: ${simResult.beforeState.blockedRoads.joinToString()}
                    Response Time: ${simResult.beforeState.responseTimeMin} mins
                    
                    After State:
                    Stranded Vehicles: ${simResult.afterState.strandedVehicles}
                    Blocked Roads: ${simResult.afterState.blockedRoads.joinToString()}
                    Response Time: ${simResult.afterState.responseTimeMin} mins
                    
                    Provide a concise 1-sentence reasoning explanation of this outcome.
                """.trimIndent()

                val response = GeminiClient.generate(prompt, apiKey).trim()
                if (response.isNotBlank()) {
                    explanation = response
                }
            }

            val finalResult = simResult.copy(message = explanation)
            results.add(finalResult)

            // Log each executed action individually to respect the schema
            AgentLogger.log(
                agentName = "Executor",
                input = mapOf("action" to action.name),
                output = listOf(finalResult), // Pass as list to match custom JSON log parser
                reasoning = explanation
            )
        }

        return results
    }
}
