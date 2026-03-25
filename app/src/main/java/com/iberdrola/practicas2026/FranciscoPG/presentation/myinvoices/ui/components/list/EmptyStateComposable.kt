package com.iberdrola.practicas2026.FranciscoPG.presentation.myinvoices.ui.components.list

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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import com.iberdrola.practicas2026.FranciscoPG.presentation.theme.IberFontBold
import com.iberdrola.practicas2026.FranciscoPG.presentation.theme.IberFontRegular
import androidx.compose.ui.text.style.TextAlign
import com.iberdrola.practicas2026.FranciscoPG.presentation.myinvoices.ui.preview.DevicePreview
import com.iberdrola.practicas2026.FranciscoPG.R
import com.iberdrola.practicas2026.FranciscoPG.presentation.theme.IberdrolaTheme
import com.iberdrola.practicas2026.FranciscoPG.presentation.theme.Spacing
import com.iberdrola.practicas2026.FranciscoPG.presentation.theme.IconSize
import com.iberdrola.practicas2026.FranciscoPG.presentation.theme.TextSize

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
            tint = IberdrolaTheme.colors.iberdrolaGreen,
            modifier = Modifier.size(IconSize.dp90)
        )

        Spacer(modifier = Modifier.height(Spacing.dp24))

        Text(
            text = title,
            fontFamily = IberFontBold,
            fontWeight = FontWeight.Bold,
            fontSize = TextSize.sp22,
            color = IberdrolaTheme.colors.darkGreyText,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(Spacing.dp8))

        Text(
            text = subtitle,
            fontFamily = IberFontRegular,
            fontSize = TextSize.sp16,
            color = IberdrolaTheme.colors.textSubtitle,
            textAlign = TextAlign.Center
        )
    }
}

@DevicePreview
@Composable
private fun PreviewEmptyStateComposable() {
    IberdrolaTheme {
        EmptyStateComposable(
            title = "Sin facturas",
            subtitle = "No tienes facturas de luz en este momento."
        )
    }
}

@DevicePreview
@Composable
private fun PreviewEmptyStateGlobalComposable() {
    IberdrolaTheme {
        EmptyStateComposable(
            title = "Sin facturas",
            subtitle = "No tienes facturas disponibles en este momento."
        )
    }
}
