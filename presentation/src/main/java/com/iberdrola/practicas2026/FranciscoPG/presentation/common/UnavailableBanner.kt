package com.iberdrola.practicas2026.FranciscoPG.presentation.common
import com.iberdrola.practicas2026.FranciscoPG.presentation.R

import android.content.res.Configuration
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.iberdrola.practicas2026.FranciscoPG.presentation.theme.IberdrolaTheme

@Composable
fun UnavailableBanner(
    visible: Boolean,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
    durationMillis: Long = 4000L
) {
    GenericBanner(
        visible = visible,
        text = stringResource(R.string.banner_service_unavailable),
        onDismiss = onDismiss,
        modifier = modifier,
        durationMillis = durationMillis
    )
}

@Preview(name = "Banner - Light", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_NO)
@Preview(name = "Banner - Dark", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun UnavailableBannerPreview() {
    IberdrolaTheme {
        UnavailableBanner(visible = true, onDismiss = {})
    }
}
