package com.iberdrola.practicas2026.FranciscoPG.presentation.invoices.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.iberdrola.practicas2026.FranciscoPG.domain.model.InvoiceStatus
import com.iberdrola.practicas2026.FranciscoPG.presentation.theme.IberdrolaTheme

data class StatusColors(
    val background: Color,
    val text: Color
)

object InvoiceStatusColors {

    @Composable
    fun forStatus(status: InvoiceStatus): StatusColors {
        val colors = IberdrolaTheme.colors
        return when (status) {
            InvoiceStatus.PAID -> StatusColors(
                background = colors.statusPaid,
                text = colors.iberdrolaDarkGreen
            )
            InvoiceStatus.PENDING -> StatusColors(
                background = colors.errorContainer,
                text = colors.errorText
            )
            else -> StatusColors(
                background = colors.statusDefault,
                text = colors.statusDefaultText
            )
        }
    }
}
