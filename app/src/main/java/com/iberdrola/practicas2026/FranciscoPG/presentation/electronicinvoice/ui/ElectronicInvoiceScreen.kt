package com.iberdrola.practicas2026.FranciscoPG.presentation.electronicinvoice.ui

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import com.iberdrola.practicas2026.FranciscoPG.R
import com.iberdrola.practicas2026.FranciscoPG.presentation.common.BackButton
import com.iberdrola.practicas2026.FranciscoPG.presentation.myinvoices.ui.components.UnavailableBanner
import com.iberdrola.practicas2026.FranciscoPG.presentation.theme.IberFontBold
import com.iberdrola.practicas2026.FranciscoPG.presentation.theme.IberFontRegular
import com.iberdrola.practicas2026.FranciscoPG.presentation.theme.IberdrolaTheme
import com.iberdrola.practicas2026.FranciscoPG.presentation.theme.IconSize
import com.iberdrola.practicas2026.FranciscoPG.presentation.theme.Radius
import com.iberdrola.practicas2026.FranciscoPG.presentation.theme.Spacing
import com.iberdrola.practicas2026.FranciscoPG.presentation.theme.TextSize

@Composable
fun ElectronicInvoiceScreen(
    onNavigateBack: () -> Unit
) {
    val colors = IberdrolaTheme.colors
    var showBanner by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(colors.background)
                .padding(top = Spacing.dp24)
        ) {
            BackButton(
                text = stringResource(R.string.my_invoices_back),
                onClick = onNavigateBack,
                modifier = Modifier.padding(start = Spacing.dp16, top = Spacing.dp16),
                color = colors.iberdrolaDarkGreen,
                fontSize = TextSize.sp16
            )

            Text(
                text = stringResource(R.string.electronic_invoice_title),
                color = colors.textPrimary,
                fontFamily = IberFontBold,
                fontWeight = FontWeight.Bold,
                fontSize = TextSize.sp28,
                modifier = Modifier.padding(
                    start = Spacing.dp24,
                    top = Spacing.dp16,
                    bottom = Spacing.dp24
                )
            )

            ContractRow(
                iconRes = R.drawable.ic_light,
                title = stringResource(R.string.electronic_invoice_light_contract),
                statusText = stringResource(R.string.electronic_invoice_status_active),
                isActive = true,
                onClick = { showBanner = true }
            )

            HorizontalDivider(
                modifier = Modifier.padding(horizontal = Spacing.dp24),
                color = colors.divider
            )

            ContractRow(
                iconRes = R.drawable.ic_gas,
                title = stringResource(R.string.electronic_invoice_gas_contract),
                statusText = stringResource(R.string.electronic_invoice_status_inactive),
                isActive = false,
                onClick = { showBanner = true }
            )

            HorizontalDivider(
                modifier = Modifier.padding(horizontal = Spacing.dp24),
                color = colors.divider
            )
        }

        UnavailableBanner(
            visible = showBanner,
            onDismiss = { showBanner = false },
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .navigationBarsPadding().padding(bottom = Spacing.dp32)
        )
    }
}

@Composable
private fun ContractRow(
    iconRes: Int,
    title: String,
    statusText: String,
    isActive: Boolean,
    onClick: () -> Unit
) {
    val colors = IberdrolaTheme.colors

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = Spacing.dp24, vertical = Spacing.dp16),
        verticalAlignment = Alignment.Top
    ) {
        Icon(
            painter = painterResource(iconRes),
            contentDescription = null,
            tint = colors.iberdrolaDarkGreen,
            modifier = Modifier
                .size(IconSize.dp32)
                .alignByBaseline()
        )

        Spacer(modifier = Modifier.width(Spacing.dp16))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                color = colors.textPrimary,
                fontFamily = IberFontBold,
                fontSize = TextSize.sp15
            )
            Spacer(modifier = Modifier.height(Spacing.dp12))
            Box(
                modifier = Modifier
                    .background(
                        color = if (isActive) colors.statusActive
                        else colors.statusDefault,
                        shape = RoundedCornerShape(Radius.dp10)
                    )
                    .padding(horizontal = Spacing.dp12, vertical = Spacing.dp8)
            ) {
                Text(
                    text = statusText,
                    color = if (isActive) colors.iberdrolaGreen else colors.statusDefaultText,
                    fontFamily = IberFontBold,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = TextSize.sp12
                )
            }
        }

        Icon(
            painter = painterResource(R.drawable.ic_arrow_right),
            contentDescription = null,
            tint = colors.lightGrey,
            modifier = Modifier
                .size(IconSize.dp28)
                .align(Alignment.CenterVertically)
        )
    }
}

@Preview(name = "Electronic Invoice - Light", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_NO)
@Preview(name = "Electronic Invoice - Dark", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun ElectronicInvoiceScreenPreview() {
    IberdrolaTheme {
        ElectronicInvoiceScreen(onNavigateBack = {})
    }
}
