package com.iberdrola.practicas2026.FranciscoPG.presentation.invoices.ui.components.filter

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.RangeSlider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.res.stringResource
import com.iberdrola.practicas2026.FranciscoPG.presentation.theme.IberFontBold
import com.iberdrola.practicas2026.FranciscoPG.presentation.theme.IberFontRegular
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.iberdrola.practicas2026.FranciscoPG.R
import com.iberdrola.practicas2026.FranciscoPG.presentation.theme.IberdrolaTheme
import com.iberdrola.practicas2026.FranciscoPG.presentation.theme.Radius
import com.iberdrola.practicas2026.FranciscoPG.presentation.theme.Spacing
import com.iberdrola.practicas2026.FranciscoPG.presentation.theme.TextSize
import kotlinx.coroutines.launch

// Sección de rango de importe con badge central, slider de doble thumb y etiquetas de límites
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PriceRangeSection(
    minPrice: Float,
    maxPrice: Float,
    minLimit: Float = 0f,
    maxLimit: Float = 500f,
    onRangeChange: (Float, Float) -> Unit
) {
    val colors = IberdrolaTheme.colors

    val animMin = remember { Animatable(minPrice) }
    val animMax = remember { Animatable(maxPrice) }
    var isDragging by remember { mutableStateOf(false) }

    LaunchedEffect(minPrice, maxPrice) {
        if (isDragging) {
            animMin.snapTo(minPrice)
            animMax.snapTo(maxPrice)
        } else {
            launch { animMin.animateTo(minPrice, tween(150)) }
            launch { animMax.animateTo(maxPrice, tween(150)) }
        }
    }

    Column(modifier = Modifier.fillMaxWidth()) {
        // Badge con el rango actual
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .background(colors.badgeAmountFilter, RoundedCornerShape(Radius.dp4))
                .align(Alignment.CenterHorizontally)
                .padding(horizontal = Spacing.dp12, vertical = Spacing.dp8)
        ) {
            Text(
                text = stringResource(
                    R.string.filter_price_range,
                    animMin.value.toInt(),
                    animMax.value.toInt()
                ),
                fontFamily = IberFontBold,
                fontWeight = FontWeight.Bold,
                fontSize = TextSize.sp12,
                style = TextStyle(
                    platformStyle = PlatformTextStyle(includeFontPadding = false)
                )
            )
        }

        RangeSlider(
            value = animMin.value..animMax.value,
            onValueChange = { range ->
                isDragging = true
                val startValue = if (range.start < minLimit + 0.1f) minLimit else range.start
                val endValue = if (range.endInclusive > maxLimit - 0.1f) maxLimit else range.endInclusive
                onRangeChange(startValue, endValue)
            },
            onValueChangeFinished = { isDragging = false },
            valueRange = minLimit..maxLimit,
            steps = 0,
            modifier = Modifier.fillMaxWidth(),
            startThumb = {
                Box(
                    modifier = Modifier
                        .size(22.dp)
                        .background(colors.iberdrolaGreen, shape = CircleShape)
                )
            },
            endThumb = {
                Box(
                    modifier = Modifier
                        .size(22.dp)
                        .background(colors.iberdrolaGreen, shape = CircleShape)
                )
            },
            track = { rangeSliderState ->
                val trackColor = colors.iberdrolaGreen
                val inactiveColor = colors.divider
                Canvas(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(4.dp)
                ) {
                    val trackHeight = size.height
                    val width = size.width
                    val startPos = width * ((rangeSliderState.activeRangeStart - minLimit) / (maxLimit - minLimit))
                    val endPos = width * ((rangeSliderState.activeRangeEnd - minLimit) / (maxLimit - minLimit))

                    // Línea inactiva (fondo)
                    drawLine(
                        color = inactiveColor,
                        start = Offset(0f, trackHeight / 2),
                        end = Offset(width, trackHeight / 2),
                        strokeWidth = trackHeight,
                        cap = StrokeCap.Round
                    )
                    // Línea activa (verde)
                    drawLine(
                        color = trackColor,
                        start = Offset(startPos, trackHeight / 2),
                        end = Offset(endPos, trackHeight / 2),
                        strokeWidth = trackHeight,
                        cap = StrokeCap.Round
                    )
                }
            }
        )

        // Etiquetas de extremos
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = stringResource(R.string.filter_price_limit, minLimit.toInt()),
                color = colors.textSubtitle,
                fontFamily = IberFontRegular,
                fontSize = TextSize.sp12
            )
            Text(
                text = stringResource(R.string.filter_price_limit, maxLimit.toInt()),
                color = colors.textSubtitle,
                fontFamily = IberFontRegular,
                fontSize = TextSize.sp12
            )
        }
    }
}
