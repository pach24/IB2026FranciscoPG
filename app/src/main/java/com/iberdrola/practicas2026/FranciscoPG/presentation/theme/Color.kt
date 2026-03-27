package com.iberdrola.practicas2026.FranciscoPG.presentation.theme

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

// ══════════════════════════════════════════
// BASE
// ══════════════════════════════════════════
val Black = Color(0xFF000000)
val White = Color(0xFFFFFFFF)

// ══════════════════════════════════════════
// MARCA IBERDROLA
// ══════════════════════════════════════════
val IberGreen = Color(0xFF006E31)
val IberSky = Color(0xFF0DA9FF)
val IberDarkGreen = Color(0xFF2D5F51)
val IberDarkGreenNight = Color(0xFF276553)
val IberGreenDark = Color(0xFF4DA570)
val IberSunset = Color(0xFFFF9C1A)

// ══════════════════════════════════════════
// FONDOS Y SUPERFICIES
// ══════════════════════════════════════════
val LightBackground = Color(0xFFFFFFFF)
val LightSurface = Color(0xFFFFFFFF)
val LightPromo = Color(0xFFD9F1E3)
val LightSkeletonBg = Color(0xFFE5ECE8)

val DarkBackground = Color(0xFF121212)
val DarkSurface = Color(0xFF1E1E1E)
val DarkPromo = Color(0xFF1A3326)
val DarkSkeletonBg = Color(0xFF2C2C2C)

// ══════════════════════════════════════════
// TEXTO
// ══════════════════════════════════════════
val LightTextPrimary = Color(0xFF000000)
val LightTextHighEmphasis = Color(0xFF121212)
val LightDarkGreyText = Color(0xFF333333)
val LightDarkGrey = Color(0xFF3A3A3A)
val LightTextSubtitle = Color(0xFF7A8585)
val LightLightGrey = Color(0xFF6C6C6C)
val LightTextOnPrimary = Color(0xFFFFFFFF)

val DarkTextPrimary = Color(0xFFE3E3E3)
val DarkTextHighEmphasis = Color(0xFFF5F5F5)
val DarkDarkGreyText = Color(0xFFE3E3E3)
val DarkDarkGrey = Color(0xFFCCCCCC)
val DarkTextSubtitle = Color(0xFF8A9696)
val DarkLightGrey = Color(0xFF999999)
val DarkTextOnPrimary = Color(0xFF121212)

// ══════════════════════════════════════════
// BORDES Y SEPARADORES
// ══════════════════════════════════════════
val LightDivider = Color(0xFFE5E8E8)
val LightStrokeNeutral = Color(0xFFD9D9D9)

val DarkDivider = Color(0xFF333333)
val DarkStrokeNeutral = Color(0xFF444444)

// ══════════════════════════════════════════
// ESTADO / SEMÁNTICO
// ══════════════════════════════════════════
val StatusPaidLight = Color(0xFFB1DEC8)

val StatusPaidDark = Color(0xFF173B2A)
val ErrorLight = Color(0xFFB72727)
val ErrorLightContainer = Color(0xFFF1BFBF)
val ErrorDark = Color(0xFFE57373)
val ErrorDarkContainer = Color(0xFF4A1919)
val StatusDefaultLight = Color(0xFFE7E7E7)
val StatusDefaultDark = Color(0xFF3A3A3A)
val StatusDefaultTextLight = Color(0xFF6C6C6C)
val StatusDefaultTextDark = Color(0xFF999999)

// ══════════════════════════════════════════
// COMPONENTES UI
// ══════════════════════════════════════════
val LightHandlerColor = Color(0xFFC3D5CF)
val DarkHandlerColor = Color(0xFF4A5954)
val LightTabMisFacturas = Color(0xFFC6D7D1)
val DarkTabMisFacturas = Color(0xFF4A5954)
val SnackbarYellow = Color(0xFFFFE7A9)
val IconSnackbar = Color(0xFF8A4C00)
val SnackbarGreen =Color(0xFF89CB8C)
val LightBadgeAmountFilter = Color(0xFFDBE9E1)
val DarkBadgeAmountFilter = Color(0xFF253D33)

val StatusActiveLight = Color(0xFFB1DEC8)
val StatusActiveDark = Color(0xFF173B2A)

val buttonDisabledLight = Color(0xFFECF3EF)
val buttonActiveLight = Color(0xFF253D33)

val buttonDisabledDark = Color(0xFF1C1C1C)
val buttonActiveDark = Color(0xFF253D33)

val buttonTextDisabledLight = Color(0xFFA6BEB6)
val buttonTextActiveLight = Color(0xFFFFFFFF)

val buttonTextDisabledDark = Color(0xFF3A3A3A)
val buttonTextActiveDark = Color(0xFFFFFFFF)

val errorTextForm = Color(0xFFFF2C2C)

val InfoBannerBgLight = Color(0xFFDDF5FF)
val InfoBannerBgDark = Color(0x8D183241)
val InfoBannerIconLight = Color(0xFF626262)
val InfoBannerIconDark = Color(0xFF97BFCC)

// ══════════════════════════════════════════
// LOADING
// ══════════════════════════════════════════
val LoadingRailLight = Color(0xFF8C938F)
val LoadingRailDark  = Color(0xFF2E4A47)

// ══════════════════════════════════════════
// SUCCESS BANNER
// ══════════════════════════════════════════
val SuccessBannerBgLight = Color(0xFFB1DEC8)
val SuccessBannerBgDark  = Color(0xFF459355)

