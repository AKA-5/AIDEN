# AIDEN - Project Completion Report

## 1. Project Overview
AIDEN (AI-powered Intelligent Disaster & Emergency Network) is an agentic AI-driven crisis response orchestrator built for Challenge 3 of the Google Antigravity Hackathon 2026. The app ingests natural language reports (in English and Roman Urdu), correlates them with weather, traffic, and social signals, and utilizes three distinct agents (Sentinel, Planner, and Executor) to detect emergencies, formulate mitigation plans, and simulate coordinate response actions.

---

## 2. Files Created and Modified
The project has been implemented cleanly following the package namespace `com.muhammadkaleemakhtar.aiden` and Material 3 design rules:

### Core Configuration & Resources
- [app/build.gradle.kts](file:///d:/CodeProjects/AndroidProjects/AIDEN/app/build.gradle.kts) - Injected Gemini API keys and configured Material 3 dependencies.
- [app/src/main/res/values/font_certs.xml](file:///d:/CodeProjects/AndroidProjects/AIDEN/app/src/main/res/values/font_certs.xml) - Downloadable fonts verification certificates for Google Fonts integration.
- [app/src/main/java/com/muhammadkaleemakhtar/aiden/MainActivity.kt](file:///d:/CodeProjects/AndroidProjects/AIDEN/app/src/main/java/com/muhammadkaleemakhtar/aiden/MainActivity.kt) - Entry point coordinating view models and navigation.

### Theme & Typography (Warm Palette - No Blues)
- [app/src/main/java/com/muhammadkaleemakhtar/aiden/ui/theme/Color.kt](file:///d:/CodeProjects/AndroidProjects/AIDEN/app/src/main/java/com/muhammadkaleemakhtar/aiden/ui/theme/Color.kt) - Strict warm tone color system.
- [app/src/main/java/com/muhammadkaleemakhtar/aiden/ui/theme/Type.kt](file:///d:/CodeProjects/AndroidProjects/AIDEN/app/src/main/java/com/muhammadkaleemakhtar/aiden/ui/theme/Type.kt) - Configured Playfair Display, Inter, and DM Sans via downloadable Google Fonts.
- [app/src/main/java/com/muhammadkaleemakhtar/aiden/ui/theme/Theme.kt](file:///d:/CodeProjects/AndroidProjects/AIDEN/app/src/main/java/com/muhammadkaleemakhtar/aiden/ui/theme/Theme.kt) - Configured warm Light and Dark Material 3 color schemes.

### Data Layer
- [app/src/main/java/com/muhammadkaleemakhtar/aiden/data/models/CrisisInput.kt](file:///d:/CodeProjects/AndroidProjects/AIDEN/app/src/main/java/com/muhammadkaleemakhtar/aiden/data/models/CrisisInput.kt)
- [app/src/main/java/com/muhammadkaleemakhtar/aiden/data/models/DetectionResult.kt](file:///d:/CodeProjects/AndroidProjects/AIDEN/app/src/main/java/com/muhammadkaleemakhtar/aiden/data/models/DetectionResult.kt)
- [app/src/main/java/com/muhammadkaleemakhtar/aiden/data/models/ActionPlan.kt](file:///d:/CodeProjects/AndroidProjects/AIDEN/app/src/main/java/com/muhammadkaleemakhtar/aiden/data/models/ActionPlan.kt)
- [app/src/main/java/com/muhammadkaleemakhtar/aiden/data/models/CrisisState.kt](file:///d:/CodeProjects/AndroidProjects/AIDEN/app/src/main/java/com/muhammadkaleemakhtar/aiden/data/models/CrisisState.kt)
- [app/src/main/java/com/muhammadkaleemakhtar/aiden/data/models/SimulationResult.kt](file:///d:/CodeProjects/AndroidProjects/AIDEN/app/src/main/java/com/muhammadkaleemakhtar/aiden/data/models/SimulationResult.kt)
- [app/src/main/java/com/muhammadkaleemakhtar/aiden/data/mock/WeatherMockApi.kt](file:///d:/CodeProjects/AndroidProjects/AIDEN/app/src/main/java/com/muhammadkaleemakhtar/aiden/data/mock/WeatherMockApi.kt)
- [app/src/main/java/com/muhammadkaleemakhtar/aiden/data/mock/TrafficMockApi.kt](file:///d:/CodeProjects/AndroidProjects/AIDEN/app/src/main/java/com/muhammadkaleemakhtar/aiden/data/mock/TrafficMockApi.kt)
- [app/src/main/java/com/muhammadkaleemakhtar/aiden/data/mock/SocialMockParser.kt](file:///d:/CodeProjects/AndroidProjects/AIDEN/app/src/main/java/com/muhammadkaleemakhtar/aiden/data/mock/SocialMockParser.kt)
- [app/src/main/java/com/muhammadkaleemakhtar/aiden/data/database/CrisisStateDatabase.kt](file:///d:/CodeProjects/AndroidProjects/AIDEN/app/src/main/java/com/muhammadkaleemakhtar/aiden/data/database/CrisisStateDatabase.kt)

### Agent & Utilities Layer
- [app/src/main/java/com/muhammadkaleemakhtar/aiden/agents/AgentLogger.kt](file:///d:/CodeProjects/AndroidProjects/AIDEN/app/src/main/java/com/muhammadkaleemakhtar/aiden/agents/AgentLogger.kt) - Thread-safe logging engine with custom JSON formatting.
- [app/src/main/java/com/muhammadkaleemakhtar/aiden/agents/SentinelAgent.kt](file:///d:/CodeProjects/AndroidProjects/AIDEN/app/src/main/java/com/muhammadkaleemakhtar/aiden/agents/SentinelAgent.kt) - Emergency classifier (Gemini & local regex fallback).
- [app/src/main/java/com/muhammadkaleemakhtar/aiden/agents/PlannerAgent.kt](file:///d:/CodeProjects/AndroidProjects/AIDEN/app/src/main/java/com/muhammadkaleemakhtar/aiden/agents/PlannerAgent.kt) - Generates mitigation plans (Gemini & structured fallback).
- [app/src/main/java/com/muhammadkaleemakhtar/aiden/agents/ExecutorAgent.kt](file:///d:/CodeProjects/AndroidProjects/AIDEN/app/src/main/java/com/muhammadkaleemakhtar/aiden/agents/ExecutorAgent.kt) - Performs simulation routines (Gemini explanation & state updates).
- [app/src/main/java/com/muhammadkaleemakhtar/aiden/utils/GeminiClient.kt](file:///d:/CodeProjects/AndroidProjects/AIDEN/app/src/main/java/com/muhammadkaleemakhtar/aiden/utils/GeminiClient.kt) - Light-weight HTTP client making post requests to Gemini API (no gradle sync issues).
- [app/src/main/java/com/muhammadkaleemakhtar/aiden/utils/FileExporter.kt](file:///d:/CodeProjects/AndroidProjects/AIDEN/app/src/main/java/com/muhammadkaleemakhtar/aiden/utils/FileExporter.kt) - Exports logs to Downloads folder using standard `MediaStore`.

### UI Component & Navigation Layer
- [app/src/main/java/com/muhammadkaleemakhtar/aiden/ui/viewmodel/AidenViewModel.kt](file:///d:/CodeProjects/AndroidProjects/AIDEN/app/src/main/java/com/muhammadkaleemakhtar/aiden/ui/viewmodel/AidenViewModel.kt)
- [app/src/main/java/com/muhammadkaleemakhtar/aiden/ui/navigation/NavGraph.kt](file:///d:/CodeProjects/AndroidProjects/AIDEN/app/src/main/java/com/muhammadkaleemakhtar/aiden/ui/navigation/NavGraph.kt)
- [app/src/main/java/com/muhammadkaleemakhtar/aiden/ui/components/ConfidenceBar.kt](file:///d:/CodeProjects/AndroidProjects/AIDEN/app/src/main/java/com/muhammadkaleemakhtar/aiden/ui/components/ConfidenceBar.kt)
- [app/src/main/java/com/muhammadkaleemakhtar/aiden/ui/components/MetricCard.kt](file:///d:/CodeProjects/AndroidProjects/AIDEN/app/src/main/java/com/muhammadkaleemakhtar/aiden/ui/components/MetricCard.kt)
- [app/src/main/java/com/muhammadkaleemakhtar/aiden/ui/components/ActionCard.kt](file:///d:/CodeProjects/AndroidProjects/AIDEN/app/src/main/java/com/muhammadkaleemakhtar/aiden/ui/components/ActionCard.kt)
- [app/src/main/java/com/muhammadkaleemakhtar/aiden/ui/components/AgentLogViewer.kt](file:///d:/CodeProjects/AndroidProjects/AIDEN/app/src/main/java/com/muhammadkaleemakhtar/aiden/ui/components/AgentLogViewer.kt)
- [app/src/main/java/com/muhammadkaleemakhtar/aiden/ui/screens/InputScreen.kt](file:///d:/CodeProjects/AndroidProjects/AIDEN/app/src/main/java/com/muhammadkaleemakhtar/aiden/ui/screens/InputScreen.kt)
- [app/src/main/java/com/muhammadkaleemakhtar/aiden/ui/screens/DetectionScreen.kt](file:///d:/CodeProjects/AndroidProjects/AIDEN/app/src/main/java/com/muhammadkaleemakhtar/aiden/ui/screens/DetectionScreen.kt)
- [app/src/main/java/com/muhammadkaleemakhtar/aiden/ui/screens/ActionScreen.kt](file:///d:/CodeProjects/AndroidProjects/AIDEN/app/src/main/java/com/muhammadkaleemakhtar/aiden/ui/screens/ActionScreen.kt)
- [app/src/main/java/com/muhammadkaleemakhtar/aiden/ui/screens/OutcomeScreen.kt](file:///d:/CodeProjects/AndroidProjects/AIDEN/app/src/main/java/com/muhammadkaleemakhtar/aiden/ui/screens/OutcomeScreen.kt)

---

## 3. Assumptions Made
1. **Gemini API Key Injection**: To avoid exposing raw API keys on git history, the build file reads the key from `local.properties` under `GEMINI_API_KEY` and exposes it as `BuildConfig.GEMINI_API_KEY`.
2. **Local Fallback Mode**: If `GEMINI_API_KEY` is not present, or if there is no internet, the application seamlessly falls back to a high-fidelity local keyword and regex engine so that the hackathon demo remains 100% operational offline.
3. **Downloadable Fonts**: We download Google Fonts dynamically. A certificates resource file `font_certs.xml` was created to satisfy GMS Core security checks.

---

## 4. Setting up Gemini API Key
To retrieve and integrate your Google Gemini API Key:
1. Visit [Google AI Studio](https://aistudio.google.com/).
2. Click **Create API Key**.
3. Copy the generated key.
4. Open the root directory of this Android project.
5. Open or create the [local.properties](file:///d:/CodeProjects/AndroidProjects/AIDEN/local.properties) file.
6. Add the following line:
   ```properties
   GEMINI_API_KEY=AIzaSy...your_actual_key_here
   ```
7. Re-sync the Gradle project in Android Studio.

---

## 5. How to Run and Test
1. **Sync and Compile**: Open the folder in Android Studio and let Gradle sync complete. Run `./gradlew assembleDebug` to verify compilation.
2. **Launch Application**: Deploy to an emulator or physical device running API level 26 (Android 8.0) or higher.
3. **Input Phase**:
   - Write a report: e.g. *"G-10 main barish ki wajah se pani bhar gaya hai, traffic bilkul jam hai"*
   - Toggle auxiliary sources (Weather data, Traffic feed, Social reports) as desired.
   - Click **SUBMIT REPORT**.
4. **Detection Phase**:
   - View the detected crisis event ("Urban Flooding") and the animated Confidence bar.
   - Click on the **AGENT REASONING** collapsible card to view explanations in mono terminal font.
   - Click **PLAN RESPONSE**.
5. **Action Phase**:
   - Review proposed action items: *Traffic Rerouting*, *Emergency Dispatch*, and *Public Alerts*.
   - Click **Simulate** on individual cards to see real-time status transitions, or click **Simulate All**.
   - Click **VIEW FULL IMPACT**.
6. **Outcome Phase**:
   - View the Canvas city map toggle:
     - **Before**: Shows red dots highlighting flooded roadblocks.
     - **After**: Shows green dots showing clear paths and active rescue squads.
   - Swipe horizontally through the `MetricCard` carousel to compare stranded vehicles, response times, and alerts.
   - Click **Export Logs** (Download icon on Top Bar) to save the raw JSON agent trace file to the phone's `Downloads` folder.
   - Click **Share Report** to broadcast mock safety summaries or **New Report** to restart.
