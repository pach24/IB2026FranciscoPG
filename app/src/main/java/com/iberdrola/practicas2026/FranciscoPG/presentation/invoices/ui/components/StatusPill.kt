package com.iberdrola.practicas2026.FranciscoPG.presentation.invoices.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.iberdrola.practicas2026.FranciscoPG.R

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
                shape = RoundedCornerShape(dimensionResource(R.dimen.m3_comp_shape_corner_radius_small))
            )
            .padding(
                horizontal = dimensionResource(R.dimen.m3_sys_spacing_custom_12),
                vertical = dimensionResource(R.dimen.m3_sys_spacing_custom_6)
            ),
        fontFamily = StatusFontBold,
        fontWeight = FontWeight.Bold,
        fontSize = dimensionResource(R.dimen.m3_sys_typescale_micro).value.sp,
        color = colorResource(if (isPaid) R.color.iberdrola_dark_green else R.color.red_600)
    )
}
