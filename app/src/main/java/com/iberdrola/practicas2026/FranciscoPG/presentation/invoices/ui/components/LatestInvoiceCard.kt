package com.iberdrola.practicas2026.FranciscoPG.presentation.invoices.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import com.iberdrola.practicas2026.FranciscoPG.presentation.invoices.ui.preview.DevicePreview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.iberdrola.practicas2026.FranciscoPG.R

private val IberFontRegular = FontFamily(Font(R.font.iberpangea_regular, FontWeight.Normal))
private val IberFontBold = FontFamily(Font(R.font.iberpangea_bold, FontWeight.Bold))

@Composable
fun LatestInvoiceCardComposable(
    amount: String,
    dateRange: String,
    modifier: Modifier = Modifier,
    supplyType: String = "Factura Luz",
    status: String = "Pendiente de Pago",
    isPaid: Boolean = false,
    iconRes: Int = R.drawable.ic_light
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .border(
                width = dimensionResource(R.dimen.m3_comp_divider_thickness),
                color = colorResource(R.color.iberdrola_dark_green),
                shape = RoundedCornerShape(dimensionResource(R.dimen.m3_comp_card_corner_radius))
            ),
        shape = RoundedCornerShape(dimensionResource(R.dimen.m3_comp_card_corner_radius)),
        colors = CardDefaults.cardColors(containerColor = colorResource(R.color.color_surface)),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(dimensionResource(R.dimen.m3_sys_spacing_custom_15))
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    // Título: "Última factura"
                    Text(
                        text = stringResource(R.string.card_title_latest_invoice),
                        fontFamily = IberFontBold,
                        fontWeight = FontWeight.Bold,
                        fontSize = dimensionResource(R.dimen.m3_sys_typescale_label).value.sp,
                        lineHeight = (dimensionResource(R.dimen.m3_sys_typescale_label).value * 1.4f).sp,
                        color = colorResource(R.color.dark_grey_text)
                    )
                    Spacer(modifier = Modifier.height(dimensionResource(R.dimen.m3_sys_spacing_half)))
                    // Tipo de suministro: "Factura Luz"
                    Text(
                        text = supplyType,
                        fontFamily = IberFontRegular,
                        fontSize = dimensionResource(R.dimen.m3_sys_typescale_body_small).value.sp,
                        lineHeight = (dimensionResource(R.dimen.m3_sys_typescale_body_small).value * 1.4f).sp,
                        color = colorResource(R.color.light_grey)
                    )
                }

                // Icono de suministro (Luz/Gas)
                Icon(
                    painter = painterResource(iconRes),
                    contentDescription = null,
                    tint = colorResource(R.color.iberdrola_dark_green),
                    modifier = Modifier
                        .size(dimensionResource(R.dimen.m3_comp_card_main_icon_size))
                        .padding(end = dimensionResource(R.dimen.m3_sys_spacing_half))
                )
            }

            Spacer(modifier = Modifier.height(dimensionResource(R.dimen.m3_sys_spacing_custom_14)))

            // Importe: "20,00 €"
            Text(
                text = amount,
                fontFamily = IberFontBold,
                fontWeight = FontWeight.Bold,
                fontSize = dimensionResource(R.dimen.m3_sys_typescale_headline_medium).value.sp,
                lineHeight = (dimensionResource(R.dimen.m3_sys_typescale_headline_medium).value * 1.3f).sp,
                color = colorResource(R.color.dark_grey)
            )

            Spacer(modifier = Modifier.height(dimensionResource(R.dimen.m3_sys_spacing_half)))

            // Rango de fechas: "01 feb. 2024 - 04 mar. 2024"
            Text(
                text = dateRange,
                fontFamily = IberFontRegular,
                fontSize = dimensionResource(R.dimen.m3_sys_typescale_body_small).value.sp,
                lineHeight = (dimensionResource(R.dimen.m3_sys_typescale_body_small).value * 1.4f).sp,
                color = colorResource(R.color.light_grey)
            )

            Spacer(modifier = Modifier.height(dimensionResource(R.dimen.m3_sys_spacing_custom_10)))

            // Divisor horizontal
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(dimensionResource(R.dimen.m3_comp_divider_thickness))
                    .background(colorResource(R.color.divider))
            )

            Spacer(modifier = Modifier.height(dimensionResource(R.dimen.m3_sys_spacing_custom_14)))

            // Estado: "Pagada" / "Pendiente de Pago"
            StatusPillComposable(
                text = status,
                isPaid = isPaid
            )
        }
    }
}

