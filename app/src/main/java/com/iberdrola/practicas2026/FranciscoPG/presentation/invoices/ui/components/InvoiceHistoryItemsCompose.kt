package com.iberdrola.practicas2026.FranciscoPG.presentation.invoices.ui.components

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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import com.iberdrola.practicas2026.FranciscoPG.presentation.invoices.ui.preview.DevicePreview
import com.iberdrola.practicas2026.FranciscoPG.R
import com.iberdrola.practicas2026.FranciscoPG.presentation.theme.Spacing
import com.iberdrola.practicas2026.FranciscoPG.presentation.theme.IconSize
import com.iberdrola.practicas2026.FranciscoPG.presentation.theme.Radius
import com.iberdrola.practicas2026.FranciscoPG.presentation.theme.Stroke
import com.iberdrola.practicas2026.FranciscoPG.presentation.theme.Skeleton
import com.iberdrola.practicas2026.FranciscoPG.presentation.theme.TextSize

private val InvoiceFontRegular = FontFamily(Font(R.font.iberpangea_regular, FontWeight.Normal))
private val InvoiceFontBold = FontFamily(Font(R.font.iberpangea_bold, FontWeight.Bold))

@Composable
fun InvoiceHeaderItemComposable(
    year: String,
    modifier: Modifier = Modifier
) {
    // Año de agrupación: "2024", "2023"...
    Text(
        text = year,
        modifier = modifier
            .fillMaxWidth()
            .background(colorResource(R.color.color_background))
            .padding(
                horizontal = Spacing.dp32,
                vertical = Spacing.dp10
            ),
        fontFamily = InvoiceFontBold,
        fontWeight = FontWeight.Bold,
        fontSize = TextSize.sp14,
        color = colorResource(R.color.dark_grey_text)
    )
}

@Composable
fun InvoiceRowItemComposable(
    date: String,
    type: String,
    status: String,
    amount: String,
    isPaid: Boolean = false,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(colorResource(R.color.color_background))
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
                    fontFamily = InvoiceFontBold,
                    fontWeight = FontWeight.Bold,
                    fontSize = TextSize.sp14,
                    color = colorResource(R.color.dark_grey_text)
                )
                Spacer(modifier = Modifier.height(Spacing.dp8))

                // Tipo de factura: "Factura Luz", "Factura Gas"
                Text(
                    text = type,
                    fontFamily = InvoiceFontRegular,
                    fontSize = TextSize.sp12,
                    color = colorResource(R.color.light_grey)
                )

                // Estado: "Pagada" / "Pendiente de Pago"
                StatusPillComposable(
                    text = status,
                    isPaid = isPaid,
                    modifier = Modifier.padding(top = Spacing.dp8)
                )
            }

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Importe: "20,00 €"
                Text(
                    text = amount,
                    fontFamily = InvoiceFontRegular,
                    fontSize = TextSize.sp16,
                    color = colorResource(R.color.light_grey)
                )
                Spacer(modifier = Modifier.width(Spacing.dp4))
                // Flecha de navegación
                Icon(
                    painter = painterResource(R.drawable.ic_arrow_right),
                    contentDescription = null,
                    tint = colorResource(R.color.light_grey),
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
                .background(colorResource(R.color.divider))
        )
    }
}

@Composable
fun SkeletonInvoiceHeaderItemComposable(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(androidx.compose.ui.graphics.Color.Transparent)
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
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(androidx.compose.ui.graphics.Color.Transparent)
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
                .background(colorResource(R.color.color_stroke_neutral))
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
    MaterialTheme { InvoiceHeaderItemComposable(year = "2024") }
}

// Fila de factura (pendiente de pago)
@DevicePreview
@Composable
private fun PreviewInvoiceRowItemComposable() {
    MaterialTheme {
        InvoiceRowItemComposable(
            date = "8 de marzo",
            type = "Factura Luz",
            status = "Pendiente de Pago",
            amount = "20,00 €",
            isPaid = false
        )
    }
}

// Fila de factura (pagada)
@DevicePreview
@Composable
private fun PreviewInvoiceRowItemPaidComposable() {
    MaterialTheme {
        InvoiceRowItemComposable(
            date = "8 de marzo",
            type = "Factura Luz",
            status = "Pagada",
            amount = "20,00 €",
            isPaid = true
        )
    }
}

// Skeleton de la cabecera de año
@DevicePreview
@Composable
private fun PreviewSkeletonInvoiceHeaderItemComposable() {
    MaterialTheme { SkeletonInvoiceHeaderItemComposable() }
}

// Skeleton de la fila de factura
@DevicePreview
@Composable
private fun PreviewSkeletonInvoiceRowItemComposable() {
    MaterialTheme { SkeletonInvoiceRowItemComposable() }
}

// Overlay: cabecera + fila con skeletons superpuestos
@DevicePreview
@Composable
private fun PreviewOverlaySkeletonOnRow() {
    MaterialTheme {
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
                        isPaid = false
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
