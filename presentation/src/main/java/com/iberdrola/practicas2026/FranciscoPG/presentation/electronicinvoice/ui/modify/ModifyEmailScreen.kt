package com.iberdrola.practicas2026.FranciscoPG.presentation.electronicinvoice.ui.modify
import com.iberdrola.practicas2026.FranciscoPG.presentation.R

import android.content.res.Configuration
import androidx.compose.foundation.clickable
import androidx.compose.foundation.background
import androidx.compose.ui.draw.clip
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.unit.dp
import com.iberdrola.practicas2026.FranciscoPG.presentation.common.BackButton
import com.iberdrola.practicas2026.FranciscoPG.presentation.common.InfoDialog
import com.iberdrola.practicas2026.FranciscoPG.presentation.common.UnavailableBanner
import com.iberdrola.practicas2026.FranciscoPG.presentation.theme.IberFontBold
import com.iberdrola.practicas2026.FranciscoPG.presentation.theme.IberFontRegular
import com.iberdrola.practicas2026.FranciscoPG.presentation.theme.IberdrolaTheme
import com.iberdrola.practicas2026.FranciscoPG.presentation.theme.IconSize
import com.iberdrola.practicas2026.FranciscoPG.presentation.theme.Radius
import com.iberdrola.practicas2026.FranciscoPG.presentation.theme.Spacing
import com.iberdrola.practicas2026.FranciscoPG.presentation.theme.TextSize

@Composable
fun ModifyEmailScreen(
    currentEmail: String,
    onModifyClick: () -> Unit,
    onNavigateBack: () -> Unit
) {
    val colors = IberdrolaTheme.colors
    var showInfoDialog by remember { mutableStateOf(false) }
    var showUnavailableBanner by remember { mutableStateOf(false) }

    if (showInfoDialog) {
        InfoDialog(
            title = stringResource(R.string.info_dialog_title),
            message = stringResource(R.string.info_dialog_message),
            linkText = stringResource(R.string.info_dialog_link),
            closeText = stringResource(R.string.info_dialog_close),
            onLinkClick = {
                showInfoDialog = false
                showUnavailableBanner = true
            },
            onDismiss = { showInfoDialog = false }
        )
    }

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

        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
        ) {
            // Contract title
            Text(
                text = stringResource(R.string.electronic_invoice_light_contract),
                color = colors.textPrimary,
                fontFamily = IberFontBold,
                fontWeight = FontWeight.Bold,
                fontSize = TextSize.sp28,
                modifier = Modifier.padding(
                    start = Spacing.dp24,
                    top = Spacing.dp16
                )
            )
            Spacer(modifier = Modifier.height(Spacing.dp12))
            // Direccion
            Text(
                text = stringResource(R.string.modify_email_contract_address),
                color = colors.darkGreyText,
                fontFamily = IberFontBold,
                fontSize = TextSize.sp18,
                lineHeight = TextSize.sp22,
                modifier = Modifier.padding(
                    start = Spacing.dp24,
                    end = Spacing.dp24,
                    top = Spacing.dp4
                )
            )

            Spacer(modifier = Modifier.height(Spacing.dp16))

            // "Actualmente..."
            Text(
                text = stringResource(R.string.modify_email_currently_receiving),
                color = colors.darkGreyText,
                fontFamily = IberFontRegular,
                fontSize = TextSize.sp14,
                lineHeight = TextSize.sp22,
                modifier = Modifier.padding(horizontal = Spacing.dp24)
            )

            Spacer(modifier = Modifier.height(Spacing.dp20))



            Spacer(modifier = Modifier.height(Spacing.dp16))

            // Recibes
            Text(
                text = stringResource(R.string.modify_email_email_section_title),
                color = colors.textPrimary,
                fontFamily = IberFontBold,
                fontWeight = FontWeight.Bold,
                fontSize = TextSize.sp15,
                modifier = Modifier.padding(horizontal = Spacing.dp24)
            )

            Spacer(modifier = Modifier.height(Spacing.dp12))

            // Email actual
            Column(
                modifier = Modifier
                    .padding(
                        start = Spacing.dp24,
                        end = Spacing.dp24,
                        top = Spacing.dp4
                    )
                    .clip(RoundedCornerShape(Radius.dp24))
                    .clickable { onModifyClick() }
                    .padding(horizontal = Spacing.dp16, vertical = Spacing.dp4)
            ) {
                Text(
                    text = currentEmail,
                    color = colors.lightGrey,
                    fontFamily = IberFontRegular,
                    fontSize = TextSize.sp14,
                )
                Spacer(modifier = Modifier.height(Spacing.dp10))
                HorizontalDivider(color = colors.divider)
            }

            Spacer(modifier = Modifier.height(Spacing.dp24))

            // Info

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = Spacing.dp24)
                    .clip(RoundedCornerShape(Radius.dp32))
                    .clickable { showInfoDialog = true }
                    .padding(horizontal = Spacing.dp16, vertical = Spacing.dp8),
                verticalAlignment = Alignment.Top
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_info),
                    contentDescription = null,
                    tint = colors.lightGrey,
                    modifier = Modifier.size(IconSize.dp24)
                )

                Spacer(modifier = Modifier.width(Spacing.dp12))

                Text(
                    text = stringResource(R.string.modify_email_info_banner),
                    color = colors.lightGrey,
                    fontFamily = IberFontRegular,
                    fontSize = TextSize.sp12,
                    lineHeight = TextSize.sp22,
                    modifier = Modifier.weight(1f)
                )
            }
        }

        UnavailableBanner(
            visible = showUnavailableBanner,
            onDismiss = { showUnavailableBanner = false }
        )

        HorizontalDivider(
            Modifier.height(Spacing.dp4),
            color = colors.divider
        )
        // Botón modificar email
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = Spacing.dp24, vertical = Spacing.dp24)
                .navigationBarsPadding(),
            contentAlignment = Alignment.Center
        ) {
            Button(
                onClick = onModifyClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(Radius.dp50),
                colors = ButtonDefaults.buttonColors(
                    containerColor = colors.iberdrolaDarkGreen,
                    contentColor = colors.white
                )
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_edit),
                    contentDescription = null,
                    modifier = Modifier.size(IconSize.dp24)
                )
                Spacer(modifier = Modifier.width(Spacing.dp8))
                Text(
                    text = stringResource(R.string.modify_email_button),
                    fontFamily = IberFontBold,
                    fontWeight = FontWeight.Bold,
                    fontSize = TextSize.sp16
                )
            }
        }
    }
}

@Preview(name = "Modify Email Screen - Light", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_NO)
@Preview(name = "Modify Email Screen - Dark", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun ModifyEmailScreenPreview() {
    IberdrolaTheme {
        ModifyEmailScreen(
            currentEmail = "pepe2@gmail.com",
            onModifyClick = {},
            onNavigateBack = {}
        )
    }
}
