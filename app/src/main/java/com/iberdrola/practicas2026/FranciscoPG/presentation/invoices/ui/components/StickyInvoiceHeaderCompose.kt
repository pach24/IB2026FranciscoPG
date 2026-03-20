package com.iberdrola.practicas2026.FranciscoPG.presentation.invoices.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import com.iberdrola.practicas2026.FranciscoPG.presentation.theme.IberFontBold
import com.iberdrola.practicas2026.FranciscoPG.presentation.invoices.ui.preview.DevicePreview
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.unit.Dp
import com.iberdrola.practicas2026.FranciscoPG.R
import com.iberdrola.practicas2026.FranciscoPG.presentation.theme.IberdrolaTheme
import com.iberdrola.practicas2026.FranciscoPG.presentation.theme.Spacing
import com.iberdrola.practicas2026.FranciscoPG.presentation.theme.Radius
import com.iberdrola.practicas2026.FranciscoPG.presentation.theme.Stroke
import com.iberdrola.practicas2026.FranciscoPG.presentation.theme.Skeleton
import com.iberdrola.practicas2026.FranciscoPG.presentation.theme.TextSize

@Composable
fun StickyInvoiceHeaderComposable(
    modifier: Modifier = Modifier,
    activeFilterCount: Int = 0,
    onFilterClick: () -> Unit = {}
) {
    val colors = IberdrolaTheme.colors
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(colors.background)
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
            fontFamily = IberFontBold,
            fontWeight = FontWeight.Bold,
            fontSize = TextSize.sp16,
            color = colors.textHighEmphasis
        )

        // Botón de filtro con badge opcional
        Box(modifier = Modifier.padding(end = Spacing.dp24)) {
            OutlinedButton(
                onClick = onFilterClick,
                shape = RoundedCornerShape(Radius.dp50),
                border = BorderStroke(
                    width = Stroke.dp2,
                    color = if (activeFilterCount > 0) colors.iberdrolaGreen else colors.iberdrolaDarkGreen
                ),
                colors = ButtonDefaults.outlinedButtonColors(
                    containerColor = if (activeFilterCount > 0) colors.iberdrolaGreen else colors.surface,
                    contentColor = if (activeFilterCount > 0) Color.White else colors.iberdrolaDarkGreen
                )
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_filter),
                    contentDescription = null,
                    tint = if (activeFilterCount > 0) Color.White else colors.iberdrolaDarkGreen
                )
                Text(
                    text = stringResource(R.string.action_filter),
                    modifier = Modifier.padding(start = Spacing.dp8),
                    fontFamily = IberFontBold,
                    fontWeight = FontWeight.Bold,
                    fontSize = TextSize.sp14,
                    color = if (activeFilterCount > 0) Color.White else colors.iberdrolaDarkGreen
                )
            }

            if (activeFilterCount > 0) {
                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .size(Spacing.dp20)
                        .background(colors.activeFilterBadgeColor, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = activeFilterCount.toString(),
                        color = Color.White,
                        fontSize = TextSize.sp10,
                        fontFamily = IberFontBold,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}

@Composable
fun SkeletonStickyInvoiceHeaderComposable(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(IberdrolaTheme.colors.background)
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
    IberdrolaTheme {
        StickyInvoiceHeaderComposable()
    }
}

// Skeleton del histórico de facturas
@DevicePreview
@Composable
private fun PreviewSkeletonStickyInvoiceHeaderComposable() {
    IberdrolaTheme {
        SkeletonStickyInvoiceHeaderComposable()
    }
}

// Overlay: histórico de facturas + skeleton superpuesto
@DevicePreview
@Composable
private fun PreviewStickyHeaderOverlayAlignment() {
    IberdrolaTheme {
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
