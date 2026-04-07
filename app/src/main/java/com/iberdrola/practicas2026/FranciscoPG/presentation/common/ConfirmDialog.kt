package com.iberdrola.practicas2026.FranciscoPG.presentation.common

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
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

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(Stroke.dp1)
                    .background(colors.divider)
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(IntrinsicSize.Min)
            ) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clickable { onDismiss() }
                        .padding(vertical = Spacing.dp16),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = dismissText,
                        fontFamily = IberFontRegular,
                        fontSize = TextSize.sp15,
                        color = colors.darkGreyText
                    )
                }

                Box(
                    modifier = Modifier
                        .width(Stroke.dp1)
                        .fillMaxHeight()
                        .background(colors.divider)
                )

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clickable { onConfirm() }
                        .padding(vertical = Spacing.dp16),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = confirmText,
                        fontFamily = IberFontBold,
                        fontWeight = FontWeight.Bold,
                        fontSize = TextSize.sp15,
                        color = colors.errorTextForm
                    )
                }
            }
        }
    }
}

@Preview(name = "ConfirmDialog - Light", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_NO)
@Preview(name = "ConfirmDialog - Dark", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
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