// ══════════════════════════════════════════
// PALETA SEMÁNTICA (light / dark aware)
// ══════════════════════════════════════════
@Immutable
data class IberdrolaColors(
    val iberdrolaGreen: Color,
    val iberdrolaDarkGreen: Color,
    val brandPrimary: Color,
    val background: Color,
    val surface: Color,
    val promoBackground: Color,
    val skeletonBackground: Color,
    val textPrimary: Color,
    val textHighEmphasis: Color,
    val darkGreyText: Color,
    val darkGrey: Color,
    val textSubtitle: Color,
    val lightGrey: Color,
    val textOnPrimary: Color,
    val divider: Color,
    val strokeNeutral: Color,
    val statusPaid: Color,
    val errorText: Color,
    val errorContainer: Color,
    val handlerColor: Color,
    val tabMisFacturas: Color,
    val snackbar: Color,
    val snackbarGreen: Color,
    val black: Color,
    val white: Color,
    val badgeAmountFilter: Color,
    val statusDefault: Color,
    val statusDefaultText: Color,
    val activeFilterBadgeColor: Color,
    val snackbarIcon: Color,
    val statusActive: Color,
    val buttonDisabled: Color,
    val buttonActive : Color,
    val buttonTextDisabled : Color,
    val buttonTextActive : Color,
    val errorTextForm: Color,
    val infoBannerBackground: Color,
    val infoBannerIcon: Color,
    val loadingSpinnerRail: Color,
    val successBannerBackground: Color,
)

val LightIberdrolaColors = IberdrolaColors(
    iberdrolaGreen = IberGreen,
    iberdrolaDarkGreen = IberDarkGreen,
    brandPrimary = IberGreen,
    background = LightBackground,
    surface = LightSurface,
    promoBackground = LightPromo,
    skeletonBackground = LightSkeletonBg,
    textPrimary = LightTextPrimary,
    textHighEmphasis = LightTextHighEmphasis,
    darkGreyText = LightDarkGreyText,
    darkGrey = LightDarkGrey,
    textSubtitle = LightTextSubtitle,
    lightGrey = LightLightGrey,
    textOnPrimary = LightTextOnPrimary,
    divider = LightDivider,
    strokeNeutral = LightStrokeNeutral,
    statusPaid = StatusPaidLight,
    errorText = ErrorLight,
    errorContainer = ErrorLightContainer,
    handlerColor = LightHandlerColor,
    tabMisFacturas = LightTabMisFacturas,
    snackbar = SnackbarYellow,
    snackbarGreen = SnackbarGreen,
    black = Black,
    white = White,
    badgeAmountFilter = LightBadgeAmountFilter,
    statusDefault = StatusDefaultLight,
    statusDefaultText = StatusDefaultTextLight,
    activeFilterBadgeColor = IberSunset,
    snackbarIcon = IconSnackbar,
    statusActive = StatusActiveLight,
    buttonActive = buttonActiveLight,
    buttonDisabled = buttonDisabledLight,
    buttonTextActive = buttonTextActiveLight,
    buttonTextDisabled = buttonTextDisabledLight,
    errorTextForm = errorTextForm,
    infoBannerBackground = InfoBannerBgLight,
    infoBannerIcon = InfoBannerIconLight,
    loadingSpinnerRail = LoadingRailLight,
    successBannerBackground = SuccessBannerBgLight,
)

val DarkIberdrolaColors = IberdrolaColors(
    iberdrolaGreen = IberGreen,
    iberdrolaDarkGreen = IberDarkGreenNight,
    brandPrimary = IberGreenDark,
    background = DarkBackground,
    surface = DarkSurface,
    promoBackground = DarkPromo,
    skeletonBackground = DarkSkeletonBg,
    textPrimary = DarkTextPrimary,
    textHighEmphasis = DarkTextHighEmphasis,
    darkGreyText = DarkDarkGreyText,
    darkGrey = DarkDarkGrey,
    textSubtitle = DarkTextSubtitle,
    lightGrey = DarkLightGrey,
    textOnPrimary = DarkTextOnPrimary,
    divider = DarkDivider,
    strokeNeutral = DarkStrokeNeutral,
    statusPaid = StatusPaidDark,
    errorText = ErrorDark,
    errorContainer = ErrorDarkContainer,
    handlerColor = DarkHandlerColor,
    tabMisFacturas = DarkTabMisFacturas,
    snackbar = SnackbarYellow,
    snackbarGreen = SnackbarGreen,
    black = Black,
    white = White,
    badgeAmountFilter = DarkBadgeAmountFilter,
    statusDefault = StatusDefaultDark,
    statusDefaultText = StatusDefaultTextDark,
    activeFilterBadgeColor = IberSunset,
    snackbarIcon = IconSnackbar,
    statusActive = StatusActiveDark,
    buttonActive = buttonActiveDark,
    buttonDisabled = buttonDisabledDark,
    buttonTextActive = buttonTextActiveDark,
    buttonTextDisabled = buttonTextDisabledDark,
    errorTextForm = errorTextForm,
    infoBannerBackground = InfoBannerBgDark,
    infoBannerIcon = InfoBannerIconDark,
    loadingSpinnerRail = LoadingRailDark,
    successBannerBackground = SuccessBannerBgDark,
)

val LocalIberdrolaColors = staticCompositionLocalOf { LightIberdrolaColors }
