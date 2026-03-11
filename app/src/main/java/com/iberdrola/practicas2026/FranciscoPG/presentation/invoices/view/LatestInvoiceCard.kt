package com.iberdrola.practicas2026.FranciscoPG.presentation.invoices.view

import android.content.res.Configuration
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
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
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
    iconRes: Int = R.drawable.ic_light
) {
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
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = stringResource(R.string.card_title_latest_invoice),
                        fontFamily = IberFontBold,
                        fontWeight = FontWeight.Bold,
                        fontSize = dimensionResource(R.dimen.m3_sys_typescale_label).value.sp,
                        color = colorResource(R.color.dark_grey_text)
                    )
                    Spacer(modifier = Modifier.height(dimensionResource(R.dimen.m3_sys_spacing_half)))
                    Text(
                        text = supplyType,
                        fontFamily = IberFontRegular,
                        fontSize = dimensionResource(R.dimen.m3_sys_typescale_body_small).value.sp,
                        color = colorResource(R.color.light_grey)
                    )
                }

                Icon(
                    painter = painterResource(iconRes),
                    contentDescription = null,
                    tint = colorResource(R.color.iberdrola_dark_green),
                    modifier = Modifier.size(dimensionResource(R.dimen.m3_comp_card_main_icon_size))
                )
            }

            Spacer(modifier = Modifier.height(dimensionResource(R.dimen.m3_sys_spacing_custom_10)))

            Text(
                text = amount,
                fontFamily = IberFontBold,
                fontWeight = FontWeight.Bold,
                fontSize = dimensionResource(R.dimen.m3_sys_typescale_headline_medium).value.sp,
                color = colorResource(R.color.dark_grey)
            )

            Spacer(modifier = Modifier.height(dimensionResource(R.dimen.m3_sys_spacing_half)))

            Text(
                text = dateRange,
                fontFamily = IberFontRegular,
                fontSize = dimensionResource(R.dimen.m3_sys_typescale_body_small).value.sp,
                color = colorResource(R.color.light_grey)
            )

            Spacer(modifier = Modifier.height(dimensionResource(R.dimen.m3_sys_spacing_custom_10)))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(dimensionResource(R.dimen.m3_comp_divider_thickness))
                    .background(colorResource(R.color.divider))
            )

            Spacer(modifier = Modifier.height(dimensionResource(R.dimen.m3_sys_spacing_custom_12)))

            Text(
                text = status,
                modifier = Modifier
                    .background(
                        color = colorResource(R.color.red_100),
                        shape = RoundedCornerShape(dimensionResource(R.dimen.m3_comp_shape_corner_radius_small))
                    )
                    .padding(
                        horizontal = dimensionResource(R.dimen.m3_sys_spacing_custom_12),
                        vertical = dimensionResource(R.dimen.m3_sys_spacing_custom_6)
                    ),
                fontFamily = IberFontBold,
                fontWeight = FontWeight.Bold,
                fontSize = dimensionResource(R.dimen.m3_sys_typescale_micro).value.sp,
                color = colorResource(R.color.red_600)
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
                    SkeletonBox(
                        width = dimensionResource(R.dimen.m3_comp_skeleton_card_title_width),
                        height = dimensionResource(R.dimen.m3_comp_skeleton_card_title_height)
                    )
                    Spacer(modifier = Modifier.height(dimensionResource(R.dimen.m3_sys_spacing_half)))
                    SkeletonBox(
                        width = dimensionResource(R.dimen.m3_comp_skeleton_card_subtitle_width),
                        height = dimensionResource(R.dimen.m3_comp_skeleton_card_subtitle_height)
                    )
                }

                SkeletonBox(
                    width = dimensionResource(R.dimen.m3_comp_skeleton_card_icon_width),
                    height = dimensionResource(R.dimen.m3_comp_skeleton_card_icon_height)
                )
            }

            Spacer(modifier = Modifier.height(dimensionResource(R.dimen.m3_sys_spacing_custom_10)))
            SkeletonBox(
                width = dimensionResource(R.dimen.m3_comp_skeleton_card_amount_width),
                height = dimensionResource(R.dimen.m3_comp_skeleton_card_amount_height)
            )

            Spacer(modifier = Modifier.height(dimensionResource(R.dimen.m3_sys_spacing_half)))
            SkeletonBox(
                width = dimensionResource(R.dimen.m3_comp_skeleton_card_dates_width),
                height = dimensionResource(R.dimen.m3_comp_skeleton_card_dates_height)
            )

            Spacer(modifier = Modifier.height(dimensionResource(R.dimen.m3_sys_spacing_custom_10)))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(dimensionResource(R.dimen.m3_comp_divider_thickness))
                    .background(colorResource(R.color.color_stroke_neutral))
            )

            Spacer(modifier = Modifier.height(dimensionResource(R.dimen.m3_sys_spacing_custom_12)))
            ShimmerBox(
                width = dimensionResource(R.dimen.m3_comp_skeleton_card_status_width),
                height = dimensionResource(R.dimen.m3_comp_skeleton_card_status_height),
                shape = RoundedCornerShape(dimensionResource(R.dimen.m3_comp_shape_corner_radius_small))
            )
        }
    }
}

@Composable
private fun SkeletonBox(width: androidx.compose.ui.unit.Dp, height: androidx.compose.ui.unit.Dp) {
    ShimmerBox(
        width = width,
        height = height,
        shape = RoundedCornerShape(dimensionResource(R.dimen.m3_comp_skeleton_corner_radius))
    )
}

@Preview(name = "Latest Invoice - Light", showBackground = true)
@Preview(
    name = "Latest Invoice - Dark",
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    showBackground = true
)
@Composable
private fun PreviewLatestInvoiceCardComposable() {
    MaterialTheme {
        LatestInvoiceCardComposable(
            amount = "20,00 \u20AC",
            dateRange = "01 feb. 2024 - 04 mar. 2024"
        )
    }
}

@Preview(name = "Skeleton Latest Invoice - Light", showBackground = true)
@Preview(
    name = "Skeleton Latest Invoice - Dark",
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    showBackground = true
)
@Composable
private fun PreviewSkeletonLatestInvoiceCardComposable() {
    MaterialTheme {
        SkeletonLatestInvoiceCardComposable()
    }
}