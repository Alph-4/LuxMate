package org.julienjnnqin.luxmateapp.core.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val LightColorScheme = lightColorScheme(
    primary = PrimaryDark,
    onPrimary = androidx.compose.ui.graphics.Color.White,
    primaryContainer = PrimaryLight.copy(alpha = 0.1f),
    onPrimaryContainer = PrimaryDark,

    secondary = PrimaryLight,
    onSecondary = androidx.compose.ui.graphics.Color.White,
    secondaryContainer = PrimaryLight.copy(alpha = 0.1f),
    onSecondaryContainer = PrimaryLight,

    tertiary = SuccessGreen,
    onTertiary = androidx.compose.ui.graphics.Color.White,
    tertiaryContainer = SuccessGreen.copy(alpha = 0.1f),
    onTertiaryContainer = SuccessGreen,

    error = ErrorRed,
    onError = androidx.compose.ui.graphics.Color.White,
    errorContainer = ErrorRed.copy(alpha = 0.1f),
    onErrorContainer = ErrorRed,

    background = BackgroundLight,
    onBackground = Slate900,

    surface = SurfaceLight,
    onSurface = Slate900,
    surfaceVariant = Slate100,
    onSurfaceVariant = Slate600,

    outline = Slate300,
    outlineVariant = Slate200,
    scrim = androidx.compose.ui.graphics.Color.Black,
)

private val DarkColorScheme = darkColorScheme(
    primary = PrimaryLight,
    onPrimary = Slate900,
    primaryContainer = PrimaryLight.copy(alpha = 0.2f),
    onPrimaryContainer = androidx.compose.ui.graphics.Color.White,

    secondary = PrimaryLight,
    onSecondary = Slate900,
    secondaryContainer = PrimaryLight.copy(alpha = 0.2f),
    onSecondaryContainer = androidx.compose.ui.graphics.Color.White,

    tertiary = SuccessGreen,
    onTertiary = Slate900,
    tertiaryContainer = SuccessGreen.copy(alpha = 0.2f),
    onTertiaryContainer = androidx.compose.ui.graphics.Color.White,

    error = ErrorRed,
    onError = Slate900,
    errorContainer = ErrorRed.copy(alpha = 0.2f),
    onErrorContainer = ErrorRed,

    background = BackgroundDark,
    onBackground = Slate50,

    surface = SurfaceDark,
    onSurface = Slate50,
    surfaceVariant = Slate700,
    onSurfaceVariant = Slate400,

    outline = Slate500,
    outlineVariant = Slate700,
    scrim = androidx.compose.ui.graphics.Color.Black,
)

@Composable
fun LuxMateAppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = LuxMateTypography,
        content = content
    )
}

