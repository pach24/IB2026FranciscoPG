package com.iberdrola.practicas2026.FranciscoPG.presentation.myinvoices.ui.components.list

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import com.iberdrola.practicas2026.FranciscoPG.presentation.theme.IberFontBold
import com.iberdrola.practicas2026.FranciscoPG.presentation.theme.IberFontRegular
import androidx.compose.ui.unit.Dp
import com.iberdrola.practicas2026.FranciscoPG.presentation.myinvoices.ui.preview.DevicePreview
import com.iberdrola.practicas2026.FranciscoPG.domain.model.InvoiceStatus
import com.iberdrola.practicas2026.FranciscoPG.R
import com.iberdrola.practicas2026.FranciscoPG.presentation.myinvoices.ui.components.ShimmerBox
import com.iberdrola.practicas2026.FranciscoPG.presentation.myinvoices.ui.components.ShimmerHost
import com.iberdrola.practicas2026.FranciscoPG.presentation.myinvoices.ui.components.StatusPillComposable
import com.iberdrola.practicas2026.FranciscoPG.presentation.theme.IberdrolaTheme
import com.iberdrola.practicas2026.FranciscoPG.presentation.theme.Spacing
import com.iberdrola.practicas2026.FranciscoPG.presentation.theme.IconSize
import com.iberdrola.practicas2026.FranciscoPG.presentation.theme.Radius
import com.iberdrola.practicas2026.FranciscoPG.presentation.theme.Stroke
import com.iberdrola.practicas2026.FranciscoPG.presentation.theme.Skeleton
import com.iberdrola.practicas2026.FranciscoPG.presentation.theme.TextSize

@Composable
fun InvoiceHeaderItemComposable(
    year: String,
    modifier: Modifier = Modifier
) {
    val colors = IberdrolaTheme.colors
    // Año de agrupación: "2024", "2023"...
    Text(
        text = year,
        modifier = modifier
            .fillMaxWidth()
            .background(colors.background)
            .padding(
                horizontal = Spacing.dp32,
                vertical = Spacing.dp10
            ),
        fontFamily = IberFontBold,
        fontWeight = FontWeight.Bold,
        fontSize = TextSize.sp14,
        color = colors.darkGreyText
    )
}

@Composable
fun InvoiceRowItemComposable(
    date: String,
    type: String,
    status: String,
    amount: String,
    invoiceStatus: InvoiceStatus = InvoiceStatus.PENDING,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    val colors = IberdrolaTheme.colors
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(colors.background)
            .clickable(onClick = onClick)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    top = Spacing.dp14,
                    start = Spacing.dp32,
                    end = Spacing.dp32
                ),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                // Fecha de la factura: "8 de marzo"
                Text(
                    text = date,
                    fontFamily = IberFontBold,
                    fontWeight = FontWeight.Bold,
                    fontSize = TextSize.sp14,
                    color = colors.darkGreyText
                )
                Spacer(modifier = Modifier.height(Spacing.dp8))

                // Tipo de factura: "Factura Luz", "Factura Gas"
                Text(
                    text = type,
                    fontFamily = IberFontRegular,
                    fontSize = TextSize.sp12,
                    color = colors.lightGrey
                )

                // Estado: "Pagada" / "Pendiente de Pago"
                StatusPillComposable(
                    text = status,
                    status = invoiceStatus,
                    modifier = Modifier.padding(top = Spacing.dp8)
                )
            }

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Importe: "20,00 €"
                Text(
                    text = amount,
                    fontFamily = IberFontRegular,
                    fontSize = TextSize.sp16,
                    color = colors.lightGrey
                )
                Spacer(modifier = Modifier.width(Spacing.dp4))
                // Flecha de navegación
                Icon(
                    painter = painterResource(R.drawable.ic_arrow_right),
                    contentDescription = null,
                    tint = colors.lightGrey,
                    modifier = Modifier.size(IconSize.dp30)
                )
            }
        }

        Spacer(modifier = Modifier.height(Spacing.dp14))
        // Divisor horizontal
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(Stroke.dp1)
                .background(colors.divider)
        )
    }
}

@Composable
fun SkeletonInvoiceHeaderItemComposable(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(Color.Transparent)
            .padding(
                horizontal = Spacing.dp32,
                vertical = Spacing.dp10
            )
    ) {
        // Año de agrupación: "2024", "2023"...
        SkeletonPlaceholder(
            width = Skeleton.yearW,
            height = Skeleton.yearH,
            modifier = Modifier.padding(top = Spacing.dp1)
        )
    }
}

