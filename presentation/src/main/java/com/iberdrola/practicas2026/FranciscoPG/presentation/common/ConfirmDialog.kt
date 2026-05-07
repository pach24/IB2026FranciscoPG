package com.iberdrola.practicas2026.FranciscoPG.presentation.common

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.window.Dialog
import com.iberdrola.practicas2026.FranciscoPG.presentation.theme.IberFontBold
import com.iberdrola.practicas2026.FranciscoPG.presentation.theme.IberFontRegular
import com.iberdrola.practicas2026.FranciscoPG.presentation.theme.IberdrolaTheme
import com.iberdrola.practicas2026.FranciscoPG.presentation.theme.Radius
import com.iberdrola.practicas2026.FranciscoPG.presentation.theme.Spacing
import com.iberdrola.practicas2026.FranciscoPG.presentation.theme.Stroke
import com.iberdrola.practicas2026.FranciscoPG.presentation.theme.TextSize

@Composable
fun ConfirmDialog(
    title: String,
    message: String,
    confirmText: String,
    dismissText: String,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    val colors = IberdrolaTheme.colors

    Dialog(onDismissRequest = onDismiss) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(Radius.dp8))
                .background(colors.surface)
        ) {
            Column(modifier = Modifier.padding(Spacing.dp24)) {
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
            }

            Spacer(modifier = Modifier.height(Spacing.dp8))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = Spacing.dp24)
                    .padding(bottom = Spacing.dp24),
                horizontalArrangement = Arrangement.spacedBy(Spacing.dp12)
            ) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(Spacing.dp48)
                        .clip(CircleShape)
                        .border(Stroke.dp2, colors.iberdrolaDarkGreen, CircleShape)
                        .clickable { onDismiss() },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = dismissText,
                        fontFamily = IberFontBold,
                        fontWeight = FontWeight.Bold,
                        fontSize = TextSize.sp14,
                        color = colors.iberdrolaDarkGreen
                    )
                }

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(Spacing.dp48)
                        .clip(CircleShape)
                        .background(colors.buttonActive)
                        .clickable { onConfirm() },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = confirmText,
                        fontFamily = IberFontBold,
                        fontWeight = FontWeight.Bold,
                        fontSize = TextSize.sp14,
                        color = colors.buttonTextActive
                    )
                }
            }
        }
    }
}

@Preview(name = "ConfirmDialog - Light", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_NO)
@Preview(name = "ConfirmDialog - Dark", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun ConfirmDialogPreview() {
    IberdrolaTheme {
        ConfirmDialog(
            title = "¿Salir del proceso?",
            message = "Si sales ahora, perderás los datos introducidos.",
            confirmText = "Salir",
            dismissText = "Cancelar",
            onConfirm = {},
            onDismiss = {}
        )
    }
}
