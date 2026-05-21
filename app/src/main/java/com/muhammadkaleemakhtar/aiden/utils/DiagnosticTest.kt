package com.muhammadkaleemakhtar.aiden.utils

import android.content.Context
import android.os.Environment
import com.muhammadkaleemakhtar.aiden.agents.AgentLogger
import com.muhammadkaleemakhtar.aiden.agents.SentinelAgent
import com.muhammadkaleemakhtar.aiden.data.models.CrisisInput
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File

object DiagnosticTest {
    
    suspend fun runAllTests(context: Context): String = withContext(Dispatchers.IO) {
        val results = mutableListOf<String>()
        
        // Test 1: Different inputs should give different outputs
        results.add("=== TEST 1: Input Variation Test ===")
        val testInputs = listOf(
            "G-10 mein pani bhar gaya hai",
            "Earthquake in Islamabad magnitude 6.0",
            "Karachi heatwave 45 degrees",
            "I like pizza and the sky is blue"
        )
        
        for (inputStr in testInputs) {
            val input = CrisisInput(
                rawText = inputStr,
                weatherEnabled = true,
                trafficEnabled = true,
                socialEnabled = true,
                timestamp = System.currentTimeMillis()
            )
            val detection = SentinelAgent.analyze(input)
            results.add("Input: \"$inputStr\"")
            results.add("-> Detected: ${detection.crisisType} (IsCrisis: ${detection.isCrisis}, Location: ${detection.location}, Severity: ${detection.severity}, Confidence: ${detection.confidence})")
            results.add("------------------------------------")
        }
        
        // Test 2: Location extraction test
        results.add("\n=== TEST 2: Location Extraction Test ===")
        val locationTests = mapOf(
            "G-10 mein pani" to "G-10",
            "DHA Karachi flooding" to "DHA Karachi",
            "Islamabad sector F-7 water" to "F-7",
            "Lahore canal overflowing" to "Lahore"
        )
        
        locationTests.forEach { (text, expected) ->
            // Use the reflection of SentinelAgent's extraction mechanism
            val method = SentinelAgent::class.java.getDeclaredMethod("extractLocationFromText", String::class.java)
            method.isAccessible = true
            val extracted = method.invoke(SentinelAgent, text) as String
            results.add("Input: \"$text\"")
            results.add("-> Extracted: \"$extracted\" (Expected substring: \"$expected\")")
            results.add("------------------------------------")
        }
        
        // Test 3: API call verification
        results.add("\n=== TEST 3: API Call Verification ===")
        results.add("Check Logcat (tag: 'AIDEN_DEBUG') for: 'Calling Gemini API'")
        
        // Test 4: Agent Logger verification
        results.add("\n=== TEST 4: Agent Logger Test ===")
        val beforeCount = AgentLogger.getLogs().size
        AgentLogger.log("TEST", "diagnostic", "running", "test log")
        val afterCount = AgentLogger.getLogs().size
        results.add(if (afterCount > beforeCount) "✅ Logger working" else "❌ Logger not storing")
        
        val report = results.joinToString("\n")
        
        // Export results to Downloads/AIDEN_diagnostic.txt
        try {
            val fileName = "AIDEN_diagnostic.txt"
            var file = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), fileName)
            try {
                file.parentFile?.mkdirs()
                file.writeText(report)
            } catch (e: Exception) {
                // Fall back to sandbox downloads directory if permission is denied
                file = File(context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), fileName)
                file.writeText(report)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        
        return@withContext report
    }
}
