package com.iberdrola.practicas2026.FranciscoPG.presentation.electronicinvoice.ui

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import com.iberdrola.practicas2026.FranciscoPG.R
import com.iberdrola.practicas2026.FranciscoPG.presentation.theme.IberFontBold
import com.iberdrola.practicas2026.FranciscoPG.presentation.theme.IberFontRegular
import com.iberdrola.practicas2026.FranciscoPG.presentation.theme.IberdrolaTheme
import com.iberdrola.practicas2026.FranciscoPG.presentation.theme.IconSize
import com.iberdrola.practicas2026.FranciscoPG.presentation.theme.Radius
import com.iberdrola.practicas2026.FranciscoPG.presentation.theme.Spacing
import com.iberdrola.practicas2026.FranciscoPG.presentation.theme.Stroke
import com.iberdrola.practicas2026.FranciscoPG.presentation.theme.TextSize

@Composable
fun ConfirmElectronicInvoiceContent(
    verificationCode: String,
    onVerificationCodeChanged: (String) -> Unit,
    onResendCode: () -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = IberdrolaTheme.colors

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        Spacer(modifier = Modifier.height(Spacing.dp24))

        // Título
        Text(
            text = stringResource(R.string.confirm_einvoice_title),
            color = colors.textPrimary,
            fontFamily = IberFontBold,
            fontWeight = FontWeight.Bold,
            fontSize = TextSize.sp17,
            modifier = Modifier.padding(horizontal = Spacing.dp24)
        )

        Spacer(modifier = Modifier.height(Spacing.dp18))

        // Subtítulo
        Text(
            text = stringResource(R.string.confirm_einvoice_subtitle),
            color = colors.darkGreyText,
            fontFamily = IberFontRegular,
            fontSize = TextSize.sp14,
            lineHeight = TextSize.sp14,
            modifier = Modifier.padding(horizontal = Spacing.dp24)
        )

        Spacer(modifier = Modifier.height(Spacing.dp32))

        // Campo código de verificación
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = Spacing.dp24)
        ) {
            BasicTextField(
                value = verificationCode,
                onValueChange = onVerificationCodeChanged,
                singleLine = true,
                textStyle = TextStyle(
                    fontFamily = IberFontRegular,
                    fontSize = TextSize.sp15,
                    color = colors.textPrimary
                ),
                cursorBrush = SolidColor(colors.iberdrolaDarkGreen),
                modifier = Modifier.fillMaxWidth(),
                decorationBox = { innerTextField ->
                    Column {
                        Box(modifier = Modifier.padding(bottom = Spacing.dp8)) {
                            if (verificationCode.isEmpty()) {
                                Text(
                                    text = stringResource(R.string.confirm_einvoice_code_label),
                                    fontFamily = IberFontRegular,
                                    color = colors.darkGreyText, // Oscurecido ligeramente para parecerse al diseño
                                    fontSize = TextSize.sp14
                                )
                            }
                            innerTextField()
                        }
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(Stroke.dp1)
                                .background(colors.textPrimary.copy(alpha = 0.6f)) // Línea más oscura
                        )
                    }
                }
            )
        }

        Spacer(modifier = Modifier.height(Spacing.dp32))

        // Banner informativo
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = Spacing.dp20)
                .background(
                    color = colors.infoBannerBackground,
                    shape = RoundedCornerShape(
                        topStart = Spacing.dp0,
                        topEnd = Radius.dp16,
                        bottomEnd = Radius.dp16,
                        bottomStart = Radius.dp16
                    )
                )
                .padding(Spacing.dp20),
            verticalAlignment = Alignment.Top
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_info),
                contentDescription = null,
                tint = colors.infoBannerIcon,
                modifier = Modifier.size(IconSize.dp24)
            )

            Spacer(modifier = Modifier.width(Spacing.dp12))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = stringResource(R.string.confirm_einvoice_not_received),
                    color = colors.textPrimary,
                    fontFamily = IberFontBold,
                    fontWeight = FontWeight.Bold,
                    fontSize = TextSize.sp12
                )

                Spacer(modifier = Modifier.height(Spacing.dp4))

                Text(
                    text = stringResource(R.string.confirm_einvoice_resend_hint),
                    color = colors.darkGreyText,
                    fontFamily = IberFontRegular,
                    fontSize = TextSize.sp12,
                    lineHeight = TextSize.sp22
                )

                Spacer(modifier = Modifier.height(Spacing.dp6))

                Text(
                    text = stringResource(R.string.confirm_einvoice_resend_link),
                    color = colors.textPrimary,
                    fontFamily = IberFontBold,
                    fontWeight = FontWeight.Bold,
                    fontSize = TextSize.sp12,
                    textDecoration = TextDecoration.Underline,
                    modifier = Modifier.clickable { onResendCode() }
                )
            }
        }
    }
}

@Preview(name = "Confirm Content - Light", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_NO)
@Preview(name = "Confirm Content - Dark", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun ConfirmElectronicInvoiceContentPreview() {
    IberdrolaTheme {
        ConfirmElectronicInvoiceContent(
            verificationCode = "",
            onVerificationCodeChanged = {},
            onResendCode = {}
        )
    }
}