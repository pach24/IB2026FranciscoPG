package com.iberdrola.practicas2026.FranciscoPG.presentation.invoices.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.iberdrola.practicas2026.FranciscoPG.presentation.invoices.ui.preview.DevicePreview
import androidx.compose.ui.unit.sp
import com.iberdrola.practicas2026.FranciscoPG.R

private val ErrorStateBold = FontFamily(Font(R.font.iberpangea_bold, FontWeight.Bold))
private val ErrorStateRegular = FontFamily(Font(R.font.iberpangea_regular, FontWeight.Normal))

@Composable
fun ErrorStateComposable(
    title: String,
    subtitle: String,
    iconRes: Int,
    onRetryClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = dimensionResource(R.dimen.m3_sys_spacing_4)),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            painter = painterResource(iconRes),
            contentDescription = null,
            tint = colorResource(R.color.color_stroke_neutral),
            modifier = Modifier.size(dimensionResource(R.dimen.m3_comp_error_state_icon_size))
        )

        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.m3_sys_spacing_3)))

        Text(
            text = title,
            fontFamily = ErrorStateBold,
            fontWeight = FontWeight.Bold,
            fontSize = dimensionResource(R.dimen.m3_sys_typescale_title2).value.sp,
            color = colorResource(R.color.dark_grey_text),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.m3_sys_spacing_1)))

        Text(
            text = subtitle,
            fontFamily = ErrorStateRegular,
            fontSize = dimensionResource(R.dimen.m3_sys_typescale_body_large).value.sp,
            color = colorResource(R.color.color_text_subtitle),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.m3_sys_spacing_3)))

        OutlinedButton(
            onClick = onRetryClick,
            shape = RoundedCornerShape(dimensionResource(R.dimen.m3_comp_empty_state_button_corner_radius)),
            border = BorderStroke(
                width = dimensionResource(R.dimen.m3_comp_button_stroke_width),
                color = colorResource(R.color.iberdrola_dark_green)
            )
        ) {
            Text(
                text = stringResource(R.string.error_retry_button),
                fontFamily = ErrorStateBold,
                fontWeight = FontWeight.Bold,
                fontSize = dimensionResource(R.dimen.m3_sys_typescale_label).value.sp,
                color = colorResource(R.color.iberdrola_dark_green)
            )
        }
    }
}

@DevicePreview
@Composable
private fun PreviewServerErrorStateComposable() {
    MaterialTheme {
        ErrorStateComposable(
            title = "Error del servidor",
            subtitle = "No hemos podido cargar tus facturas. Inténtalo de nuevo más tarde.",
            iconRes = R.drawable.ic_server_error,
            onRetryClick = {}
        )
    }
}

@DevicePreview
@Composable
private fun PreviewConnectionErrorStateComposable() {
    MaterialTheme {
        ErrorStateComposable(
            title = "Sin conexión",
            subtitle = "Comprueba tu conexión a internet e inténtalo de nuevo.",
            iconRes = R.drawable.ic_connection_error,
            onRetryClick = {}
        )
    }
}
