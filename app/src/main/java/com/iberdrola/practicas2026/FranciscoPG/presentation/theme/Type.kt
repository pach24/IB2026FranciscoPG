package com.iberdrola.practicas2026.FranciscoPG.presentation.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.iberdrola.practicas2026.FranciscoPG.R

private val IberPangea = FontFamily(
    Font(R.font.iberpangea_regular, FontWeight.Normal),
    Font(R.font.iberpangea_bold, FontWeight.Bold)
)

val IberFontBold = FontFamily(Font(R.font.iberpangea_bold, FontWeight.Bold))
val IberFontRegular = FontFamily(Font(R.font.iberpangea_regular, FontWeight.Normal))

val IberTypography = Typography(
    bodyLarge = TextStyle(
        fontFamily = IberPangea,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp
    ),
    bodyMedium = TextStyle(
        fontFamily = IberPangea,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp
    ),
    titleLarge = TextStyle(
        fontFamily = IberPangea,
        fontWeight = FontWeight.Bold,
        fontSize = 20.sp
    ),
    labelLarge = TextStyle(
        fontFamily = IberPangea,
        fontWeight = FontWeight.Bold,
        fontSize = 14.sp
    )
)
