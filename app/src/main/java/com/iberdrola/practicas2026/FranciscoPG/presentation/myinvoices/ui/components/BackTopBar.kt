package com.iberdrola.practicas2026.FranciscoPG.presentation.myinvoices.ui.components

import android.content.res.Configuration
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.iberdrola.practicas2026.FranciscoPG.R
import com.iberdrola.practicas2026.FranciscoPG.presentation.theme.IberdrolaTheme
import com.iberdrola.practicas2026.FranciscoPG.presentation.theme.Spacing

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
                .padding(horizontal = Spacing.dp20)
        ) {
            BackButton(
                text = stringResource(R.string.filter_back),
                onClick = onBack,
                modifier = Modifier.padding(top = Spacing.dp18)
            )
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
