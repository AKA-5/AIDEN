package com.muhammadkaleemakhtar.aiden.agents

import com.muhammadkaleemakhtar.aiden.BuildConfig
import com.muhammadkaleemakhtar.aiden.data.models.Action
import com.muhammadkaleemakhtar.aiden.data.models.ActionPlan
import com.muhammadkaleemakhtar.aiden.data.models.DetectionResult
import com.muhammadkaleemakhtar.aiden.utils.GeminiClient
import org.json.JSONObject

object PlannerAgent {

    suspend fun plan(detection: DetectionResult): ActionPlan {
        if (!detection.isCrisis) {
            return ActionPlan(emptyList(), "Low")
        }

        val apiKey = BuildConfig.GEMINI_API_KEY
        var plan: ActionPlan? = null

        if (apiKey.isNotBlank()) {
            val prompt = """
                You are PlannerAgent. Recommend exactly 2 or 3 actions to coordinate a response to this crisis.
                
                Crisis Type: "${detection.crisisType}"
                Location: "${detection.location}"
                Severity: "${detection.severity}"
                Impact: ${detection.impact.joinToString()}
                
                Respond ONLY with a JSON object. No markdown, no formatting.
                The JSON must contain these exact keys:
                {
                  "priority": String ("High", "Medium", "Low"),
                  "actions": [
                    {
                      "id": String,
                      "name": String (Choose from: "Traffic Rerouting", "Emergency Dispatch", "Public Alerts"),
                      "description": String
                    }
                  ]
                }
            """.trimIndent()

            val responseText = GeminiClient.generate(prompt, apiKey)
            plan = parseGeminiResponse(responseText)
        }

        if (plan == null) {
            plan = runLocalPlanning(detection)
        }

        // Log the decision
        AgentLogger.log(
            agentName = "Planner",
            input = detection,
            output = plan,
            reasoning = "Formulated emergency response actions prioritized by severity (${detection.severity})."
        )

        return plan
    }

    private fun parseGeminiResponse(text: String): ActionPlan? {
        if (text.isBlank()) return null
        try {
            val cleanJson = text.trim()
                .removePrefix("```json")
                .removePrefix("```")
                .removeSuffix("```")
                .trim()

            val obj = JSONObject(cleanJson)
            val actionsArray = obj.getJSONArray("actions")
            val actionList = mutableListOf<Action>()
            for (i in 0 until actionsArray.length()) {
                val actObj = actionsArray.getJSONObject(i)
                actionList.add(
                    Action(
                        id = actObj.getString("id"),
                        name = actObj.getString("name"),
                        description = actObj.getString("description"),
                        isSimulated = false
                    )
                )
            }
            return ActionPlan(actionList, obj.getString("priority"))
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }
    }

    private fun runLocalPlanning(detection: DetectionResult): ActionPlan {
        val actions = mutableListOf<Action>()
        val priority = detection.severity

        when (detection.crisisType) {
            "Urban Flooding" -> {
                actions.add(
                    Action(
                        id = "act_reroute",
                        name = "Traffic Rerouting",
                        description = "Close waterlogged channels and divert traffic through elevated secondary streets."
                    )
                )
                actions.add(
                    Action(
                        id = "act_dispatch",
                        name = "Emergency Dispatch",
                        description = "Deploy rescue vehicles and portable water pumps to clear stranded areas."
                    )
                )
                actions.add(
                    Action(
                        id = "act_alert",
                        name = "Public Alerts",
                        description = "Send high-priority warning SMS to residents within a 2km radius."
                    )
                )
            }
            "Road Blockage" -> {
                actions.add(
                    Action(
                        id = "act_reroute",
                        name = "Traffic Rerouting",
                        description = "Reroute incoming traffic to alleviate congestion and avoid sector bottlenecks."
                    )
                )
                actions.add(
                    Action(
                        id = "act_alert",
                        name = "Public Alerts",
                        description = "Broadcast road blockage details via emergency notification channels."
                    )
                )
            }
            "Heatwave" -> {
                actions.add(
                    Action(
                        id = "act_dispatch",
                        name = "Emergency Dispatch",
                        description = "Establish emergency cooling stations and dispatch mobile medical assistance squads."
                    )
                )
                actions.add(
                    Action(
                        id = "act_alert",
                        name = "Public Alerts",
                        description = "Issue warnings to avoid direct sun exposure during peak daylight hours."
                    )
                )
            }
            else -> {
                actions.add(
                    Action(
                        id = "act_alert",
                        name = "Public Alerts",
                        description = "Broadcast standard safety guidelines regarding current situational alerts."
                    )
                )
            }
        }

        return ActionPlan(actions, priority)
    }
}
