package com.iberdrola.practicas2026.FranciscoPG.presentation.common

import android.content.res.Configuration
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.iberdrola.practicas2026.FranciscoPG.R
import com.iberdrola.practicas2026.FranciscoPG.presentation.theme.IberdrolaTheme
import com.iberdrola.practicas2026.FranciscoPG.presentation.theme.IconSize
import com.iberdrola.practicas2026.FranciscoPG.presentation.theme.Spacing

@Composable
fun CloseTopBar(
    onClose: () -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = IberdrolaTheme.colors

    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = Spacing.dp24, vertical = Spacing.dp18),
        contentAlignment = Alignment.CenterEnd
    ) {
        Icon(
            painter = painterResource(R.drawable.ic_close),
            contentDescription = null,
            tint = colors.iberdrolaGreen,
            modifier = Modifier
                .size(IconSize.dp28)
                .clickable { onClose() }
        )
    }
}

@Preview(name = "CloseTopBar - Light", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_NO)
@Preview(name = "CloseTopBar - Dark", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun CloseTopBarPreview() {
    IberdrolaTheme {
        CloseTopBar(onClose = {})
    }
}
