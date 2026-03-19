package com.iberdrola.practicas2026.FranciscoPG.presentation.invoices.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import com.iberdrola.practicas2026.FranciscoPG.presentation.invoices.ui.preview.DevicePreview
import com.iberdrola.practicas2026.FranciscoPG.R
import com.iberdrola.practicas2026.FranciscoPG.presentation.theme.IberdrolaTheme
import com.iberdrola.practicas2026.FranciscoPG.presentation.theme.Spacing
import com.iberdrola.practicas2026.FranciscoPG.presentation.theme.IconSize
import com.iberdrola.practicas2026.FranciscoPG.presentation.theme.Radius
import com.iberdrola.practicas2026.FranciscoPG.presentation.theme.Stroke
import com.iberdrola.practicas2026.FranciscoPG.presentation.theme.TextSize

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
            .verticalScroll(rememberScrollState())
            .padding(horizontal = Spacing.dp32),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            painter = painterResource(iconRes),
            contentDescription = null,
            tint = IberdrolaTheme.colors.iberdrolaGreen,
            modifier = Modifier.size(IconSize.dp90)
        )

        Spacer(modifier = Modifier.height(Spacing.dp24))

        Text(
            text = title,
            fontFamily = ErrorStateBold,
            fontWeight = FontWeight.Bold,
            fontSize = TextSize.sp22,
            color = IberdrolaTheme.colors.darkGreyText,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(Spacing.dp8))

        Text(
            text = subtitle,
            fontFamily = ErrorStateRegular,
            fontSize = TextSize.sp16,
            color = IberdrolaTheme.colors.textSubtitle,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(Spacing.dp24))

        OutlinedButton(
            onClick = onRetryClick,
            shape = RoundedCornerShape(Radius.dp24),
            border = BorderStroke(
                width = Stroke.dp2,
                color = IberdrolaTheme.colors.iberdrolaDarkGreen
            )
        ) {
            Text(
                text = stringResource(R.string.error_retry_button),
                fontFamily = ErrorStateBold,
                fontWeight = FontWeight.Bold,
                fontSize = TextSize.sp14,
                color = IberdrolaTheme.colors.iberdrolaDarkGreen
            )
        }
    }
}

@DevicePreview
@Composable
private fun PreviewServerErrorStateComposable() {
    IberdrolaTheme {
        ErrorStateComposable(
            title = "Error del servidor",
            subtitle = "No hemos podido cargar tus facturas. Inténtalo de nuevo más tarde.",
            iconRes = R.drawable.ic_server_off,
            onRetryClick = {}
        )
    }
}

@DevicePreview
@Composable
private fun PreviewConnectionErrorStateComposable() {
    IberdrolaTheme {
        ErrorStateComposable(
            title = "Sin conexión",
            subtitle = "Comprueba tu conexión a internet e inténtalo de nuevo.",
            iconRes = R.drawable.ic_connection_error,
            onRetryClick = {}
        )
    }
}
