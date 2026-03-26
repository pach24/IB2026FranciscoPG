package com.iberdrola.practicas2026.FranciscoPG.presentation.electronicinvoice.ui

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.painterResource
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
import com.iberdrola.practicas2026.FranciscoPG.presentation.theme.IberFontBold
import com.iberdrola.practicas2026.FranciscoPG.presentation.theme.IberFontRegular
import com.iberdrola.practicas2026.FranciscoPG.presentation.theme.IberdrolaTheme
import com.iberdrola.practicas2026.FranciscoPG.presentation.theme.IconSize
import com.iberdrola.practicas2026.FranciscoPG.presentation.theme.Spacing
import com.iberdrola.practicas2026.FranciscoPG.presentation.theme.Stroke
import com.iberdrola.practicas2026.FranciscoPG.presentation.theme.TextSize

@Composable
fun ActivateElectronicInvoiceScreen(
    onNavigateBack: () -> Unit
) {
    val colors = IberdrolaTheme.colors
    var email by remember { mutableStateOf("") }
    var legalAccepted by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colors.background)
            .padding(top = Spacing.dp24)
    ) {
        // X cerrar alineada a la derecha
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = Spacing.dp24, vertical = Spacing.dp18),
            contentAlignment = Alignment.CenterEnd
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_close),
                contentDescription = null,
                tint = colors.iberdrolaGreen, // Ajusta si el diseño usa otro color para el icono
                modifier = Modifier
                    .size(IconSize.dp28)
                    .clickable(onClick = onNavigateBack)
            )
        }

        // Contenido scrollable
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
        ) {

            // Título
            Text(
                text = stringResource(R.string.activate_einvoice_title),
                color = colors.textPrimary,
                fontFamily = IberFontBold,
                fontWeight = FontWeight.Bold,
                fontSize = TextSize.sp23,
                modifier = Modifier.padding(horizontal = Spacing.dp24)
            )

            Spacer(modifier = Modifier.height(Spacing.dp12))

            // Barra de progreso (Mitad verde, mitad gris/claro)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = Spacing.dp24)
            ) {
                HorizontalDivider(
                    modifier = Modifier.weight(0.5f),
                    color = colors.iberdrolaGreen,
                    thickness = 4.dp
                )
                HorizontalDivider(
                    modifier = Modifier.weight(0.5f),
                    color = colors.divider, // o colors.lightGrey dependiendo de tu paleta
                    thickness = 4.dp
                )
            }

            Spacer(modifier = Modifier.height(Spacing.dp24))

            // Info de Email actual (en la maqueta está en dos líneas, la segunda en negrita)
            Column(modifier = Modifier.padding(horizontal = Spacing.dp24)) {
                Text(
                    text = stringResource(R.string.activate_einvoice_linked_email), // "Email vinculado a tu cuenta:"
                    color = colors.darkGreyText,
                    fontFamily = IberFontRegular,
                    fontSize = TextSize.sp14
                )
                Text(
                    text = "a*****a@a.com",
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
                    onValueChange = { email = it },
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
                                    .background(colors.lightGrey.copy(alpha = 0.5f))
                            )
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
                    linkText = stringResource(R.string.activate_einvoice_more_info)
                )
                Spacer(modifier = Modifier.height(Spacing.dp8))
                DataProtectionItem(
                    boldPrefix = "Finalidad:",
                    text = " Gestión de la factura electrónica.",
                    linkText = stringResource(R.string.activate_einvoice_more_info)
                )
                Spacer(modifier = Modifier.height(Spacing.dp8))
                DataProtectionItem(
                    boldPrefix = "Derechos:",
                    text = " Acceso, rectificación, supresión, limitación del tratamiento, portabilidad de datos u oposición, incluida la oposición a decisiones individuales automatizadas.",
                    linkText = stringResource(R.string.activate_einvoice_more_info)
                )
            }

            Spacer(modifier = Modifier.height(Spacing.dp24))

            // Checkbox legal
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { legalAccepted = !legalAccepted }
                    .padding(horizontal = Spacing.dp24),
                verticalAlignment = Alignment.Top
            ) {
                RoundedCheckbox(
                    checked = legalAccepted,
                    checkedColor = colors.iberdrolaGreen,
                    uncheckedBorderColor = colors.iberdrolaGreen,
                    checkmarkColor = colors.white,
                    modifier = Modifier.padding(top = 2.dp)
                )
                Spacer(modifier = Modifier.width(Spacing.dp12))
                Text(
                    text = buildAnnotatedString {
                        append("He leído y acepto la ")
                        withStyle(SpanStyle(
                        )) {
                            append("Política de privacidad")
                        }
                        append(", acepto las ")
                        withStyle(SpanStyle(
                            color = colors.iberdrolaDarkGreen,
                            textDecoration = TextDecoration.Underline,
                            fontWeight = FontWeight.Bold,
                            fontSize = TextSize.sp17
                        )) {
                            append("Condiciones Generales")
                        }
                        append(" y Particulares de la oferta y la suscripción a Factura Electrónica.")
                    },
                    color = colors.darkGreyText,
                    fontFamily = IberFontRegular,
                    fontSize = TextSize.sp16,
                    lineHeight = TextSize.sp22
                )
            }

            Spacer(modifier = Modifier.height(Spacing.dp32))
        }

        // Separador superior de botones
        HorizontalDivider(color = colors.divider, thickness = 1.dp)

        // Botones fijos abajo
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(colors.background)
                .navigationBarsPadding()
                .padding(horizontal = Spacing.dp24, vertical = Spacing.dp16),
            horizontalArrangement = Arrangement.spacedBy(Spacing.dp12)
        ) {
            // Botón Anterior
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(Spacing.dp48)
                    .clip(CircleShape) // Botón tipo píldora
                    .border(
                        width = Stroke.dp1,
                        color = colors.iberdrolaDarkGreen,
                        shape = CircleShape
                    )
                    .clickable(onClick = onNavigateBack),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = stringResource(R.string.activate_einvoice_btn_back),
                    color = colors.iberdrolaDarkGreen,
                    fontFamily = IberFontBold,
                    fontWeight = FontWeight.Bold,
                    fontSize = TextSize.sp14
                )
            }

            // Botón Siguiente
            val isEnabled = email.isNotBlank() && legalAccepted
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(Spacing.dp48)
                    .clip(CircleShape) // Botón tipo píldora
                    .background(
                        if (isEnabled) colors.iberdrolaDarkGreen
                        else colors.divider // Ajusta este color al tono gris claro/verdoso de tu paleta
                    )
                    .then(
                        if (isEnabled) Modifier.clickable { /* Acción siguiente */ }
                        else Modifier
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = stringResource(R.string.activate_einvoice_btn_next),
                    color = if (isEnabled) colors.surface else colors.lightGrey,
                    fontFamily = IberFontBold,
                    fontWeight = FontWeight.Bold,
                    fontSize = TextSize.sp14
                )
            }
        }
    }
}

@Composable
private fun DataProtectionItem(
    boldPrefix: String,
    text: String,
    linkText: String
) {
    val colors = IberdrolaTheme.colors
    Text(
        text = buildAnnotatedString {
            withStyle(SpanStyle(fontWeight = FontWeight.Medium, color = colors.darkGreyText)) {
                append(boldPrefix)
            }
            withStyle(SpanStyle(color = colors.darkGreyText)) {
                append(text)
                append(" ")
            }
            withStyle(
                SpanStyle(
                    color = colors.iberdrolaDarkGreen,
                    fontWeight = FontWeight.ExtraBold,
                    textDecoration = TextDecoration.Underline
                )
            ) {
                append(linkText)
            }
        },
        fontFamily = IberFontRegular,
        fontSize = TextSize.sp15,
        lineHeight = TextSize.sp22
    )
}

@Preview(name = "Activate - Light", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_NO)
@Preview(name = "Activate - Dark", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun ActivateElectronicInvoiceScreenPreview() {
    IberdrolaTheme {
        ActivateElectronicInvoiceScreen(onNavigateBack = {})
    }
}