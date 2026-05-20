package com.muhammadkaleemakhtar.aiden.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.googlefonts.Font
import androidx.compose.ui.text.googlefonts.GoogleFont
import androidx.compose.ui.unit.sp
import com.muhammadkaleemakhtar.aiden.R

/**
 * Configure Google Font provider with downloadable font certificates resource.
 */
val provider = GoogleFont.Provider(
    providerAuthority = "com.google.android.gms.fonts",
    providerPackage = "com.google.android.gms",
    certificates = R.array.com_google_android_gms_fonts_certs
)

// Font Families
val PlayfairDisplayFont = GoogleFont("Playfair Display")
val PlayfairDisplayFontFamily = FontFamily(
    Font(googleFont = PlayfairDisplayFont, fontProvider = provider, weight = FontWeight.Bold),
    Font(googleFont = PlayfairDisplayFont, fontProvider = provider, weight = FontWeight.SemiBold),
    Font(googleFont = PlayfairDisplayFont, fontProvider = provider, weight = FontWeight.Medium),
    Font(googleFont = PlayfairDisplayFont, fontProvider = provider, weight = FontWeight.Normal)
)

val InterFont = GoogleFont("Inter")
val InterFontFamily = FontFamily(
    Font(googleFont = InterFont, fontProvider = provider, weight = FontWeight.Normal),
    Font(googleFont = InterFont, fontProvider = provider, weight = FontWeight.Medium),
    Font(googleFont = InterFont, fontProvider = provider, weight = FontWeight.Bold)
)

val DmSansFont = GoogleFont("DM Sans")
val DmSansFontFamily = FontFamily(
    Font(googleFont = DmSansFont, fontProvider = provider, weight = FontWeight.Medium),
    Font(googleFont = DmSansFont, fontProvider = provider, weight = FontWeight.Normal),
    Font(googleFont = DmSansFont, fontProvider = provider, weight = FontWeight.Bold)
)

val JetBrainsMonoFont = GoogleFont("JetBrains Mono")
val JetBrainsMonoFontFamily = FontFamily(
    Font(googleFont = JetBrainsMonoFont, fontProvider = provider, weight = FontWeight.Normal)
)

// Set of Material typography styles to start with
val Typography = Typography(
    displayLarge = TextStyle(
        fontFamily = PlayfairDisplayFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 34.sp
    ),
    displayMedium = TextStyle(
        fontFamily = PlayfairDisplayFontFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 28.sp
    ),
    headlineMedium = TextStyle(
        fontFamily = PlayfairDisplayFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 22.sp
    ),
    bodyLarge = TextStyle(
        fontFamily = InterFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp
    ),
    bodyMedium = TextStyle(
        fontFamily = InterFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp
    ),
    labelLarge = TextStyle(
        fontFamily = DmSansFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp
    ),
    labelSmall = TextStyle(
        fontFamily = DmSansFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 12.sp
    )
)