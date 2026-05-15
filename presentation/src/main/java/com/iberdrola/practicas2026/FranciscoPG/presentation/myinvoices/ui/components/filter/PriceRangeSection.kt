package com.iberdrola.practicas2026.FranciscoPG.presentation.myinvoices.ui.components.filter
import com.iberdrola.practicas2026.FranciscoPG.presentation.R

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
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.res.stringResource
import com.iberdrola.practicas2026.FranciscoPG.presentation.theme.IberFontBold
import com.iberdrola.practicas2026.FranciscoPG.presentation.theme.IberFontRegular
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.iberdrola.practicas2026.FranciscoPG.presentation.theme.IberdrolaTheme
import com.iberdrola.practicas2026.FranciscoPG.presentation.theme.Radius
import com.iberdrola.practicas2026.FranciscoPG.presentation.theme.Spacing
import com.iberdrola.practicas2026.FranciscoPG.presentation.theme.TextSize
import kotlin.math.ceil
import kotlin.math.floor
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PriceRangeSection(
    minPrice: Float,
    maxPrice: Float,
    minLimit: Float = 0f,
    maxLimit: Float = 500f,
    onRangeChange: (Float, Float) -> Unit,
    onRangeChangeFinished: (Float, Float) -> Unit = { _, _ -> }
) {
    val colors = IberdrolaTheme.colors

    val animMin = remember { Animatable(minPrice) }
    val animMax = remember { Animatable(maxPrice) }
    var isDragging by remember { mutableStateOf(false) }
    var lastRange by remember { mutableStateOf(minPrice to maxPrice) }

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
                .padding(horizontal = Spacing.dp12, vertical = Spacing.dp4)
        ) {
            Text(
                text = stringResource(
                    R.string.filter_price_range,
                    floor(animMin.value).toInt(),
                    ceil(animMax.value).toInt()
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
                val minGap = 1f
                isDragging = true

                var newStart = range.start
                var newEnd = range.endInclusive


                if (newEnd - newStart < minGap) {

                    if (newStart != animMin.value) {

                        newStart = (newEnd - minGap).coerceAtLeast(minLimit)
                    } else {
                        newEnd = (newStart + minGap).coerceAtMost(maxLimit)
                    }
                }

                lastRange = newStart to newEnd
                onRangeChange(newStart, newEnd)
            },
            onValueChangeFinished = {
                isDragging = false
                onRangeChangeFinished(lastRange.first, lastRange.second)
            },
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

                    drawLine(
                        color = inactiveColor,
                        start = Offset(0f, trackHeight / 2),
                        end = Offset(width, trackHeight / 2),
                        strokeWidth = trackHeight,
                        cap = StrokeCap.Round
                    )
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

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = stringResource(R.string.filter_price_limit, floor(minLimit).toInt()),
                color = colors.textSubtitle,
                fontFamily = IberFontRegular,
                fontSize = TextSize.sp12
            )
            Text(
                text = stringResource(R.string.filter_price_limit, ceil(maxLimit).toInt()),
                color = colors.textSubtitle,
                fontFamily = IberFontRegular,
                fontSize = TextSize.sp12
            )
        }
    }
}