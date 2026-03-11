package com.iberdrola.practicas2026.FranciscoPG.presentation.invoices.view

import android.content.res.Configuration
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
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.iberdrola.practicas2026.FranciscoPG.R

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
                top = dimensionResource(R.dimen.m3_sys_spacing_custom_10),
                bottom = dimensionResource(R.dimen.m3_sys_spacing_1)
            ),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = stringResource(R.string.title_invoice_history),
            modifier = Modifier
                .weight(1f)
                .padding(start = dimensionResource(R.dimen.m3_sys_spacing_4)),
            fontFamily = StickyHeaderBold,
            fontWeight = FontWeight.Bold,
            fontSize = dimensionResource(R.dimen.m3_sys_typescale_body_large).value.sp,
            color = colorResource(R.color.color_text_high_emphasis)
        )

        OutlinedButton(
            onClick = onFilterClick,
            modifier = Modifier.padding(end = dimensionResource(R.dimen.m3_sys_spacing_3)),
            shape = RoundedCornerShape(dimensionResource(R.dimen.m3_comp_button_corner_radius_full)),
            border = BorderStroke(
                width = dimensionResource(R.dimen.m3_comp_button_stroke_width),
                color = colorResource(R.color.iberdrola_dark_green)
            ),
            colors = ButtonDefaults.outlinedButtonColors(
                containerColor = colorResource(R.color.color_surface),
                contentColor = colorResource(R.color.iberdrola_dark_green)
            )
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_filter),
                contentDescription = null,
                tint = colorResource(R.color.iberdrola_dark_green)
            )
            Text(
                text = stringResource(R.string.action_filter),
                modifier = Modifier.padding(start = dimensionResource(R.dimen.m3_sys_spacing_1)),
                fontFamily = StickyHeaderBold,
                fontWeight = FontWeight.Bold,
                fontSize = dimensionResource(R.dimen.m3_sys_typescale_label).value.sp,
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
                top = dimensionResource(R.dimen.m3_sys_spacing_custom_10),
                bottom = dimensionResource(R.dimen.m3_sys_spacing_1)
            ),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {

        SkeletonHeaderPlaceholder(
            width = dimensionResource(R.dimen.m3_comp_skeleton_header_title_width),
            height = dimensionResource(R.dimen.m3_comp_skeleton_header_title_height),
            modifier = Modifier

                .padding(start = dimensionResource(R.dimen.m3_sys_spacing_4))
        )

        SkeletonHeaderPlaceholder(
            width = dimensionResource(R.dimen.m3_comp_skeleton_header_btn_width),
            height = dimensionResource(R.dimen.m3_comp_skeleton_header_btn_height),
            modifier = Modifier
                .padding(end = dimensionResource(R.dimen.m3_sys_spacing_3)),
            shape = RoundedCornerShape(
                dimensionResource(R.dimen.m3_comp_button_corner_radius_full)
            )
        )
    }
}

@Composable
private fun SkeletonHeaderPlaceholder(
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

@Preview(name = "Sticky Header - Light", showBackground = true)
@Preview(name = "Sticky Header - Dark", uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
private fun PreviewStickyInvoiceHeaderComposable() {
    MaterialTheme {
        StickyInvoiceHeaderComposable()
    }
}

@Preview(name = "Skeleton Sticky Header - Light", showBackground = true)
@Preview(name = "Skeleton Sticky Header - Dark", uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
private fun PreviewSkeletonStickyInvoiceHeaderComposable() {
    MaterialTheme {
        SkeletonStickyInvoiceHeaderComposable()
    }
}
