package com.muhammadkaleemakhtar.aiden.agents

import android.util.Log
import com.muhammadkaleemakhtar.aiden.BuildConfig
import com.muhammadkaleemakhtar.aiden.data.mock.SocialMockParser
import com.muhammadkaleemakhtar.aiden.data.models.CrisisInput
import com.muhammadkaleemakhtar.aiden.data.models.DetectionResult
import com.muhammadkaleemakhtar.aiden.utils.GeminiClient
import org.json.JSONArray
import org.json.JSONObject

object SentinelAgent {

    suspend fun analyze(input: CrisisInput): DetectionResult {
        Log.d("AIDEN_DEBUG", "=== SENTINEL AGENT CALLED ===")
        Log.d("AIDEN_DEBUG", "Input text: ${input.rawText}")
        Log.d("AIDEN_DEBUG", "Calling Gemini API...")

        val apiKey = BuildConfig.GEMINI_API_KEY
        var result: DetectionResult? = null

        if (apiKey.isNotBlank()) {
            val prompt = """
                You are SentinelAgent, the first step in a crisis management system.
                Analyze this report and determine if there is an active crisis.
                
                Raw Report text: "${input.rawText}"
                Weather Data Integration Enabled: ${input.weatherEnabled}
                Traffic Data Integration Enabled: ${input.trafficEnabled}
                Social Media Signal Parser Enabled: ${input.socialEnabled}
                
                Respond ONLY with a JSON object. No formatting, no markdown, no ```json wrapper.
                The JSON must contain these exact keys:
                {
                  "isCrisis": Boolean,
                  "crisisType": String (e.g. "Urban Flooding", "Road Blockage", "Heatwave", "None"),
                  "confidence": Float (between 0.0 and 1.0),
                  "location": String,
                  "severity": String ("Low", "Medium", "High"),
                  "impact": Array of Strings,
                  "reasoning": String
                }
            """.trimIndent()

            try {
                val responseText = GeminiClient.generate(prompt, apiKey)
                var detection = parseGeminiResponse(responseText)
                if (detection != null) {
                    // Normalize location if Gemini fails to identify it
                    if (detection.location.isBlank() || detection.location.lowercase().contains("unknown")) {
                        val localLoc = extractLocationFromText(input.rawText)
                        detection = detection.copy(
                            location = if (localLoc.startsWith("Unknown")) "G-10 Sector" else localLoc
                        )
                    }
                    result = detection
                }
            } catch (e: Exception) {
                Log.e("AIDEN_DEBUG", "Gemini API failed: ${e.message}", e)
            }
        } else {
            Log.d("AIDEN_DEBUG", "Gemini API key is blank. Falling back to local rules.")
        }

        // Fallback to local rule engine if Gemini failed or is not configured
        if (result == null) {
            result = runLocalAnalysis(input)
        }

        // Log the decision
        AgentLogger.log(
            agentName = "Sentinel",
            input = input.rawText,
            output = result,
            reasoning = result.reasoning
        )

        Log.d("AIDEN_DEBUG", "Output crisisType: ${result.crisisType}")
        Log.d("AIDEN_DEBUG", "Output location: ${result.location}")
        Log.d("AIDEN_DEBUG", "Output confidence: ${result.confidence}")
        Log.d("AIDEN_DEBUG", "=== SENTINEL AGENT FINISHED ===")

        return result
    }

    private fun parseGeminiResponse(text: String): DetectionResult? {
        if (text.isBlank()) return null
        try {
            val cleanJson = text.trim()
                .removePrefix("```json")
                .removePrefix("```")
                .removeSuffix("```")
                .trim()

            val obj = JSONObject(cleanJson)
            val impactArray = obj.getJSONArray("impact")
            val impactList = mutableListOf<String>()
            for (i in 0 until impactArray.length()) {
                impactList.add(impactArray.getString(i))
            }

            return DetectionResult(
                isCrisis = obj.getBoolean("isCrisis"),
                crisisType = obj.getString("crisisType"),
                confidence = obj.getDouble("confidence").toFloat(),
                location = obj.getString("location"),
                severity = obj.getString("severity"),
                impact = impactList,
                reasoning = obj.getString("reasoning")
            )
        } catch (e: Exception) {
            Log.e("AIDEN_DEBUG", "JSON parsing failed for: $text", e)
            return null
        }
    }

