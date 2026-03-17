package com.iberdrola.practicas2026.FranciscoPG.presentation.invoices.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import com.iberdrola.practicas2026.FranciscoPG.presentation.invoices.ui.preview.DevicePreview
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.unit.Dp
import com.iberdrola.practicas2026.FranciscoPG.R
import com.iberdrola.practicas2026.FranciscoPG.presentation.theme.Spacing
import com.iberdrola.practicas2026.FranciscoPG.presentation.theme.Radius
import com.iberdrola.practicas2026.FranciscoPG.presentation.theme.Stroke
import com.iberdrola.practicas2026.FranciscoPG.presentation.theme.Skeleton
import com.iberdrola.practicas2026.FranciscoPG.presentation.theme.TextSize

private val StickyHeaderBold = FontFamily(Font(R.font.iberpangea_bold, FontWeight.Bold))

@Composable
fun StickyInvoiceHeaderComposable(
    modifier: Modifier = Modifier,
    onFilterClick: () -> Unit = {}
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(colorResource(R.color.color_background))
            .padding(
                top = Spacing.dp10,
                bottom = Spacing.dp8
            ),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Título: "Histórico de facturas"
        Text(
            text = stringResource(R.string.title_invoice_history),
            modifier = Modifier
                .weight(1f)
                .padding(start = Spacing.dp32),
            fontFamily = StickyHeaderBold,
            fontWeight = FontWeight.Bold,
            fontSize = TextSize.sp16,
            color = colorResource(R.color.color_text_high_emphasis)
        )

        // Botón de filtro
        OutlinedButton(
            onClick = onFilterClick,
            modifier = Modifier.padding(end = Spacing.dp24),
            shape = RoundedCornerShape(Radius.dp50),
            border = BorderStroke(
                width = Stroke.dp2,
                color = colorResource(R.color.iberdrola_dark_green)
            ),
            colors = ButtonDefaults.outlinedButtonColors(
                containerColor = colorResource(R.color.color_surface),
                contentColor = colorResource(R.color.iberdrola_dark_green)
            )
        ) {
            // Icono de filtro
            Icon(
                painter = painterResource(R.drawable.ic_filter),
                contentDescription = null,
                tint = colorResource(R.color.iberdrola_dark_green)
            )
            // Texto del botón: "Filtrar"
            Text(
                text = stringResource(R.string.action_filter),
                modifier = Modifier.padding(start = Spacing.dp8),
                fontFamily = StickyHeaderBold,
                fontWeight = FontWeight.Bold,
                fontSize = TextSize.sp14,
                color = colorResource(R.color.iberdrola_dark_green)
            )
        }
    }
}

@Composable
fun SkeletonStickyInvoiceHeaderComposable(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(colorResource(R.color.color_background))
            .padding(
                top = Spacing.dp10,
                bottom = Spacing.dp8
            ),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Título: "Histórico de facturas"
        SkeletonHeaderPlaceholder(
            width = Skeleton.headerTitleW,
            height = Skeleton.headerTitleH,
            modifier = Modifier
                .padding(start = Spacing.dp32, top = Spacing.dp4)
        )

        // Botón de filtro
        SkeletonHeaderPlaceholder(
            width = Skeleton.headerBtnW,
            height = Skeleton.headerBtnH,
            modifier = Modifier
                .padding(end = Spacing.dp24, top = Spacing.dp6),
            shape = RoundedCornerShape(
                Radius.dp50
            )
        )
    }
}

@Composable
private fun SkeletonHeaderPlaceholder(
    width: Dp,
    height: Dp,
    modifier: Modifier = Modifier,
    shape: RoundedCornerShape = RoundedCornerShape(Radius.dp4)
) {
    ShimmerBox(
        width = width,
        height = height,
        modifier = modifier,
        shape = shape
    )
}

// Histórico de facturas
@DevicePreview
@Composable
private fun PreviewStickyInvoiceHeaderComposable() {
    MaterialTheme {
        StickyInvoiceHeaderComposable()
    }
}

// Skeleton del histórico de facturas
@DevicePreview
@Composable
private fun PreviewSkeletonStickyInvoiceHeaderComposable() {
    MaterialTheme {
        SkeletonStickyInvoiceHeaderComposable()
    }
}

// Overlay: histórico de facturas + skeleton superpuesto
@DevicePreview
@Composable
private fun PreviewStickyHeaderOverlayAlignment() {
    MaterialTheme {
        Box {
            // Histórico de facturas (real)
            StickyInvoiceHeaderComposable()
            // Skeleton del histórico de facturas (superpuesto)
            SkeletonStickyInvoiceHeaderComposable(
                modifier = Modifier.alpha(0.8f)
            )
        }
    }
}

