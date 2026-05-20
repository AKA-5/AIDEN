package com.muhammadkaleemakhtar.aiden.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

// Dark Color Scheme - Warm, rich tones (no blues/neons)
private val DarkColorScheme = darkColorScheme(
    primary = Terracotta,
    onPrimary = PureWhite,
    secondary = WarmSand,
    onSecondary = DarkBrown,
    background = DarkBrown,
    onBackground = WarmWhite,
    surface = Color(0xFF3D271D), // Darker Warm Brown
    onSurface = WarmWhite,
    error = CoralRed,
    onError = PureWhite,
    outline = MutedBrown
)

// Light Color Scheme - Soft, warm sand tones
private val LightColorScheme = lightColorScheme(
    primary = Terracotta,
    onPrimary = PureWhite,
    secondary = WarmSand,
    onSecondary = DarkBrown,
    background = WarmWhite,
    onBackground = DarkBrown,
    surface = PureWhite,
    onSurface = DarkBrown,
    error = CoralRed,
    onError = PureWhite,
    outline = WarmGray
)

@Composable
fun AIDENTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is premium, but MASTER_PLAN asks for strict color palette.
    // Thus we set dynamicColor to false by default to respect color constraints.
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.background.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}