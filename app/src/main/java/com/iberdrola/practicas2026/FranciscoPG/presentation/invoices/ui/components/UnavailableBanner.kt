package com.iberdrola.practicas2026.FranciscoPG.presentation.invoices.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import android.content.res.Configuration
import androidx.compose.foundation.layout.heightIn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import com.iberdrola.practicas2026.FranciscoPG.R
import com.iberdrola.practicas2026.FranciscoPG.presentation.theme.Component.compHeigh100
import com.iberdrola.practicas2026.FranciscoPG.presentation.theme.IberdrolaTheme
import com.iberdrola.practicas2026.FranciscoPG.presentation.theme.Spacing
import com.iberdrola.practicas2026.FranciscoPG.presentation.theme.TextSize
@Composable
fun UnavailableBanner(
    visible: Boolean,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
    durationMillis: Long = 4000L
) {
    LaunchedEffect(visible) {
        if (visible) {
            kotlinx.coroutines.delay(durationMillis)
            onDismiss()
        }
    }

    AnimatedVisibility(
        visible = visible,
        enter = slideInVertically { it },
        exit = slideOutVertically { it },
        modifier = modifier
    ) {
        Row(
            // Quitamos el verticalAlignment global para controlar cada hijo
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = compHeigh100)
                .background(color = IberdrolaTheme.colors.snackbar)
                .padding(all = Spacing.dp16)
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_warning),
                contentDescription = null,
                tint = IberdrolaTheme.colors.snackbarIcon,
                modifier = Modifier
                    .size(Spacing.dp24)
                    .align(Alignment.Top) //
                    .padding(top = Spacing.dp2)
            )

            Text(
                text = stringResource(R.string.banner_service_unavailable),
                fontSize = TextSize.sp18,
                fontWeight = FontWeight.Medium,
                color = IberdrolaTheme.colors.black,
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = Spacing.dp16)
                    .align(Alignment.CenterVertically) // Mantenemos el texto centrado
            )

            Icon(
                painter = painterResource(R.drawable.ic_close),
                contentDescription = "Close",
                tint = IberdrolaTheme.colors.black,
                modifier = Modifier
                    .size(Spacing.dp28)
                    .clickable { onDismiss() }
                    .align(Alignment.Top) // El icono de cerrar también suele ir arriba en banners altos
            )
        }
    }
}

@Preview(name = "Banner - Light", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_NO)
@Preview(name = "Banner - Dark", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun UnavailableBannerPreview() {
    IberdrolaTheme {
        UnavailableBanner(visible = true, onDismiss = {})
    }
}
