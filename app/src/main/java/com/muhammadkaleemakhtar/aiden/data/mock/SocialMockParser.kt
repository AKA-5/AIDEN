package com.muhammadkaleemakhtar.aiden.data.mock

object SocialMockParser {
    fun parse(text: String): SocialSignal {
        val keywords = listOf("pani", "flood", "water", "bhar", "heavy rain", "accident", "heatwave", "fire")
        val isCrisisRelated = keywords.any { text.lowercase().contains(it) }
        return SocialSignal(
            containsCrisisKeyword = isCrisisRelated,
            extractedLocation = extractLocation(text),
            confidence = if (isCrisisRelated) 0.8f else 0.1f
        )
    }

    private fun extractLocation(text: String): String {
        val regexes = listOf(
            Regex("G-\\d+"),
            Regex("I-\\d+"),
            Regex("H-\\d+"),
            Regex("[A-Za-z]+\\s+\\d+"),
            Regex("George\\s+Town", RegexOption.IGNORE_CASE)
        )
        for (regex in regexes) {
            val match = regex.find(text)
            if (match != null) {
                return match.value
            }
        }
        return "Unknown Location"
    }
}

data class SocialSignal(
    val containsCrisisKeyword: Boolean,
    val extractedLocation: String,
    val confidence: Float
)
