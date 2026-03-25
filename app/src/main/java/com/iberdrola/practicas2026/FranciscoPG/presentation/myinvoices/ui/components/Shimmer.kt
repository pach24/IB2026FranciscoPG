package com.iberdrola.practicas2026.FranciscoPG.presentation.myinvoices.ui.components

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
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.unit.Dp
import com.iberdrola.practicas2026.FranciscoPG.presentation.theme.IberdrolaTheme
import com.iberdrola.practicas2026.FranciscoPG.presentation.theme.Radius

/**
 * Progreso normalizado (0f..1f) compartido por todos los ShimmerBox
 * dentro de un mismo [ShimmerHost]. Evita crear N transiciones infinitas
 * independientes cuando hay muchos skeletons en pantalla.
 */
val LocalShimmerProgress = compositionLocalOf { 0f }

@Composable
fun ShimmerHost(content: @Composable () -> Unit) {
    val transition = rememberInfiniteTransition(label = "shimmerShared")
    val progress by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1200, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "shimmerProgress"
    )
    CompositionLocalProvider(LocalShimmerProgress provides progress) {
        content()
    }
}

@Composable
fun ShimmerBox(
    width: Dp,
    height: Dp,
    modifier: Modifier = Modifier,
    shape: RoundedCornerShape = RoundedCornerShape(Radius.dp4)
) {
    val baseColor = IberdrolaTheme.colors.skeletonBackground
    val shimmerColors = listOf(
        baseColor.copy(alpha = 0.7f),
        baseColor.copy(alpha = 0.3f),
        baseColor.copy(alpha = 0.7f)
    )

    val progress = LocalShimmerProgress.current

    val brush = Brush.linearGradient(
        colors = shimmerColors,
        start = Offset(progress * 1000f - 500f, 0f),
        end = Offset(progress * 1000f, height.value)
    )

    Box(
        modifier = modifier
            .width(width)
            .height(height)
            .clip(shape)
            .background(brush)
    )
}
