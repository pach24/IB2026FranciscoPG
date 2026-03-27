package com.iberdrola.practicas2026.FranciscoPG.presentation.electronicinvoice.ui

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.iberdrola.practicas2026.FranciscoPG.R
import com.iberdrola.practicas2026.FranciscoPG.presentation.common.RoundedCheckbox
import com.iberdrola.practicas2026.FranciscoPG.presentation.common.UnavailableBanner
import com.iberdrola.practicas2026.FranciscoPG.presentation.theme.IberFontBold
import com.iberdrola.practicas2026.FranciscoPG.presentation.theme.IberFontRegular
import com.iberdrola.practicas2026.FranciscoPG.presentation.theme.IberdrolaTheme
import com.iberdrola.practicas2026.FranciscoPG.presentation.theme.Spacing
import com.iberdrola.practicas2026.FranciscoPG.presentation.theme.Stroke
import com.iberdrola.practicas2026.FranciscoPG.presentation.theme.TextSize

@Composable
fun ActivateElectronicInvoiceContent(
    email: String,
    legalAccepted: Boolean,
    isEmailValid: Boolean,
    onEmailChanged: (String) -> Unit,
    onLegalAcceptedChanged: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = IberdrolaTheme.colors
    val focusManager = LocalFocusManager.current
    var showBanner by remember { mutableStateOf(false) }
    var emailHasBlurred by remember { mutableStateOf(false) }

    Box(modifier = modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null
                ) { focusManager.clearFocus() }
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(modifier = Modifier.height(Spacing.dp24))

            // Info de Email actual
            Column(modifier = Modifier.padding(horizontal = Spacing.dp24)) {
                Text(
                    text = stringResource(R.string.activate_einvoice_linked_email),
                    color = colors.darkGreyText,
                    fontFamily = IberFontRegular,
                    fontSize = TextSize.sp14
                )
                Text(
                    text = stringResource(R.string.email_censored),
                    color = colors.textPrimary,
                    fontFamily = IberFontBold,
                    fontWeight = FontWeight.Bold,
                    fontSize = TextSize.sp14,
                    modifier = Modifier.padding(top = 2.dp)
                )
            }

            Spacer(modifier = Modifier.height(Spacing.dp28))

            Text(
                text = stringResource(R.string.activate_einvoice_question),
                color = colors.textPrimary,
                fontFamily = IberFontBold,
                fontWeight = FontWeight.Bold,
                fontSize = TextSize.sp17,
                modifier = Modifier.padding(horizontal = Spacing.dp24)
            )

            Spacer(modifier = Modifier.height(Spacing.dp8))

            // TextField tipo línea inferior
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = Spacing.dp24)
            ) {
                BasicTextField(
                    value = email,
                    onValueChange = onEmailChanged,
                    singleLine = true,
                    textStyle = TextStyle(
                        fontFamily = IberFontRegular,
                        fontSize = TextSize.sp15,
                        color = colors.textPrimary
                    ),
                    cursorBrush = SolidColor(colors.iberdrolaDarkGreen),
                    modifier = Modifier
                        .fillMaxWidth()
                        .onFocusChanged { focusState ->
                            if (focusState.isFocused) {
                                emailHasBlurred = false
                            } else if (email.isNotEmpty()) {
                                emailHasBlurred = true
                            }
                        },
                    decorationBox = { innerTextField ->
                        val showError = emailHasBlurred && email.isNotEmpty() && !isEmailValid
                        val underlineColor = when {
                            showError -> colors.errorTextForm
                            isEmailValid && email.isNotEmpty() -> colors.iberdrolaGreen
                            else -> colors.lightGrey.copy(alpha = 0.5f)
                        }

                        Column {
                            Box(modifier = Modifier.padding(bottom = Spacing.dp8, top = Spacing.dp20)) {
                                if (email.isEmpty()) {
                                    Text(
                                        text = stringResource(R.string.activate_einvoice_email_label),
                                        fontFamily = IberFontRegular,
                                        color = colors.lightGrey,
                                        fontSize = TextSize.sp14
                                    )
                                }
                                innerTextField()
                            }
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(Stroke.dp2)
                                    .background(underlineColor)
                            )
                            if (showError) {
                                Text(
                                    text = stringResource(R.string.activate_einvoice_email_error),
                                    color = colors.errorTextForm,
                                    fontFamily = IberFontRegular,
                                    fontSize = TextSize.sp12,
                                    modifier = Modifier.padding(top = Spacing.dp4)
                                )
                            }
                        }
                    }
                )
            }

            Spacer(modifier = Modifier.height(Spacing.dp32))

            Text(
                text = stringResource(R.string.activate_einvoice_data_protection_title),
                color = colors.textPrimary,
                fontFamily = IberFontBold,
                fontWeight = FontWeight.Bold,
                fontSize = TextSize.sp17,
                modifier = Modifier.padding(horizontal = Spacing.dp24)
            )

            Spacer(modifier = Modifier.height(Spacing.dp12))

            Column(modifier = Modifier.padding(horizontal = Spacing.dp24)) {
                DataProtectionItem(
                    boldPrefix = "Responsable:",
                    text = " Iberdrola Clientes S.A.U.",
                    linkText = stringResource(R.string.activate_einvoice_more_info),
                    onLinkClick = { focusManager.clearFocus(); showBanner = true }
                )
                Spacer(modifier = Modifier.height(Spacing.dp8))
                DataProtectionItem(
                    boldPrefix = "Finalidad:",
                    text = " Gestión de la factura electrónica.",
                    linkText = stringResource(R.string.activate_einvoice_more_info),
                    onLinkClick = { focusManager.clearFocus(); showBanner = true }
                )
                Spacer(modifier = Modifier.height(Spacing.dp8))
                DataProtectionItem(
                    boldPrefix = "Derechos:",
                    text = " Acceso, rectificación, supresión, limitación del tratamiento, portabilidad de datos u oposición, incluida la oposición a decisiones individuales automatizadas.",
                    linkText = stringResource(R.string.activate_einvoice_more_info),
                    onLinkClick = { focusManager.clearFocus(); showBanner = true }
                )
            }

            Spacer(modifier = Modifier.height(Spacing.dp24))

            // Checkbox legal
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { focusManager.clearFocus(); onLegalAcceptedChanged(!legalAccepted) }
                    .padding(horizontal = Spacing.dp24),
                verticalAlignment = Alignment.Top
            ) {
                RoundedCheckbox(
                    checked = legalAccepted,
                    checkedColor = colors.iberdrolaDarkGreen,
                    uncheckedBorderColor = colors.iberdrolaDarkGreen,
                    checkmarkColor = colors.white,
                    modifier = Modifier.padding(top = 2.dp)
                )
                Spacer(modifier = Modifier.width(Spacing.dp12))

                val legalAnnotated = buildAnnotatedString {
                    append("He leído y acepto la Política de privacidad, acepto las ")
                    pushStringAnnotation(tag = "LINK", annotation = "conditions")
                    withStyle(SpanStyle(
                        color = colors.iberdrolaDarkGreen,
                        textDecoration = TextDecoration.Underline,
                        fontWeight = FontWeight.Bold
                    )) {
                        append("Condiciones Generales")
                    }
                    pop()
                    append(" y Particulares de la oferta y la suscripción a Factura Electrónica.")
                }

                ClickableText(
                    text = legalAnnotated,
                    modifier = Modifier.padding(top = Spacing.dp10),
                    style = TextStyle(
                        color = colors.darkGreyText,
                        fontFamily = IberFontRegular,
                        fontSize = TextSize.sp16,
                        lineHeight = TextSize.sp22
                    ),
                    onClick = { offset ->
                        focusManager.clearFocus()
                        legalAnnotated.getStringAnnotations("LINK", offset, offset)
                            .firstOrNull()?.let { showBanner = true }
                    }
                )
            }

            Spacer(modifier = Modifier.height(Spacing.dp32))
        }

        UnavailableBanner(
            visible = showBanner,
            onDismiss = { showBanner = false },
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .navigationBarsPadding()
                .padding(bottom = Spacing.dp32)
        )
    }
}

