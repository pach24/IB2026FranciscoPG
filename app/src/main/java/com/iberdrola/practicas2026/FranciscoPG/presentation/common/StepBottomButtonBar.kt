package com.iberdrola.practicas2026.FranciscoPG.presentation.common

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.iberdrola.practicas2026.FranciscoPG.R
import com.iberdrola.practicas2026.FranciscoPG.presentation.theme.IberFontBold
import com.iberdrola.practicas2026.FranciscoPG.presentation.theme.IberdrolaTheme
import com.iberdrola.practicas2026.FranciscoPG.presentation.theme.Spacing
import com.iberdrola.practicas2026.FranciscoPG.presentation.theme.Stroke
import com.iberdrola.practicas2026.FranciscoPG.presentation.theme.TextSize

@Composable
fun StepBottomButtonBar(
    onBack: () -> Unit,
    onNext: () -> Unit,
    isNextEnabled: Boolean,
    modifier: Modifier = Modifier
) {
    val colors = IberdrolaTheme.colors

    Column(modifier = modifier) {
        HorizontalDivider(color = colors.divider, thickness = 1.dp)

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
                    .clip(CircleShape)
                    .border(
                        width = Stroke.dp2,
                        color = colors.iberdrolaDarkGreen,
                        shape = CircleShape
                    )
                    .clickable { onBack() },
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
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(Spacing.dp48)
                    .clip(CircleShape)
                    .background(
                        if (isNextEnabled) colors.buttonActive
                        else colors.buttonDisabled
                    )
                    .then(
                        if (isNextEnabled) Modifier.clickable { onNext() }
                        else Modifier
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = stringResource(R.string.activate_einvoice_btn_next),
                    color = if (isNextEnabled) colors.buttonTextActive else colors.buttonTextDisabled,
                    fontFamily = IberFontBold,
                    fontWeight = FontWeight.Bold,
                    fontSize = TextSize.sp14
                )
            }
        }
    }
}

@Preview(name = "StepBottomButtonBar - Enabled", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_NO)
@Preview(name = "StepBottomButtonBar - Dark", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun StepBottomButtonBarPreview() {
    IberdrolaTheme {
        StepBottomButtonBar(
            onBack = {},
            onNext = {},
            isNextEnabled = true
        )
    }
}
