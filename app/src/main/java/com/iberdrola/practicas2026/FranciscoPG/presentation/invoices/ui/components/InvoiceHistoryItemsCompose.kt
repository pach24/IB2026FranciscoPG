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
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import com.iberdrola.practicas2026.FranciscoPG.presentation.invoices.ui.preview.DevicePreview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.iberdrola.practicas2026.FranciscoPG.R

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
                horizontal = dimensionResource(R.dimen.m3_sys_spacing_4),
                vertical = dimensionResource(R.dimen.m3_sys_spacing_custom_10)
            ),
        fontFamily = InvoiceFontBold,
        fontWeight = FontWeight.Bold,
        fontSize = dimensionResource(R.dimen.m3_sys_typescale_label).value.sp,
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
                    top = dimensionResource(R.dimen.m3_sys_spacing_custom_14),
                    start = dimensionResource(R.dimen.m3_sys_spacing_4),
                    end = dimensionResource(R.dimen.m3_sys_spacing_4)
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
                    fontSize = dimensionResource(R.dimen.m3_sys_typescale_label).value.sp,
                    color = colorResource(R.color.dark_grey_text)
                )
                Spacer(modifier = Modifier.height(8.dp))

                // Tipo de factura: "Factura Luz", "Factura Gas"
                Text(
                    text = type,
                    fontFamily = InvoiceFontRegular,
                    fontSize = dimensionResource(R.dimen.m3_sys_typescale_body_small).value.sp,
                    color = colorResource(R.color.light_grey)
                )

                // Estado: "Pagada" / "Pendiente de Pago"
                StatusPillComposable(
                    text = status,
                    isPaid = isPaid,
                    modifier = Modifier.padding(top = dimensionResource(R.dimen.m3_sys_spacing_1))
                )
            }

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Importe: "20,00 €"
                Text(
                    text = amount,
                    fontFamily = InvoiceFontRegular,
                    fontSize = dimensionResource(R.dimen.m3_sys_typescale_body_large).value.sp,
                    color = colorResource(R.color.light_grey)
                )
                Spacer(modifier = Modifier.width(dimensionResource(R.dimen.m3_sys_spacing_half)))
                // Flecha de navegación
                Icon(
                    painter = painterResource(R.drawable.ic_arrow_right),
                    contentDescription = null,
                    tint = colorResource(R.color.light_grey),
                    modifier = Modifier.size(dimensionResource(R.dimen.m3_comp_list_icon_size))
                )
            }
        }

        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.m3_sys_spacing_custom_14)))
        // Divisor horizontal
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(dimensionResource(R.dimen.m3_comp_divider_thickness))
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
                horizontal = dimensionResource(R.dimen.m3_sys_spacing_4),
                vertical = dimensionResource(R.dimen.m3_sys_spacing_custom_10)
            )
    ) {
        // Año de agrupación: "2024", "2023"...
        SkeletonPlaceholder(
            width = dimensionResource(R.dimen.m3_comp_skeleton_year_width),
            height = dimensionResource(R.dimen.m3_comp_skeleton_year_height),
            modifier = Modifier.padding(top = 1.dp)
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
                    top = dimensionResource(R.dimen.m3_sys_spacing_custom_14),
                    start = dimensionResource(R.dimen.m3_sys_spacing_4),
                    end = dimensionResource(R.dimen.m3_sys_spacing_4)
                ),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                // Fecha de la factura
                SkeletonPlaceholder(
                    width = dimensionResource(R.dimen.m3_comp_skeleton_list_date_width),
                    height = dimensionResource(R.dimen.m3_comp_skeleton_list_date_height),
                    modifier = Modifier.padding(top = dimensionResource(R.dimen.m3_sys_spacing_custom_2))
                )
                Spacer(modifier = Modifier.height(15.dp))
                // Tipo de factura: "Factura Luz", "Factura Gas"
                SkeletonPlaceholder(
                    width = dimensionResource(R.dimen.m3_comp_skeleton_list_type_width),
                    height = dimensionResource(R.dimen.m3_comp_skeleton_list_type_height)
                )
                // Estado: "Pagada" / "Pendiente de Pago"
                SkeletonPlaceholder(
                    width = dimensionResource(R.dimen.m3_comp_skeleton_list_status_width),
                    height = dimensionResource(R.dimen.m3_comp_skeleton_list_status_height),
                    modifier = Modifier.padding(top = dimensionResource(R.dimen.m3_sys_spacing_custom_14)),
                    shape = RoundedCornerShape(dimensionResource(R.dimen.m3_comp_shape_corner_radius_small))
                )
            }

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Importe: "20,00 €"
                SkeletonPlaceholder(
                    width = dimensionResource(R.dimen.m3_comp_skeleton_list_amount_width),
                    height = dimensionResource(R.dimen.m3_comp_skeleton_list_amount_height),
                    modifier = Modifier.padding(end = dimensionResource(R.dimen.m3_sys_spacing_half))
                )
                Spacer(modifier = Modifier.width(dimensionResource(R.dimen.m3_sys_spacing_custom_6)))
                // Flecha de navegación
                SkeletonPlaceholder(
                    width = dimensionResource(R.dimen.m3_comp_skeleton_list_arrow_width),
                    height = dimensionResource(R.dimen.m3_comp_skeleton_list_arrow_height),
                    modifier = modifier.padding(end = dimensionResource(R.dimen.m3_sys_spacing_1))
                )
            }
        }

        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.m3_sys_spacing_custom_14)))
        // Divisor horizontal
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(dimensionResource(R.dimen.m3_comp_divider_thickness))
                .background(colorResource(R.color.color_stroke_neutral))
        )
    }
}

@Composable
private fun SkeletonPlaceholder(
    width: Dp,
    height: Dp,
    modifier: Modifier = Modifier,
    shape: RoundedCornerShape = RoundedCornerShape(dimensionResource(R.dimen.m3_comp_skeleton_corner_radius))
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
