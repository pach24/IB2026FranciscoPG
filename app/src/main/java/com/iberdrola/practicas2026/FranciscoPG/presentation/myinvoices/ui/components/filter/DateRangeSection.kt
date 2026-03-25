package com.iberdrola.practicas2026.FranciscoPG.presentation.myinvoices.ui.components.filter

import android.content.res.Configuration
import androidx.compose.animation.Crossfade
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
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
import androidx.compose.foundation.layout.wrapContentHeight
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
import androidx.compose.ui.unit.sp
import com.iberdrola.practicas2026.FranciscoPG.R
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
        Spacer(modifier = Modifier.height(Spacing.dp20))
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

// Campo de fecha individual con floating label animado.
// El label morphea entre placeholder (sp12) y floating label (sp10).
// El valor de la fecha aparece/desaparece con fade, centrado.
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

    val labelSize by animateFloatAsState(
        targetValue = if (hasValue) 10f else 12f,
        animationSpec = tween(300),
        label = "labelSize"
    )
    val dividerColor by animateColorAsState(
        targetValue = if (hasValue) colors.iberdrolaGreen else colors.strokeNeutral,
        animationSpec = tween(300),
        label = "dividerColor"
    )
    val dividerThickness by animateFloatAsState(
        targetValue = if (hasValue) 2f else 1f,
        animationSpec = tween(300),
        label = "dividerThickness"
    )

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onClick
            )
    ) {
        // Label: Usamos wrapContentHeight y un padding controlado para evitar que se corte
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(top = 8.dp)
        ) {
            Text(
                text = label,
                fontSize = labelSize.sp,
                color = colors.textSubtitle
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(Spacing.dp32),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .padding(top = 4.dp) // Baja el texto de la fecha un poco hacia la barra
            ) {
                androidx.compose.animation.AnimatedVisibility(
                    visible = hasValue,
                    enter = fadeIn(tween(300)) + slideInVertically(tween(300)) { -it },
                    exit = fadeOut(tween(300)) + slideOutVertically(tween(300)) { -it }
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

        // Espacio pequeño para que el texto casi toque la barra
        Spacer(modifier = Modifier.height(2.dp))

        HorizontalDivider(
            color = dividerColor,
            thickness = dividerThickness.dp
        )
    }
}

// --- Previews ---

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

@Preview(name = "Con fechas - Light", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_NO)
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
