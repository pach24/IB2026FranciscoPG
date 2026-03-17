package com.iberdrola.practicas2026.FranciscoPG.presentation.invoices.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.compose.material3.MaterialTheme
import com.iberdrola.practicas2026.FranciscoPG.R
import com.iberdrola.practicas2026.FranciscoPG.presentation.invoices.ui.preview.DevicePreview
import com.iberdrola.practicas2026.FranciscoPG.presentation.theme.TextSize
import com.iberdrola.practicas2026.FranciscoPG.presentation.theme.Radius
import com.iberdrola.practicas2026.FranciscoPG.presentation.theme.Spacing

private val StatusFontBold = FontFamily(Font(R.font.iberpangea_bold, FontWeight.Bold))

@Composable
fun StatusPillComposable(
    text: String,
    isPaid: Boolean,
    modifier: Modifier = Modifier
) {
    Text(
        text = text,
        modifier = modifier
            .background(
                color = colorResource(if (isPaid) R.color.statuspaid else R.color.red_100),
                shape = RoundedCornerShape(Radius.dp8)
            )
            .padding(
                horizontal = Spacing.dp12,
                vertical = Spacing.dp4
            ),
        fontFamily = StatusFontBold,
        fontWeight = FontWeight.Bold,
        fontSize = TextSize.sp10,
        lineHeight = (10f * 1.4f).sp,
        color = colorResource(if (isPaid) R.color.iberdrola_dark_green else R.color.red_600)
    )
}

@DevicePreview
@Composable
private fun PreviewStatusPillPaid() {
    MaterialTheme {
        StatusPillComposable(
            text = "Pagada",
            isPaid = true
        )
    }
}

@DevicePreview
@Composable
private fun PreviewStatusPillPending() {
    MaterialTheme {
        StatusPillComposable(
            text = "Pendiente de Pago",
            isPaid = false
        )
    }
}
