package com.iberdrola.practicas2026.FranciscoPG.presentation.common

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.iberdrola.practicas2026.FranciscoPG.R

@Composable
fun RoundedCheckbox(
    checked: Boolean,
    checkedColor: Color,
    uncheckedBorderColor: Color,
    checkmarkColor: Color,
    modifier: Modifier = Modifier
) {
    val boxSize = 22.dp
    val cornerRadius = 3.dp

    val bgColor by animateColorAsState(
        targetValue = if (checked) checkedColor else Color.Transparent,
        animationSpec = tween(durationMillis = 200),
        label = "checkboxBg"
    )
    val borderColor by animateColorAsState(
        targetValue = if (checked) checkedColor else uncheckedBorderColor,
        animationSpec = tween(durationMillis = 200),
        label = "checkboxBorder"
    )
    val checkScale by animateFloatAsState(
        targetValue = if (checked) 1f else 0f,
        animationSpec = tween(durationMillis = 200),
        label = "checkScale"
    )

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .size(boxSize)
            .background(bgColor, RoundedCornerShape(cornerRadius))
            .border(1.5.dp, borderColor, RoundedCornerShape(cornerRadius))
    ) {
        if (checkScale > 0f) {
            Icon(
                painter = painterResource(R.drawable.ic_check),
                contentDescription = null,
                tint = checkmarkColor,
                modifier = Modifier
                    .size(12.dp)
                    .scale(checkScale)
            )
        }
    }
}
