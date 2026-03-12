package com.iberdrola.practicas2026.FranciscoPG.presentation.invoices.ui.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntSize
import com.iberdrola.practicas2026.FranciscoPG.R

@Composable
fun ShimmerBox(
    width: Dp,
    height: Dp,
    modifier: Modifier = Modifier,
    shape: RoundedCornerShape = RoundedCornerShape(dimensionResource(R.dimen.m3_comp_skeleton_corner_radius))
) {
    val baseColor = colorResource(R.color.color_skeleton_background)
    val shimmerColors = listOf(
        baseColor.copy(alpha = 0.7f),
        baseColor.copy(alpha = 0.3f),
        baseColor.copy(alpha = 0.7f)
    )

    val transition = rememberInfiniteTransition(label = "shimmer")
    val sizeState = remember { mutableStateOf(IntSize.Zero) }
    val widthPx = sizeState.value.width.toFloat()
    val heightPx = sizeState.value.height.toFloat()

    val offsetX by transition.animateFloat(
        initialValue = -widthPx,
        targetValue = widthPx * 2f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1200, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "shimmerOffset"
    )

    val brush = Brush.linearGradient(
        colors = shimmerColors,
        start = Offset(offsetX, 0f),
        end = Offset(offsetX + widthPx, heightPx)
    )

    Box(
        modifier = modifier
            .width(width)
            .height(height)
            .onGloballyPositioned { sizeState.value = it.size }
            .clip(shape)
            .background(brush)
    )
}