@Composable
private fun DataProtectionItem(
    boldPrefix: String,
    text: String,
    linkText: String,
    onLinkClick: () -> Unit
) {
    val colors = IberdrolaTheme.colors
    val annotated = buildAnnotatedString {
        withStyle(SpanStyle(fontWeight = FontWeight.Medium, color = colors.darkGreyText)) {
            append(boldPrefix)
        }
        withStyle(SpanStyle(color = colors.darkGreyText)) {
            append(text)
            append(" ")
        }
        pushStringAnnotation(tag = "LINK", annotation = "info")
        withStyle(
            SpanStyle(
                color = colors.iberdrolaDarkGreen,
                fontWeight = FontWeight.ExtraBold,
                textDecoration = TextDecoration.Underline
            )
        ) {
            append(linkText)
        }
        pop()
    }

    ClickableText(
        text = annotated,
        style = TextStyle(
            fontFamily = IberFontRegular,
            fontSize = TextSize.sp15,
            lineHeight = TextSize.sp22
        ),
        onClick = { offset ->
            annotated.getStringAnnotations("LINK", offset, offset)
                .firstOrNull()?.let { onLinkClick() }
        }
    )
}

@Preview(name = "Activate Content - Light", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_NO)
@Preview(name = "Activate Content - Dark", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun ActivateElectronicInvoiceContentPreview() {
    IberdrolaTheme {
        ActivateElectronicInvoiceContent(
            email = "",
            legalAccepted = false,
            isEmailValid = true,
            onEmailChanged = {},
            onLegalAcceptedChanged = {}
        )
    }
}
