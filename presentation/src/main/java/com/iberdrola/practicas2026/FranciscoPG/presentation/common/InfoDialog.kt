package com.iberdrola.practicas2026.FranciscoPG.presentation.common

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.window.Dialog
import com.iberdrola.practicas2026.FranciscoPG.presentation.theme.IberFontBold
import com.iberdrola.practicas2026.FranciscoPG.presentation.theme.IberFontRegular
import com.iberdrola.practicas2026.FranciscoPG.presentation.theme.IberdrolaTheme
import com.iberdrola.practicas2026.FranciscoPG.presentation.theme.Radius
import com.iberdrola.practicas2026.FranciscoPG.presentation.theme.Spacing
import com.iberdrola.practicas2026.FranciscoPG.presentation.theme.TextSize

@Composable
fun InfoDialog(
    title: String,
    message: String,
    linkText: String,
    closeText: String,
    onLinkClick: () -> Unit,
    onDismiss: () -> Unit
) {
    val colors = IberdrolaTheme.colors

    Dialog(onDismissRequest = onDismiss) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(Radius.dp8))
                .background(colors.surface)
                .padding(Spacing.dp24)
        ) {
            Text(
                text = title,
                fontFamily = IberFontBold,
                fontWeight = FontWeight.Bold,
                fontSize = TextSize.sp17,
                color = colors.textPrimary
            )

            Spacer(modifier = Modifier.height(Spacing.dp14))

            Text(
                text = message,
                fontFamily = IberFontRegular,
                fontSize = TextSize.sp14,
                lineHeight = TextSize.sp22,
                color = colors.darkGreyText
            )

            Spacer(modifier = Modifier.height(Spacing.dp16))

            Text(
                text = linkText,
                fontFamily = IberFontRegular,
                fontSize = TextSize.sp14,
                color = colors.iberdrolaGreen,
                textDecoration = TextDecoration.Underline,
                modifier = Modifier.clickable { onLinkClick() }
            )

            Spacer(modifier = Modifier.height(Spacing.dp24))

            androidx.compose.foundation.layout.Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(Spacing.dp48)
                    .clip(CircleShape)
                    .background(colors.buttonActive)
                    .clickable { onDismiss() },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = closeText,
                    fontFamily = IberFontBold,
                    fontWeight = FontWeight.Bold,
                    fontSize = TextSize.sp14,
                    color = colors.buttonTextActive
                )
            }
        }
    }
}

@Preview(name = "InfoDialog - Light", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_NO)
@Preview(name = "InfoDialog - Dark", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun InfoDialogPreview() {
    IberdrolaTheme {
        InfoDialog(
            title = "Información sobre la factura electrónica",
            message = "La factura electrónica es un requisito obligatorio de tu plan contratado.",
            linkText = "Más información sobre tu plan",
            closeText = "Cerrar",
            onLinkClick = {},
            onDismiss = {}
        )
    }
}
