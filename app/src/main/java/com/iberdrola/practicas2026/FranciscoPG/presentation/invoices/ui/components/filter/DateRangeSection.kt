package com.iberdrola.practicas2026.FranciscoPG.presentation.invoices.ui.components.filter

import android.content.res.Configuration
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
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
@Composable
private fun DateField(
    label: String,
    value: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    val colors = IberdrolaTheme.colors
    val hasValue = value.isNotEmpty()

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onClick
            )
    ) {
        if (hasValue) {
            Text(
                text = label,
                fontSize = TextSize.sp10,
                color = colors.textSubtitle
            )
            Spacer(modifier = Modifier.height(2.dp))
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = if (hasValue) value else label,
                modifier = Modifier
                    .weight(1f)
                    .padding(vertical = Spacing.dp8),
                fontSize = TextSize.sp12,
                color = if (hasValue) colors.darkGreyText else colors.textSubtitle
            )
            Icon(
                painter = painterResource(R.drawable.ic_calendar),
                contentDescription = null,
                tint = colors.textSubtitle
            )
        }

        Spacer(modifier = Modifier.height(Spacing.dp10))

        HorizontalDivider(
            color = if (hasValue) colors.iberdrolaGreen else colors.strokeNeutral,
            thickness = 1.dp
        )
    }
}

@Preview(name = "Vacío - Light", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_NO)
@Preview(name = "Vacío - Dark", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun DateRangeSectionEmptyPreview() {
    IberdrolaTheme {
        DateRangeSection(
            dateFrom = "",
            dateTo = "",
            onFromClick = {},
            onToClick = {}
        )
    }
}

@Preview(name = "Con fechas - Light", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_NO)
@Preview(name = "Con fechas - Dark", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun DateRangeSectionFilledPreview() {
    IberdrolaTheme {
        DateRangeSection(
            dateFrom = "01/01/2025",
            dateTo = "31/12/2025",
            onFromClick = {},
            onToClick = {}
        )
    }
}

@Preview(name = "Parcial - Light", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_NO)
@Composable
private fun DateRangeSectionPartialPreview() {
    IberdrolaTheme {
        DateRangeSection(
            dateFrom = "01/06/2025",
            dateTo = "",
            onFromClick = {},
            onToClick = {}
        )
    }
}
