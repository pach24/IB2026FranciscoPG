package com.iberdrola.practicas2026.FranciscoPG.presentation.invoices.ui.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.PagerState
import androidx.compose.material3.TabPosition
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.lerp

// ── Geometria del indicador (testable, pura) ─────────────────────────────────

/**
 * Resultado del calculo de posicion y tamaño del indicador stretch.
 */
@Stable
data class StretchIndicatorGeometry(
    val left: Dp,
    val width: Dp
)

/**
 * Calcula la geometria del indicador con efecto stretch (leading/trailing).
 *
 * @param position      posicion continua del indicador (0f = primer tab, 1f = segundo, etc.)
 * @param tabPositions  posiciones de los tabs proporcionadas por ScrollableTabRow
 * @param tabPadding    padding horizontal interno a cada tab
 */
fun computeStretchGeometry(
    position: Float,
    tabPositions: List<TabPosition>,
    tabPadding: Dp = 20.dp
): StretchIndicatorGeometry {
    val fromIndex = position.toInt().coerceIn(0, tabPositions.lastIndex)
    val toIndex = (fromIndex + 1).coerceAtMost(tabPositions.lastIndex)
    val progress = (position - fromIndex).coerceIn(0f, 1f)

    val fromTab = tabPositions[fromIndex]
    val toTab = tabPositions[toIndex]

    val leadingProgress = (progress * 2f).coerceAtMost(1f)
    val trailingProgress = ((progress - 0.5f) * 2f).coerceAtLeast(0f)

    val indicatorLeft = lerp(fromTab.left + tabPadding, toTab.left + tabPadding, trailingProgress)
    val indicatorRight = lerp(fromTab.right - tabPadding, toTab.right - tabPadding, leadingProgress)

    return StretchIndicatorGeometry(
        left = indicatorLeft,
        width = indicatorRight - indicatorLeft
    )
}

// ── Animacion de posicion (testable) ─────────────────────────────────────────

private const val CLICK_DURATION_MS = 500

/**
 * Determina el targetValue correcto para la animacion del indicador.
 *
 * - Click en tab  → destino fijo (targetPage) para que el tween de 100ms
 *                   controle la velocidad, sin perseguir el rawPosition del pager.
 * - Swipe manual  → rawPosition para seguir el dedo del usuario en tiempo real.
 */
@OptIn(ExperimentalFoundationApi::class)
fun resolveIndicatorTarget(
    pagerState: PagerState,
    isTabClick: Boolean
): Float {
    return if (isTabClick) {
        pagerState.targetPage.toFloat()
    } else {
        pagerState.currentPage + pagerState.currentPageOffsetFraction
    }
}

// ── Composable ───────────────────────────────────────────────────────────────

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun StretchTabIndicator(
    tabPositions: List<TabPosition>,
    pagerState: PagerState,
    isTabClick: Boolean,
    color: Color,
    indicatorHeight: Dp = 5.dp
) {
    val target = resolveIndicatorTarget(pagerState, isTabClick)

    val animatedPosition by animateFloatAsState(
        targetValue = target,
        animationSpec = if (isTabClick) {
            tween(durationMillis = CLICK_DURATION_MS, easing = FastOutSlowInEasing)
        } else {
            tween(durationMillis = 0)
        },
        label = "tabIndicatorPosition"
    )

    val position = if (isTabClick) animatedPosition else target

    val geometry = computeStretchGeometry(
        position = position,
        tabPositions = tabPositions
    )

    Box(Modifier.fillMaxWidth().height(indicatorHeight)) {
        Box(
            Modifier
                .align(Alignment.BottomStart)
                .offset(x = geometry.left)
                .width(geometry.width)
                .height(indicatorHeight)
                .background(color = color)
        )
    }
}
