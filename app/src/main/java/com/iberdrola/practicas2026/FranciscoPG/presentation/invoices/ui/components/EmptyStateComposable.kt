package com.iberdrola.practicas2026.FranciscoPG.presentation.invoices.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.iberdrola.practicas2026.FranciscoPG.presentation.invoices.ui.preview.DevicePreview
import com.iberdrola.practicas2026.FranciscoPG.R
import com.iberdrola.practicas2026.FranciscoPG.presentation.theme.Spacing
import com.iberdrola.practicas2026.FranciscoPG.presentation.theme.IconSize
import com.iberdrola.practicas2026.FranciscoPG.presentation.theme.TextSize

private val EmptyStateBold = FontFamily(Font(R.font.iberpangea_bold, FontWeight.Bold))
private val EmptyStateRegular = FontFamily(Font(R.font.iberpangea_regular, FontWeight.Normal))

@Composable
fun EmptyStateComposable(
    title: String,
    subtitle: String,
    modifier: Modifier = Modifier,
    iconRes: Int = R.drawable.ic_empty_invoices
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(
                PaddingValues(
                    start = Spacing.dp32,
                    end = Spacing.dp32,
                    top = Spacing.dp64,
                )
            ),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Icon(
            painter = painterResource(iconRes),
            contentDescription = null,
            tint = colorResource(R.color.iberdrola_green),
            modifier = Modifier.size(IconSize.dp90)
        )

        Spacer(modifier = Modifier.height(Spacing.dp24))

        Text(
            text = title,
            fontFamily = EmptyStateBold,
            fontWeight = FontWeight.Bold,
            fontSize = TextSize.sp22,
            color = colorResource(R.color.dark_grey_text),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(Spacing.dp8))

        Text(
            text = subtitle,
            fontFamily = EmptyStateRegular,
            fontSize = TextSize.sp16,
            color = colorResource(R.color.color_text_subtitle),
            textAlign = TextAlign.Center
        )
    }
}

@DevicePreview
@Composable
private fun PreviewEmptyStateComposable() {
    MaterialTheme {
        EmptyStateComposable(
            title = "Sin facturas",
            subtitle = "No tienes facturas de luz en este momento."
        )
    }
}

@DevicePreview
@Composable
private fun PreviewEmptyStateGlobalComposable() {
    MaterialTheme {
        EmptyStateComposable(
            title = "Sin facturas",
            subtitle = "No tienes facturas disponibles en este momento."
        )
    }
}