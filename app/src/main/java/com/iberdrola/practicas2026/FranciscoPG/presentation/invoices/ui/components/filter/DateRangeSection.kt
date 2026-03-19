package com.iberdrola.practicas2026.FranciscoPG.presentation.invoices.ui.components.filter

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import com.iberdrola.practicas2026.FranciscoPG.R
import com.iberdrola.practicas2026.FranciscoPG.presentation.theme.IberdrolaTheme
import com.iberdrola.practicas2026.FranciscoPG.presentation.theme.Spacing
import com.iberdrola.practicas2026.FranciscoPG.presentation.theme.TextSize

private val SectionBold = FontFamily(Font(R.font.iberpangea_bold, FontWeight.Bold))

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
            fontSize = TextSize.sp14,
            fontWeight = FontWeight.Bold,
            fontFamily = SectionBold,
            color = colors.darkGreyText
        )
        Spacer(modifier = Modifier.height(Spacing.dp16))
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

// Campo de fecha individual con icono de calendario, read-only, abre DatePicker al pulsar
@Composable
private fun DateField(
    label: String,
    value: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    val colors = IberdrolaTheme.colors
    Box(modifier = modifier.clickable { onClick() }) {
        TextField(
            value = value,
            onValueChange = { },
            readOnly = true,
            enabled = false,
            label = { Text(label, fontSize = TextSize.sp14) },
            modifier = Modifier.fillMaxWidth(),
            trailingIcon = {
                Icon(painterResource(R.drawable.ic_calendar), null, tint = colors.lightGrey)
            },
            colors = TextFieldDefaults.colors(
                disabledContainerColor = Color.Transparent,
                disabledIndicatorColor = colors.darkGreyText,
                disabledTextColor = colors.darkGreyText,
                disabledLabelColor = colors.lightGrey
            )
        )
    }
}
