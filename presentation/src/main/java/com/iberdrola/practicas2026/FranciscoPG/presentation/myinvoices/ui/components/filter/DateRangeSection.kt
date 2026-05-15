package com.iberdrola.practicas2026.FranciscoPG.presentation.myinvoices.ui.components.filter
import com.iberdrola.practicas2026.FranciscoPG.presentation.R

import android.content.res.Configuration
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.animation.animateColor
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.iberdrola.practicas2026.FranciscoPG.presentation.theme.IberFontBold
import com.iberdrola.practicas2026.FranciscoPG.presentation.theme.IberdrolaTheme
import com.iberdrola.practicas2026.FranciscoPG.presentation.theme.Spacing
import com.iberdrola.practicas2026.FranciscoPG.presentation.theme.TextSize

@Composable
fun DateRangeSection(
    dateFrom: String,
    dateTo: String,
    onFromClick: () -> Unit,
    onToClick: () -> Unit,
    onFromClear: () -> Unit = {},
    onToClear: () -> Unit = {}
) {
    val colors = IberdrolaTheme.colors
    Column {
        Text(
            text = stringResource(R.string.filter_date_section_title),
            fontSize = TextSize.sp13,
            fontWeight = FontWeight.Bold,
            fontFamily = IberFontBold,
            color = colors.darkGrey
        )
        Spacer(modifier = Modifier.height(Spacing.dp8))
        Row(
            horizontalArrangement = Arrangement.spacedBy(Spacing.dp32),
            modifier = Modifier.fillMaxWidth()
        ) {
            DateField(
                label = stringResource(R.string.filter_date_from_label),
                value = dateFrom,
                modifier = Modifier.weight(1f),
                onClick = onFromClick,
                onClear = onFromClear
            )
            DateField(
                label = stringResource(R.string.filter_date_to_label),
                value = dateTo,
                modifier = Modifier.weight(1f),
                onClick = onToClick,
                onClear = onToClear
            )
        }
    }
}

@Composable
private fun DateField(
    label: String,
    value: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    onClear: () -> Unit
) {
    val colors = IberdrolaTheme.colors
    val hasValue = value.isNotEmpty()

    var displayValue by remember { mutableStateOf(value) }
    if (value.isNotEmpty()) displayValue = value

    val transition = updateTransition(targetState = hasValue, label = "dateField")

    val dividerColor by transition.animateColor(
        transitionSpec = { tween(300) },
        label = "dividerColor"
    ) { if (it) colors.iberdrolaGreen else colors.darkGrey }

    val dividerThickness by transition.animateFloat(
        transitionSpec = { tween(300) },
        label = "dividerThickness"
    ) { if (it) 2f else 1f }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onClick
            )
    ) {
        // Área fija para el label flotante (siempre reserva espacio)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(16.dp),
            contentAlignment = Alignment.BottomStart
        ) {
            transition.AnimatedVisibility(
                visible = { it },
                enter = fadeIn(tween(300)) + slideInVertically(tween(300)) { it * 3 },
                exit = fadeOut(tween(200)) + slideOutVertically(tween(200)) { it * 3 }
            ) {
                Text(
                    text = label,
                    fontSize = TextSize.sp10,
                    color = colors.textSubtitle
                )
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(Spacing.dp32),
            verticalAlignment = Alignment.Bottom
        ) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .padding(bottom = 4.dp)
            ) {
                // Placeholder: visible cuando no hay fecha, sale hacia arriba
                transition.AnimatedVisibility(
                    visible = { !it },
                    enter = fadeIn(tween(200)) + slideInVertically(tween(200)) { -it },
                    exit = fadeOut(tween(300)) + slideOutVertically(tween(300)) { -it }
                ) {
                    Text(
                        text = label,
                        fontSize = TextSize.sp14,
                        color = colors.textSubtitle
                    )
                }
                // Fecha: entra desde abajo cuando se selecciona
                transition.AnimatedVisibility(
                    visible = { it },
                    enter = fadeIn(tween(300)) + slideInVertically(tween(300)) { -it },
                    exit = fadeOut(tween(200)) + slideOutVertically(tween(200)) { it }
                ) {
                    Text(
                        text = displayValue,
                        modifier = Modifier.fillMaxWidth(),
                        fontSize = TextSize.sp15,
                        color = colors.darkGreyText,
                        textAlign = TextAlign.Center
                    )
                }
            }
            Crossfade(
                targetState = hasValue,
                animationSpec = tween(300),
                label = "iconCrossfade"
            ) { showClear ->
                Icon(
                    painter = painterResource(
                        if (showClear) R.drawable.ic_close else R.drawable.ic_calendar
                    ),
                    contentDescription = null,
                    modifier = Modifier
                        .size(Spacing.dp24)
                        .then(
                            if (showClear) Modifier.clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = null,
                                onClick = onClear
                            ) else Modifier
                        ),
                    tint = colors.textSubtitle
                )
            }
        }

        Spacer(modifier = Modifier.height(Spacing.dp6))

        HorizontalDivider(
            color = dividerColor,
            thickness = dividerThickness.dp
        )
    }
}

@Preview(name = "Vacío - Light", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_NO)
@Composable
private fun DateRangeSectionEmptyPreview() {
    IberdrolaTheme {
        Box(modifier = Modifier.padding(16.dp)) {
            DateRangeSection(
                dateFrom = "",
                dateTo = "",
                onFromClick = {},
                onToClick = {}
            )
        }
    }
}

@Preview(name = "Con fechas - Light", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_NO, widthDp = 360, heightDp = 120)
@Composable
private fun DateRangeSectionFilledPreview() {
    IberdrolaTheme {
        Box(modifier = Modifier.padding(16.dp)) {
            DateRangeSection(
                dateFrom = "01/01/2026",
                dateTo = "15/02/2026",
                onFromClick = {},
                onToClick = {}
            )
        }
    }
}

@Preview(name = "Animación - Inspector", showBackground = true, widthDp = 360, heightDp = 200)
@Composable
private fun DateFieldAnimationInspectorPreview() {
    IberdrolaTheme {
        var hasDate by remember { mutableStateOf(false) }

        Column(
            modifier = Modifier
                .padding(24.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            DateRangeSection(
                dateFrom = if (hasDate) "01/01/2026" else "",
                dateTo = if (hasDate) "15/02/2026" else "",
                onFromClick = {},
                onToClick = {},
                onFromClear = { hasDate = false },
                onToClear = { hasDate = false }
            )

            Spacer(modifier = Modifier.height(32.dp))

            androidx.compose.material3.Button(
                onClick = { hasDate = !hasDate }
            ) {
                Text(if (hasDate) "← Quitar fecha" else "Seleccionar fecha →")
            }
        }
    }
}
