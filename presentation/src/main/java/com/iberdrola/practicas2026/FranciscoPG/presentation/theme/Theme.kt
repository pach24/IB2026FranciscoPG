package com.iberdrola.practicas2026.FranciscoPG.presentation.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val LightColorScheme: ColorScheme = lightColorScheme(
    primary = IberGreen,
    onPrimary = LightBackground,
    primaryContainer = LightPromo,
    onPrimaryContainer = IberDarkGreen,
    secondary = IberSky,
    onSecondary = LightBackground,
    background = LightBackground,
    onBackground = LightDarkGreyText,
    surface = LightSurface,
    onSurface = LightDarkGreyText,
    surfaceVariant = LightPromo,
    onSurfaceVariant = LightDarkGreyText,
    outline = LightDivider,
    error = ErrorLight,
    onError = LightBackground,
    errorContainer = ErrorLightContainer,
    onErrorContainer = ErrorLight
)

private val DarkColorScheme: ColorScheme = darkColorScheme(
    primary = IberGreenDark,
    onPrimary = DarkBackground,
    primaryContainer = DarkPromo,
    onPrimaryContainer = DarkTextPrimary,
    secondary = IberSky,
    onSecondary = DarkBackground,
    background = DarkBackground,
    onBackground = DarkTextPrimary,
    surface = DarkSurface,
    onSurface = DarkTextPrimary,
    surfaceVariant = DarkSurface,
    onSurfaceVariant = DarkTextPrimary,
    outline = DarkDivider,
    error = ErrorDark,
    onError = DarkBackground,
    errorContainer = ErrorDarkContainer,
    onErrorContainer = ErrorDark
)

object IberdrolaTheme {
    val colors: IberdrolaColors
        @Composable
        get() = LocalIberdrolaColors.current
}

@Composable
fun IberdrolaTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme
    val iberdrolaColors = if (darkTheme) DarkIberdrolaColors else LightIberdrolaColors
    val view = LocalView.current
    val context = LocalContext.current

    if (!view.isInEditMode) {
        SideEffect {
            val window = (context as Activity).window
            WindowCompat.getInsetsController(window, view)
                .isAppearanceLightStatusBars = !darkTheme
        }
    }

    CompositionLocalProvider(LocalIberdrolaColors provides iberdrolaColors) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = IberTypography,
            content = content
        )
    }
}
