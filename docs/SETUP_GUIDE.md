# AIDEN - Setup and Configuration Guide

This guide will walk you through the process of setting up, configuring, compiling, and running the AIDEN (AI-powered Intelligent Disaster & Emergency Network) application.

---

## 1. Prerequisites

Before starting, ensure your local development machine has the following tools installed and updated:
*   **Android Studio**: Ladybug (2024.2.1) or newer.
*   **JDK**: Java Development Kit 17 (bundled within Android Studio's JBR).
*   **Android SDK**: API 35 (Android 15) Platform SDK.
*   **Device/Emulator**: A physical Android device or emulator running API 26 (Android 8.0) or higher.
*   **Internet Connection**: Required for the initial Gradle dependency synchronization and downloading Google Fonts certificates.

---

## 2. Gemini API Key Acquisition

AIDEN leverages the Gemini Pro / Flash models to power its Sentinel, Planner, and Executor agents. To acquire an API key:

1.  Navigate to the [Google AI Studio](https://aistudio.google.com/).
2.  Log in using your Google Account.
3.  Click on the **Get API Key** button in the top left.
4.  Select **Create API Key** and generate a key for a new or existing Google Cloud project.
5.  Copy the generated API Key.

---

## 3. Local Configuration

To secure your Gemini API key and prevent it from being checked into version control, the project reads configuration details from the local environment:

1.  Open the project root directory.
2.  Open or create a file named `local.properties`.
3.  Add your API Key as follows:
    ```properties
    GEMINI_API_KEY=AIzaSyYourGeminiApiKeyHere
    ```
4.  Save and close the file. The Android app gradle build script will automatically detect this property and inject it into the generated `BuildConfig` class as `BuildConfig.GEMINI_API_KEY`.

---

## 4. Building the Project

### Using Android Studio
1.  Launch Android Studio.
2.  Select **Open** and select the AIDEN root directory `d:\CodeProjects\AndroidProjects\AIDEN`.
3.  Let the Gradle sync process finish.
4.  Press **Build** > **Make Project** (`Ctrl+F9` / `Cmd+F9`) to compile the project.

### Using the Command Line
You can build the project from your terminal. Open PowerShell or Bash in the project root:

```powershell
# On Windows PowerShell, verify the JAVA_HOME is set to Android Studio's JetBrains Runtime (JBR)
$env:JAVA_HOME="C:\Program Files\Android\Android Studio\jbr"
./gradlew assembleDebug
```

---

## 5. Running the Application

1.  Start an Android Emulator or connect a physical Android device with USB Debugging enabled.
2.  In Android Studio, click the green **Run** button (`Shift+F10`) in the toolbar.
3.  Select your target device to launch the application.

---

## 6. Offline Mock Triage

AIDEN features a highly robust fallback structure. If you run the app without a `GEMINI_API_KEY` defined in `local.properties`, or if your device is completely disconnected from the network, the agents will automatically run in local keyword regex parsing mode. All screens, simulations, state changes, and map overlays will work flawlessly for presentation purposes.