@Composable
fun SkeletonLatestInvoiceCardComposable(modifier: Modifier = Modifier) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .border(
                width = dimensionResource(R.dimen.m3_comp_divider_thickness),
                color = colorResource(R.color.handler_color),
                shape = RoundedCornerShape(dimensionResource(R.dimen.m3_comp_card_corner_radius))
            ),
        shape = RoundedCornerShape(dimensionResource(R.dimen.m3_comp_card_corner_radius)),
        colors = CardDefaults.cardColors(containerColor = colorResource(R.color.color_surface)),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(dimensionResource(R.dimen.m3_sys_spacing_custom_15))
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column {
                    // Titulo: "Ultima factura"
                    SkeletonBox(
                        width = dimensionResource(R.dimen.m3_comp_skeleton_card_title_width),
                        height = dimensionResource(R.dimen.m3_comp_skeleton_card_title_height)
                    )
                    Spacer(modifier = Modifier.height(dimensionResource(R.dimen.m3_sys_spacing_1)))
                    // Tipo de suministro: "Factura Luz"
                    SkeletonBox(
                        width = dimensionResource(R.dimen.m3_comp_skeleton_card_subtitle_width),
                        height = dimensionResource(R.dimen.m3_comp_skeleton_card_subtitle_height)
                    )
                }

                // Icono de suministro (Luz/Gas)
                SkeletonBox(
                    width = dimensionResource(R.dimen.m3_comp_skeleton_card_icon_width),
                    height = dimensionResource(R.dimen.m3_comp_skeleton_card_icon_height),
                    modifier = Modifier.padding(end = dimensionResource(R.dimen.m3_sys_spacing_custom_5)))
            }

            Spacer(modifier = Modifier.height(21.dp))
            // Importe: "20,00 €"
            SkeletonBox(
                width = dimensionResource(R.dimen.m3_comp_skeleton_card_amount_width),
                height = dimensionResource(R.dimen.m3_comp_skeleton_card_amount_height)
            )

            Spacer(modifier = Modifier.height(dimensionResource(R.dimen.m3_sys_spacing_custom_10)))
            // Rango de fechas: "01 feb. 2024 - 04 mar. 2024"
            SkeletonBox(
                width = dimensionResource(R.dimen.m3_comp_skeleton_card_dates_width),
                height = dimensionResource(R.dimen.m3_comp_skeleton_card_dates_height)
            )

            Spacer(modifier = Modifier.height(dimensionResource(R.dimen.m3_sys_spacing_custom_14)))
            // Divisor horizontal
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(dimensionResource(R.dimen.m3_comp_divider_thickness))
                    .background(colorResource(R.color.color_stroke_neutral))
            )

            Spacer(modifier = Modifier.height(dimensionResource(R.dimen.m3_sys_spacing_custom_12)))
            // Estado: "Pagada" / "Pendiente de Pago"
            ShimmerBox(
                width = dimensionResource(R.dimen.m3_comp_skeleton_card_status_width),
                height = dimensionResource(R.dimen.m3_comp_skeleton_card_status_height),
                shape = RoundedCornerShape(dimensionResource(R.dimen.m3_comp_shape_corner_radius_small))
            )
        }
    }
}

@Composable
private fun SkeletonBox(
    width: androidx.compose.ui.unit.Dp,
    height: androidx.compose.ui.unit.Dp,
    modifier: Modifier = Modifier
) {
    ShimmerBox(
        modifier = modifier,
        width = width,
        height = height,
        shape = RoundedCornerShape(dimensionResource(R.dimen.m3_comp_skeleton_corner_radius))
    )
}

// Última factura
@DevicePreview
@Composable
private fun PreviewLatestInvoiceCardComposable() {
    MaterialTheme {
        LatestInvoiceCardComposable(
            amount = "20,00 \u20AC",
            dateRange = "01 feb. 2024 - 04 mar. 2024",
            status = "Pagada",
            isPaid = true
        )
    }
}

// Skeleton de la última factura
@DevicePreview
@Composable
private fun PreviewSkeletonLatestInvoiceCardComposable() {
    MaterialTheme {
        SkeletonLatestInvoiceCardComposable()
    }
}

// Overlay: última factura + skeleton superpuesto
@DevicePreview
@Composable
private fun PreviewOverlaySkeletonOnCard() {
    MaterialTheme {
        ShimmerHost {
            Box {
                // Última factura (real)
                LatestInvoiceCardComposable(
                    amount = "20,00 €",
                    dateRange = "01 feb. 2024 - 04 mar. 2024",
                    status = "Pagada",
                    isPaid = true
                )
                // Skeleton de la última factura (superpuesto)
                SkeletonLatestInvoiceCardComposable(
                    modifier = Modifier.alpha(0.8f)
                )
            }
        }
    }
}

