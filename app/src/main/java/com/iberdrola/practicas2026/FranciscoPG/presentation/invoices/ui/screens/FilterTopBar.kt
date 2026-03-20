package com.iberdrola.practicas2026.FranciscoPG.presentation.invoices.ui.screens

import android.content.res.Configuration
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import com.iberdrola.practicas2026.FranciscoPG.presentation.theme.IberFontBold
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import com.iberdrola.practicas2026.FranciscoPG.R
import com.iberdrola.practicas2026.FranciscoPG.presentation.theme.IberdrolaTheme
import com.iberdrola.practicas2026.FranciscoPG.presentation.theme.Spacing
import com.iberdrola.practicas2026.FranciscoPG.presentation.theme.TextSize

@Composable
fun FilterTopBar(onBack: () -> Unit) {
    val colors = IberdrolaTheme.colors
    Surface(
        color = colors.background,
        shadowElevation = Spacing.dp0
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = Spacing.dp16)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .padding(top = Spacing.dp24)
                    .clickable { onBack() }
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_arrow_back),
                    contentDescription = null,
                    tint = colors.iberdrolaDarkGreen,
                    modifier = Modifier.size(Spacing.dp24)
                )
                Text(
                    text = stringResource(R.string.filter_back),
                    color = colors.iberdrolaDarkGreen,
                    fontWeight = FontWeight.Bold,
                    fontFamily = IberFontBold,
                    textDecoration = TextDecoration.Underline,
                    fontSize = TextSize.sp16
                )
            }
        }
    }
}

@Preview(name = "TopBar - Light", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_NO)
@Preview(name = "TopBar - Dark", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun FilterTopBarPreview() {
    IberdrolaTheme {
        FilterTopBar(onBack = {})
    }
}