@Composable
fun SkeletonInvoiceRowItemComposable(modifier: Modifier = Modifier) {
    val colors = IberdrolaTheme.colors
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(Color.Transparent)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    top = Spacing.dp14,
                    start = Spacing.dp32,
                    end = Spacing.dp32
                ),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                // Fecha de la factura
                SkeletonPlaceholder(
                    width = Skeleton.listDateW,
                    height = Skeleton.listDateH,
                    modifier = Modifier.padding(top = Spacing.dp2)
                )
                Spacer(modifier = Modifier.height(Spacing.dp15))
                // Tipo de factura: "Factura Luz", "Factura Gas"
                SkeletonPlaceholder(
                    width = Skeleton.listTypeW,
                    height = Skeleton.listTypeH
                )
                // Estado: "Pagada" / "Pendiente de Pago"
                SkeletonPlaceholder(
                    width = Skeleton.listStatusW,
                    height = Skeleton.listStatusH,
                    modifier = Modifier.padding(top = Spacing.dp14),
                    shape = RoundedCornerShape(Radius.dp8)
                )
            }

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Importe: "20,00 €"
                SkeletonPlaceholder(
                    width = Skeleton.listAmountW,
                    height = Skeleton.listAmountH,
                    modifier = Modifier.padding(end = Spacing.dp4)
                )
                Spacer(modifier = Modifier.width(Spacing.dp6))
                // Flecha de navegación
                SkeletonPlaceholder(
                    width = Skeleton.listArrowW,
                    height = Skeleton.listArrowH,
                    modifier = modifier.padding(end = Spacing.dp8)
                )
            }
        }

        Spacer(modifier = Modifier.height(Spacing.dp14))
        // Divisor horizontal
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(Stroke.dp1)
                .background(colors.strokeNeutral)
        )
    }
}

@Composable
private fun SkeletonPlaceholder(
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

// Cabecera de año del histórico
@DevicePreview
@Composable
private fun PreviewInvoiceHeaderItemComposable() {
    IberdrolaTheme { InvoiceHeaderItemComposable(year = "2024") }
}

// Fila de factura (pendiente de pago)
@DevicePreview
@Composable
private fun PreviewInvoiceRowItemComposable() {
    IberdrolaTheme {
        InvoiceRowItemComposable(
            date = "8 de marzo",
            type = "Factura Luz",
            status = "Pendiente de Pago",
            amount = "20,00 €",
            invoiceStatus = InvoiceStatus.PENDING
        )
    }
}

// Fila de factura (pagada)
@DevicePreview
@Composable
private fun PreviewInvoiceRowItemPaidComposable() {
    IberdrolaTheme {
        InvoiceRowItemComposable(
            date = "8 de marzo",
            type = "Factura Luz",
            status = "Pagada",
            amount = "20,00 €",
            invoiceStatus = InvoiceStatus.PAID
        )
    }
}

// Skeleton de la cabecera de año
@DevicePreview
@Composable
private fun PreviewSkeletonInvoiceHeaderItemComposable() {
    IberdrolaTheme { SkeletonInvoiceHeaderItemComposable() }
}

// Skeleton de la fila de factura
@DevicePreview
@Composable
private fun PreviewSkeletonInvoiceRowItemComposable() {
    IberdrolaTheme { SkeletonInvoiceRowItemComposable() }
}

// Overlay: cabecera + fila con skeletons superpuestos
@DevicePreview
@Composable
private fun PreviewOverlaySkeletonOnRow() {
    IberdrolaTheme {
        ShimmerHost {
            Column {
                Box {
                    // Cabecera de año (real)
                    InvoiceHeaderItemComposable(year = "2024")
                    // Skeleton de la cabecera (superpuesto)
                    SkeletonInvoiceHeaderItemComposable(
                        modifier = Modifier.alpha(0.9f)
                    )
                }
                Box {
                    // Fila de factura (real)
                    InvoiceRowItemComposable(
                        date = "8 de marzo",
                        type = "Factura Luz",
                        status = "Pendiente de Pago",
                        amount = "20,00 €",
                        invoiceStatus = InvoiceStatus.PENDING
                    )
                    // Skeleton de la fila (superpuesto)
                    SkeletonInvoiceRowItemComposable(
                        modifier = Modifier.alpha(0.9f)
                    )
                }
            }
        }
    }
}
