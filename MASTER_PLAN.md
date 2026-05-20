# AIDEN - Crisis Intelligence & Response Orchestrator
## Google Antigravity Hackathon 2026 | Challenge 3

---

## PROJECT STATUS (Update this as you go)

| Phase | Status | Last Updated |
|-------|--------|--------------|
| Setup & Dependencies | ✅ Complete | 2026-05-20 |
| Data Layer (Models + Mocks) | ✅ Complete | 2026-05-20 |
| Agent Layer (3 Agents + Logger) | ✅ Complete | 2026-05-20 |
| UI - Input Screen | ✅ Complete | 2026-05-20 |
| UI - Detection Screen | ✅ Complete | 2026-05-20 |
| UI - Action Screen | ✅ Complete | 2026-05-20 |
| UI - Outcome Screen | ✅ Complete | 2026-05-20 |
| Navigation + MainActivity | ✅ Complete | 2026-05-20 |
| README + Documentation | ✅ Complete | 2026-05-20 |
| Agent Logs Export | ✅ Complete | 2026-05-20 |

**Legend:** ⬜ Not Started | 🔄 In Progress | ✅ Complete | ⚠️ Blocked

---

## PROJECT IDENTITY

### App Name
**AIDEN** (AI-powered Intelligent Disaster & Emergency Network)

### Package Name
`com.hackathon.aiden`

### Minimum SDK
API 26 (Android 8.0)

### Target SDK
API 35

### Language
Kotlin only (no Java)

### UI Toolkit
Jetpack Compose (Material 3) - NO XML layouts

---

## COLOR PALETTE (Strict - No Blues)

| Role | Name | Hex | Usage |
|------|------|-----|-------|
| Primary | Terracotta | `#E76F51` | Buttons, active states |
| Secondary | Warm Sand | `#F4A261` | Secondary buttons, accents |
| Background | Warm White | `#FDF8F5` | Main screen background |
| Surface | Pure White | `#FFFFFF` | Cards, input fields |
| Text Primary | Dark Brown | `#2C1810` | Headings, body |
| Text Secondary | Muted Brown | `#7F5E4A` | Labels, hints |
| Success | Teal Green | `#2A9D8F` | Completed actions |
| Warning | Mustard | `#E9C46A` | Attention needed |
| Crisis | Coral Red | `#E63946` | Crisis detection, errors |
| Divider | Warm Gray | `#E8DCD4` | Separators |

