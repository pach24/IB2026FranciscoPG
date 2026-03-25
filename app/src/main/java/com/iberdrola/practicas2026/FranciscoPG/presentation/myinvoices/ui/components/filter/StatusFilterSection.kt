package com.iberdrola.practicas2026.FranciscoPG.presentation.myinvoices.ui.components.filter

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import com.iberdrola.practicas2026.FranciscoPG.domain.model.InvoiceStatus
import com.iberdrola.practicas2026.FranciscoPG.presentation.theme.IberFontBold
import com.iberdrola.practicas2026.FranciscoPG.presentation.theme.IberFontRegular
import androidx.compose.ui.unit.dp
import com.iberdrola.practicas2026.FranciscoPG.R
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

// Checkbox personalizado con esquinas redondeadas y animación de escala + color
@Composable
private fun RoundedCheckbox(
    checked: Boolean,
    checkedColor: Color,
    uncheckedBorderColor: Color,
    checkmarkColor: Color
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
        modifier = Modifier
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
