package com.iberdrola.practicas2026.FranciscoPG.presentation.invoices.ui.components.filter

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import com.iberdrola.practicas2026.FranciscoPG.presentation.theme.IberFontBold
import com.iberdrola.practicas2026.FranciscoPG.R
import com.iberdrola.practicas2026.FranciscoPG.presentation.theme.IberdrolaTheme
import com.iberdrola.practicas2026.FranciscoPG.presentation.theme.Spacing
import com.iberdrola.practicas2026.FranciscoPG.presentation.theme.TextSize

// Sección de rango de fechas con campos "Desde" y "Hasta" que abren un DatePicker
@Composable
fun DateRangeSection(
    dateFrom: String,
    dateTo: String,
    onFromClick: () -> Unit,
    onToClick: () -> Unit
) {
    val colors = IberdrolaTheme.colors
    Column {
        Text(
            text = stringResource(R.string.filter_date_section_title),
            fontSize = TextSize.sp12,
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
                onClick = onFromClick
            )
            DateField(
                label = stringResource(R.string.filter_date_to_label),
                value = dateTo,
                modifier = Modifier.weight(1f),
                onClick = onToClick
            )
        }
    }
}

// Campo de fecha individual con icono de calendario, read-only, abre DatePicker al pulsar.
// Usa enabled=true + readOnly=true para que Material3 anime el label al tener valor.
@Composable
private fun DateField(
    label: String,
    value: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    val colors = IberdrolaTheme.colors
    val interactionSource = remember { MutableInteractionSource() }

    LaunchedEffect(interactionSource) {
        interactionSource.interactions.collect { interaction ->
            if (interaction is PressInteraction.Release) onClick()
        }
    }

    TextField(
        value = value,
        onValueChange = { },
        readOnly = true,
        label = { Text(label, fontSize = TextSize.sp12, modifier = Modifier.padding(top = Spacing.dp0)) },
        modifier = modifier.fillMaxWidth(),
        interactionSource = interactionSource,
        textStyle = TextStyle(fontSize = TextSize.sp12),
        trailingIcon = {
            Icon(painterResource(R.drawable.ic_calendar), null, tint = colors.textSubtitle)
        },
        colors = TextFieldDefaults.colors(
            unfocusedContainerColor = Color.Transparent,
            focusedContainerColor = Color.Transparent,
            unfocusedIndicatorColor = colors.strokeNeutral,
            focusedIndicatorColor = colors.iberdrolaGreen,
            unfocusedTextColor = colors.darkGreyText,
            focusedTextColor = colors.darkGreyText,
            unfocusedLabelColor = colors.textSubtitle,
            focusedLabelColor = colors.textSubtitle
        )
    )
}
