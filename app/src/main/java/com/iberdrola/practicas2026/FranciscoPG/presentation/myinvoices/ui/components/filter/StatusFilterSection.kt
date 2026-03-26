package com.iberdrola.practicas2026.FranciscoPG.presentation.myinvoices.ui.components.filter

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.iberdrola.practicas2026.FranciscoPG.R
import com.iberdrola.practicas2026.FranciscoPG.domain.model.InvoiceStatus
import com.iberdrola.practicas2026.FranciscoPG.presentation.common.RoundedCheckbox
import com.iberdrola.practicas2026.FranciscoPG.presentation.theme.IberFontBold
import com.iberdrola.practicas2026.FranciscoPG.presentation.theme.IberFontRegular
import com.iberdrola.practicas2026.FranciscoPG.presentation.theme.IberdrolaTheme
import com.iberdrola.practicas2026.FranciscoPG.presentation.theme.Spacing
import com.iberdrola.practicas2026.FranciscoPG.presentation.theme.TextSize

// Sección de filtrado por estado con checkboxes redondeados para cada opción
@Composable
fun StatusFilterSection(
    statusOptions: List<Pair<InvoiceStatus, String>>,
    selectedStatuses: Set<InvoiceStatus>,
    onStatusToggle: (InvoiceStatus) -> Unit
) {
    val colors = IberdrolaTheme.colors

    Column {
        Text(
            text = stringResource(R.string.filter_status_section_title),
            fontSize = TextSize.sp12,
            fontWeight = FontWeight.Bold,
            fontFamily = IberFontBold,
            color = colors.darkGreyText
        )
        Spacer(modifier = Modifier.height(Spacing.dp24))

        Column(
            verticalArrangement = Arrangement.spacedBy(Spacing.dp32),
            modifier = Modifier.padding(start = Spacing.dp8)
        ) {
            statusOptions.forEach { (status, label) ->
                val isChecked = selectedStatuses.contains(status)
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(min = 24.dp)
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null,
                            role = Role.Checkbox
                        ) { onStatusToggle(status) }
                ) {
                    RoundedCheckbox(
                        checked = isChecked,
                        checkedColor = colors.iberdrolaGreen,
                        uncheckedBorderColor = colors.iberdrolaGreen,
                        checkmarkColor = colors.white
                    )
                    Text(
                        text = label,
                        modifier = Modifier.padding(start = Spacing.dp16),
                        fontFamily = IberFontRegular,
                        fontSize = TextSize.sp14,
                        color = colors.darkGreyText
                    )
                }
            }
        }
    }
}

