package com.example.aistablediffusionprojectapp.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import com.example.aistablediffusionprojectapp.R

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

// Define custom font families
val montserratFontFamily = FontFamily(
    Font(R.font.montserrat_regular, FontWeight.Normal),
    Font(R.font.montserrat_bold, FontWeight.Bold)
)

val itModernaFontFamily = FontFamily(
    Font(R.font.playwritemoderna_regular, FontWeight.Normal)
)

// Define a data class to hold the custom theme values
data class AppTypography(
    val defaultFontFamily: FontFamily = FontFamily.Default,
    val fontSize: Int = 16
)

// Create a CompositionLocal to provide the custom theme values
val LocalAppTypography = staticCompositionLocalOf { AppTypography() }

// Create a composable function to provide the custom theme
@Composable
fun AppTheme(
    darkModeEnabled: Boolean = isSystemInDarkTheme(),
    useCustomFont: Boolean = false,
    selectedFont: String = "Default",
    appTypography: AppTypography = AppTypography(),
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        darkModeEnabled -> darkColorScheme(
            primary = Color(0xFFD0BCFF),
            secondary = Color(0xFFCCC2DC),
            tertiary = Color(0xFFEFB8C8)
        )

        else -> lightColorScheme(
            primary = Color(0xFF6650a4),
            secondary = Color(0xFF625b71),
            tertiary = Color(0xFF7D5260)
        )
    }
    val typography = Typography(
        bodyLarge = TextStyle(
            fontFamily = when {
                useCustomFont && selectedFont == "Font 1" -> montserratFontFamily
                useCustomFont && selectedFont == "Font 2" -> itModernaFontFamily
                else -> appTypography.defaultFontFamily
            },
            fontWeight = FontWeight.Normal,
            fontSize = appTypography.fontSize.sp,
            lineHeight = 24.sp,
            letterSpacing = 0.5.sp
        )
    )

    CompositionLocalProvider(LocalAppTypography provides appTypography) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = typography,
            content = content
        )
    }
}