### Forbidden Colors
- ❌ Any shade of blue (#1a73e8, #00bcd4, #2196f3, etc.)
- ❌ Neon colors
- ❌ Pure black for backgrounds

---

## TYPOGRAPHY

| Style | Font | Weight | Size | Usage |
|-------|------|--------|------|-------|
| Display Large | Playfair Display | Bold | 34sp | Screen titles |
| Display Medium | Playfair Display | SemiBold | 28sp | Crisis name, key metrics |
| Headline | Playfair Display | Medium | 22sp | Section titles |
| Body Large | Inter | Regular | 16sp | Descriptions |
| Body Medium | Inter | Regular | 14sp | Labels, secondary text |
| Label Large | DM Sans | Medium | 14sp | Input labels, badges |
| Label Small | DM Sans | Medium | 12sp | Captions, timestamps |
| Mono | JetBrains Mono | Regular | 12sp | Agent logs |

### Font Implementation
All fonts via Google Fonts Compose library (not system fonts):
```kotlin
implementation("androidx.compose.ui:ui-text-google-fonts:1.7.8")
ICONS
Source: Google Material Icons (Outlined style)

Format: Icons.Outlined.*

No emojis anywhere

ARCHITECTURE
Layer Structure
text
app/src/main/java/com/hackathon/aiden/
├── MainActivity.kt
├── ui/
│   ├── screens/
│   │   ├── InputScreen.kt
│   │   ├── DetectionScreen.kt
│   │   ├── ActionScreen.kt
│   │   └── OutcomeScreen.kt
│   ├── components/
│   │   ├── ActionCard.kt
│   │   ├── ConfidenceBar.kt
│   │   ├── MetricCard.kt
│   │   └── AgentLogViewer.kt
│   ├── theme/
│   │   ├── Color.kt
│   │   ├── Type.kt
│   │   └── Theme.kt
│   └── navigation/
│       └── NavGraph.kt
├── agents/
│   ├── SentinelAgent.kt
│   ├── PlannerAgent.kt
│   ├── ExecutorAgent.kt
│   └── AgentLogger.kt
├── data/
│   ├── models/
│   │   ├── CrisisInput.kt
│   │   ├── DetectionResult.kt
│   │   ├── ActionPlan.kt
│   │   ├── SimulationResult.kt
│   │   └── CrisisState.kt
│   ├── mock/
│   │   ├── WeatherMockApi.kt
│   │   ├── TrafficMockApi.kt
│   │   └── SocialMockParser.kt
│   └── database/
│       └── CrisisStateDatabase.kt
└── utils/
    └── FileExporter.kt
DATA MODELS (Define exactly as shown)
CrisisInput
kotlin
data class CrisisInput(
    val rawText: String,
    val weatherEnabled: Boolean,
    val trafficEnabled: Boolean,
    val socialEnabled: Boolean,
    val timestamp: Long
)
DetectionResult
kotlin
data class DetectionResult(
    val isCrisis: Boolean,
    val crisisType: String, // "Urban Flooding", "None"
    val confidence: Float, // 0.0 to 1.0
    val location: String,
    val severity: String, // "Low", "Medium", "High"
    val impact: List<String>,
    val reasoning: String
)
ActionPlan
kotlin
data class ActionPlan(
    val actions: List<Action>,
    val priority: String
)

data class Action(
    val id: String,
    val name: String, // "Traffic Rerouting", "Emergency Dispatch", "Public Alerts"
    val description: String,
    val isSimulated: Boolean
)
SimulationResult
kotlin
data class SimulationResult(
    val actionId: String,
    val beforeState: CrisisState,
    val afterState: CrisisState,
    val success: Boolean,
    val message: String
)
CrisisState
kotlin
data class CrisisState(
    val blockedRoads: List<String>,
    val strandedVehicles: Int,
    val teamsDispatched: Int,
    val alertsSent: Int,
    val responseTimeMin: Int
)
AGENT SPECIFICATIONS
Agent 1: SentinelAgent
Input: CrisisInput
Output: DetectionResult
Method: suspend fun analyze(input: CrisisInput): DetectionResult
Log Format:

json
{"agent":"Sentinel","timestamp":123456,"input":"text here","output":{"crisisType":"Urban Flooding","confidence":0.85},"reasoning":"Because user mentioned water and G-10 location..."}
Agent 2: PlannerAgent
Input: DetectionResult
Output: ActionPlan
Method: suspend fun plan(detection: DetectionResult): ActionPlan
Log Format:

json
{"agent":"Planner","timestamp":123457,"input":{"crisisType":"Urban Flooding"},"output":{"actions":["Traffic Rerouting","Emergency Dispatch","Public Alerts"]},"reasoning":"Flooding requires traffic management, rescue, and public communication"}
Agent 3: ExecutorAgent
Input: ActionPlan, current CrisisState
Output: List<SimulationResult>
Method: suspend fun execute(plan: ActionPlan, currentState: CrisisState): List<SimulationResult>
Log Format:

json
{"agent":"Executor","timestamp":123458,"input":{"action":"Traffic Rerouting"},"output":{"before":{"strandedVehicles":45},"after":{"strandedVehicles":0}},"reasoning":"Rerouting clears blocked roads, reducing stranded vehicles to zero"}
AgentLogger
Singleton object

Stores all logs in memory

Method: fun log(agentName: String, input: Any, output: Any, reasoning: String)

Method: fun exportToJson(): String

Method: fun clear()

MOCK APIs (No real network calls)
WeatherMockApi
kotlin
object WeatherMockApi {
    fun getData(location: String): WeatherResponse {
        return WeatherResponse(
            rainfall = "heavy",
            alert = "flood_warning",
            temperature = 28,
            timestamp = System.currentTimeMillis()
        )
    }
}

data class WeatherResponse(
    val rainfall: String,
    val alert: String,
    val temperature: Int,
    val timestamp: Long
)
TrafficMockApi
kotlin
object TrafficMockApi {
    fun getData(location: String): TrafficResponse {
        return TrafficResponse(
            congestionLevel = 0.85,
            blockedRoads = listOf("Main Street", "G-10 Sector Road"),
            affectedVehicles = 45
        )
    }
}
SocialMockParser
kotlin
object SocialMockParser {
    fun parse(text: String): SocialSignal {
        val keywords = listOf("pani", "flood", "water", "bhar", "heavy rain")
        val isCrisisRelated = keywords.any { text.lowercase().contains(it) }
        return SocialSignal(
            containsCrisisKeyword = isCrisisRelated,
            extractedLocation = extractLocation(text),
            confidence = if (isCrisisRelated) 0.7f else 0.1f
        )
    }
    
    private fun extractLocation(text: String): String {
        val patterns = listOf("G-[0-9]+", "([A-Za-z]+\\s+[0-9]+)", "I-[0-9]+")
        // Return first match or "Unknown"
    }
}
MOCK DATABASE
kotlin
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
        val after = when(action.name) {
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
SCREEN SPECIFICATIONS
Screen 1: InputScreen
Route: "input"
Components:

Header: "AIDEN" + tagline "Crisis Intelligence & Response"

TextField (multiline, 4-5 lines)

Placeholder: "Describe what's happening... (English / Roman Urdu)"

Example hint: '"G-10 mein pani bhar gaya hai"'

3 ToggleCards (Material3 FilterChip or custom):

[Mock] Weather Data

[Mock] Traffic Data

[Mock] Social Reports

Submit Button (Primary #E76F51)

Footer note: "All data simulated for demo purposes"

Navigation: On submit → navigate to DetectionScreen with input data

Screen 2: DetectionScreen
Route: "detection/{inputJson}" or pass via ViewModel
Components:

Back button (optional)

Situation Card

Label "DETECTED SITUATION" (DM Sans, uppercase)

Value (Playfair Display, 28sp, Crisis color if detected)

Confidence Section

Label "CONFIDENCE"

LinearProgressIndicator (track #E8DCD4, fill #E76F51)

Percentage text

Impact Section

Bullet list with Material Icons

Reasoning Section (Collapsible Card)

Shows agent reasoning text

Mono font for log view

Next Button (Secondary #F4A261)

Navigation: Next → ActionScreen

Screen 3: ActionScreen
Route: "action"
Components:

3 Action Cards (using reusable ActionCard component)
Each card has:

Icon (Material Outlined)

Title (Playfair Display)

Description (Inter)

Before state pill

After state pill (visible after simulation)

Simulate button (Success #2A9D8F)

Status indicator

"Simulate All" button (Primary)

Outcome preview area (shows after applying all simulations)

Next Button (appears after at least one action simulated)

Screen 4: OutcomeScreen
Route: "outcome"
Components:

Before/After toggle or side-by-side (tabs for phone)

Map mockup (Canvas or simple Box with circles)

Before: Red markers at blockedRoads coordinates

After: Green markers + rescue icons

Metrics Row (horizontal scroll)

3 MetricCard components (stranded vehicles, response time, teams deployed)

Agent Logs Section

Title + Export button

Scrollable log viewer (JetBrains Mono)

Share Report Button

Reset/New Report Button

NAVIGATION
kotlin
sealed class Screen(val route: String) {
    object Input : Screen("input")
    object Detection : Screen("detection")
    object Action : Screen("action")
    object Outcome : Screen("outcome")
}

// In MainActivity
NavHost(navController, startDestination = Screen.Input.route) {
    composable(Screen.Input.route) { InputScreen(navController) }
    composable(Screen.Detection.route) { DetectionScreen(navController, viewModel) }
    composable(Screen.Action.route) { ActionScreen(navController, viewModel) }
    composable(Screen.Outcome.route) { OutcomeScreen(navController, viewModel) }
}
DEPENDENCIES (build.gradle.kts - app level)
kotlin
dependencies {
    implementation("androidx.core:core-ktx:1.15.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.8.7")
    implementation("androidx.activity:activity-compose:1.10.1")
    
    // Compose BOM
    implementation(platform("androidx.compose:compose-bom:2025.02.00"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")
    implementation("androidx.compose.material3:material3-window-size-class")
    
    // Navigation
    implementation("androidx.navigation:navigation-compose:2.8.9")
    
    // ViewModel
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.8.7")
    
    // Google Fonts
    implementation("androidx.compose.ui:ui-text-google-fonts:1.7.8")
    
    // Testing
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.2.1")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.6.1")
}
ANTI-DEPRECATED CODE RULES
Forbidden Imports
kotlin
// NEVER use these:
import androidx.compose.material.Button  // Missing "3"
import androidx.compose.material.Text    // Missing "3"
import androidx.compose.material.Scaffold // Missing "3"
import androidx.compose.material.Icon     // Missing "3"
import com.google.accompanist.*           // All Accompanist is deprecated
Required Imports
kotlin
// ALWAYS use these:
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Icon
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
API Levels
minSdk = 26 (Android 8.0)

targetSdk = 35 (Android 15)

compileSdk = 35

Compose Version
Compose compiler: 1.5.15

Kotlin: 2.0.21

Use BOM for all Compose libraries (auto-versioning)

SUBMISSION DELIVERABLES CHECKLIST
Android APK (debug or release) - runs on API 26+

Source code on GitHub (this repository)

MASTER_PLAN.md (this file) in root

README.md with setup instructions

Agent logs export function (JSON file to Downloads)

Demo video (3-5 minutes) - link in README

Screenshots folder (/screenshots in repo)

DEMO VIDEO SCRIPT (3 minutes)
Time	Screen	Action	Narration
0:00-0:30	Input	Type "G-10 mein pani bhar gaya hai", enable toggles	"User reports flooding in Roman Urdu"
0:30-1:00	Detection	Show detection card	"System detects Urban Flooding with 85% confidence"
1:00-1:15	Detection	Expand reasoning	"Here's the agent's reasoning"
1:15-2:00	Action	Simulate all three actions	"Three response actions simulated sequentially"
2:00-2:30	Outcome	Show before/after	"Before: 45 stranded vehicles. After: 0"
2:30-2:50	Outcome	Export logs	"Agent logs exported for submission"
2:50-3:00	-	Closing	"AIDEN - turning crisis into coordinated response"
PROGRESS TRACKING INSTRUCTIONS
For Any AI Agent Reading This File:
Check the STATUS table at the top to see what's done

Start with the first ⬜ item

After completing a phase, update the status to ✅ and add timestamp

If stuck, mark as ⚠️ and describe the issue

Always follow the exact model definitions (don't invent new fields)

Never use deprecated imports (see rules above)

Test each file before moving to next

File Creation Order (Strict):
build.gradle.kts (app level) + build.gradle.kts (project level)

theme/Color.kt, theme/Type.kt, theme/Theme.kt

All data models (in data/models/)

Mock APIs (data/mock/)

Database (data/database/)

Agent classes (agents/) + AgentLogger

UI components (ui/components/)

Screens in order: Input → Detection → Action → Outcome

Navigation + MainActivity

README.md

NOTES FOR DEVELOPER
This project uses NO external APIs - everything is mocked

All location data is simulated (G-10, Main Street are examples)

Roman Urdu support is basic keyword matching (extendable)

Fonts are downloaded via Google Fonts (internet permission required once)

Agent logs are stored in memory and exported on demand

LAST UPDATED
2026-05-20

STATUS SUMMARY
Setup: 10/10 phases complete


make sure to remove unnecessary files or md files from github right add them in gitignore also make sure to follow the structure and instructions proeprly i am adding the details of the challenge in the Challenge.md file if any instructions are not clear ask me to change or modify them properly.