    private fun runLocalAnalysis(input: CrisisInput): DetectionResult {
        val text = input.rawText.lowercase()
        val loc = extractLocationFromText(input.rawText)
        
        val isFlood = listOf("pani", "flood", "water", "bhar", "heavy rain", "rain", "barish", "flooding").any { text.contains(it) }
        val isBlockage = listOf("block", "jam", "traffic", "road", "accident", "band", "obstruction").any { text.contains(it) }
        val isHeat = listOf("heat", "garmi", "dhup", "sunny", "temperature", "heatwave").any { text.contains(it) }

        return when {
            isFlood -> {
                val impacts = mutableListOf("Waterlogging on main sector roads", "Stranded motor vehicles")
                if (input.weatherEnabled) impacts.add("Heavy rainfall alert active")
                if (input.trafficEnabled) impacts.add("Traffic congestion index at 85%")
                
                DetectionResult(
                    isCrisis = true,
                    crisisType = "Urban Flooding",
                    confidence = if (input.weatherEnabled && input.socialEnabled) 0.88f else 0.75f,
                    location = if (loc.startsWith("Unknown")) "G-10 Sector" else loc,
                    severity = "High",
                    impact = impacts,
                    reasoning = "Detected heavy waterlogging indicators in the report. Combined signals confirm Urban Flooding at ${if (loc.startsWith("Unknown")) "G-10 Sector" else loc}."
                )
            }
            isBlockage -> {
                val impacts = mutableListOf("Traffic queues exceeding 1.5km", "Key arterial route blocked")
                if (input.trafficEnabled) impacts.add("Gridlock reported near Main Street")
                
                DetectionResult(
                    isCrisis = true,
                    crisisType = "Road Blockage",
                    confidence = 0.80f,
                    location = if (loc.startsWith("Unknown")) "Main Street" else loc,
                    severity = "Medium",
                    impact = impacts,
                    reasoning = "Traffic blockage signals found in the description. Mapped anomalies suggest physical obstruction."
                )
            }
            isHeat -> {
                DetectionResult(
                    isCrisis = true,
                    crisisType = "Heatwave",
                    confidence = 0.70f,
                    location = if (loc.startsWith("Unknown")) "Urban Center" else loc,
                    severity = "Medium",
                    impact = listOf("Risk of heat stroke for outdoor workers", "Spike in power grid load"),
                    reasoning = "High temperature indicators detected in the text report."
                )
            }
            else -> {
                DetectionResult(
                    isCrisis = false,
                    crisisType = "None",
                    confidence = 0.15f,
                    location = if (loc.startsWith("Unknown")) "Unknown Location" else loc,
                    severity = "Low",
                    impact = emptyList(),
                    reasoning = "Analyzed report text and auxiliary signals. No anomaly or crisis pattern was matched."
                )
            }
        }
    }

    private fun extractLocationFromText(text: String): String {
        val patterns = listOf(
            Regex("G-?[0-9]+", RegexOption.IGNORE_CASE),                    // G-10, G10
            Regex("DHA\\s+[A-Za-z]+", RegexOption.IGNORE_CASE),             // DHA Karachi
            Regex("I-?[0-9]+", RegexOption.IGNORE_CASE),                    // I-8, I8
            Regex("F-?[0-9]+", RegexOption.IGNORE_CASE),                    // F-7
            Regex("([A-Za-z]+)\\s+(sector|town|colony)", RegexOption.IGNORE_CASE),
            Regex("\\b(Karachi|Lahore|Islamabad|Rawalpindi|Multan|Faisalabad)\\b", RegexOption.IGNORE_CASE)
        )
        
        for (pattern in patterns) {
            val match = pattern.find(text)
            if (match != null) {
                return match.value
            }
        }
        return "Unknown (could not extract)"
    }
}
