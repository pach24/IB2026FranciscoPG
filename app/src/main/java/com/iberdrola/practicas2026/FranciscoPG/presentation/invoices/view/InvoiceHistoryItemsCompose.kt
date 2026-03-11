package com.iberdrola.practicas2026.FranciscoPG.presentation.invoices.view

import android.content.res.Configuration
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
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.sp
import com.iberdrola.practicas2026.FranciscoPG.R

private val InvoiceFontRegular = FontFamily(Font(R.font.iberpangea_regular, FontWeight.Normal))
private val InvoiceFontBold = FontFamily(Font(R.font.iberpangea_bold, FontWeight.Bold))

@Composable
fun InvoiceHeaderItemComposable(
    year: String,
    modifier: Modifier = Modifier
) {
    Text(
        text = year,
        modifier = modifier
            .fillMaxWidth()
            .background(colorResource(R.color.color_background))
            .padding(
                horizontal = dimensionResource(R.dimen.m3_sys_spacing_1),
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
                .padding(top = dimensionResource(R.dimen.m3_sys_spacing_custom_14)),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = date,
                    modifier = Modifier.padding(start = dimensionResource(R.dimen.m3_sys_spacing_1)),
                    fontFamily = InvoiceFontBold,
                    fontWeight = FontWeight.Bold,
                    fontSize = dimensionResource(R.dimen.m3_sys_typescale_label).value.sp,
                    color = colorResource(R.color.dark_grey_text)
                )

                Text(
                    text = type,
                    modifier = Modifier.padding(start = dimensionResource(R.dimen.m3_sys_spacing_1)),
                    fontFamily = InvoiceFontRegular,
                    fontSize = dimensionResource(R.dimen.m3_sys_typescale_body_small).value.sp,
                    color = colorResource(R.color.light_grey)
                )

                Text(
                    text = status,
                    modifier = Modifier
                        .padding(
                            start = dimensionResource(R.dimen.m3_sys_spacing_1),
                            top = dimensionResource(R.dimen.m3_sys_spacing_1)
                        )
                        .background(
                            color = colorResource(R.color.red_100),
                            shape = RoundedCornerShape(dimensionResource(R.dimen.m3_comp_shape_corner_radius_small))
                        )
                        .padding(
                            horizontal = dimensionResource(R.dimen.m3_sys_spacing_custom_10),
                            vertical = dimensionResource(R.dimen.m3_sys_spacing_half)
                        ),
                    fontFamily = InvoiceFontBold,
                    fontWeight = FontWeight.Bold,
                    fontSize = dimensionResource(R.dimen.m3_sys_typescale_micro).value.sp,
                    color = colorResource(R.color.red_600)
                )
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(end = dimensionResource(R.dimen.m3_sys_spacing_half))
            ) {
                Text(
                    text = amount,
                    fontFamily = InvoiceFontRegular,
                    fontSize = dimensionResource(R.dimen.m3_sys_typescale_body_large).value.sp,
                    color = colorResource(R.color.light_grey)
                )
                Spacer(modifier = Modifier.width(dimensionResource(R.dimen.m3_sys_spacing_half)))
                Icon(
                    painter = painterResource(R.drawable.ic_arrow_right),
                    contentDescription = null,
                    tint = colorResource(R.color.light_grey),
                    modifier = Modifier.size(dimensionResource(R.dimen.m3_comp_list_icon_size))
                )
            }
        }

        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.m3_sys_spacing_custom_14)))
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
                start = dimensionResource(R.dimen.m3_sys_spacing_1),
                top = dimensionResource(R.dimen.m3_sys_spacing_custom_10),
                bottom = dimensionResource(R.dimen.m3_sys_spacing_custom_10)
            )
    ) {
        SkeletonPlaceholder(
            width = dimensionResource(R.dimen.m3_comp_skeleton_year_width),
            height = dimensionResource(R.dimen.m3_comp_skeleton_year_height)
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
                .padding(top = dimensionResource(R.dimen.m3_sys_spacing_2)),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                SkeletonPlaceholder(
                    width = dimensionResource(R.dimen.m3_comp_skeleton_list_date_width),
                    height = dimensionResource(R.dimen.m3_comp_skeleton_list_date_height),
                    modifier = Modifier.padding(start = dimensionResource(R.dimen.m3_sys_spacing_1))
                )
                Spacer(modifier = Modifier.height(dimensionResource(R.dimen.m3_sys_spacing_half)))
                SkeletonPlaceholder(
                    width = dimensionResource(R.dimen.m3_comp_skeleton_list_type_width),
                    height = dimensionResource(R.dimen.m3_comp_skeleton_list_type_height),
                    modifier = Modifier.padding(start = dimensionResource(R.dimen.m3_sys_spacing_1))
                )
                Spacer(modifier = Modifier.height(dimensionResource(R.dimen.m3_sys_spacing_1)))
                Box(
                    modifier = Modifier.padding(start = dimensionResource(R.dimen.m3_sys_spacing_1))
                ) {
                    SkeletonPlaceholder(
                        width = dimensionResource(R.dimen.m3_comp_skeleton_list_status_width),
                        height = dimensionResource(R.dimen.m3_comp_skeleton_list_status_height),
                        shape = RoundedCornerShape(dimensionResource(R.dimen.m3_comp_shape_corner_radius_small))
                    )
                }
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(end = dimensionResource(R.dimen.m3_sys_spacing_1))
            ) {
                SkeletonPlaceholder(
                    width = dimensionResource(R.dimen.m3_comp_skeleton_list_amount_width),
                    height = dimensionResource(R.dimen.m3_comp_skeleton_list_amount_height)
                )
                Spacer(modifier = Modifier.width(dimensionResource(R.dimen.m3_sys_spacing_custom_12)))
                SkeletonPlaceholder(
                    width = dimensionResource(R.dimen.m3_comp_skeleton_list_arrow_width),
                    height = dimensionResource(R.dimen.m3_comp_skeleton_list_arrow_height)
                )
            }
        }

        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.m3_sys_spacing_custom_14)))
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
    Box(
        modifier = modifier
            .width(width)
            .height(height)
            .background(colorResource(R.color.color_skeleton_background), shape)
    )
}

@Preview(name = "Invoice Header - Light", showBackground = true)
@Preview(name = "Invoice Header - Dark", uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
private fun PreviewInvoiceHeaderItemComposable() {
    MaterialTheme { InvoiceHeaderItemComposable(year = "2024") }
}

@Preview(name = "Invoice Row - Light", showBackground = true)
@Preview(name = "Invoice Row - Dark", uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
private fun PreviewInvoiceRowItemComposable() {
    MaterialTheme {
        InvoiceRowItemComposable(
            date = "8 de marzo",
            type = "Factura Luz",
            status = "Pendiente de Pago",
            amount = "20,00 €"
        )
    }
}

@Preview(name = "Skeleton Header - Light", showBackground = true)
@Preview(name = "Skeleton Header - Dark", uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
private fun PreviewSkeletonInvoiceHeaderItemComposable() {
    MaterialTheme { SkeletonInvoiceHeaderItemComposable() }
}

@Preview(name = "Skeleton Row - Light", showBackground = true)
@Preview(name = "Skeleton Row - Dark", uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
private fun PreviewSkeletonInvoiceRowItemComposable() {
    MaterialTheme { SkeletonInvoiceRowItemComposable() }
}