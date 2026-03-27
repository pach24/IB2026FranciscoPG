package com.iberdrola.practicas2026.FranciscoPG.presentation.common

import android.content.res.Configuration
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import com.iberdrola.practicas2026.FranciscoPG.R
import com.iberdrola.practicas2026.FranciscoPG.presentation.theme.Component.compHeigh60
import com.iberdrola.practicas2026.FranciscoPG.presentation.theme.IberdrolaTheme
import com.iberdrola.practicas2026.FranciscoPG.presentation.theme.Spacing
import com.iberdrola.practicas2026.FranciscoPG.presentation.theme.TextSize
import kotlinx.coroutines.delay

@Composable
fun SuccessBannerSMS(
    visible: Boolean,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
    durationMillis: Long = 4000L
) {
    LaunchedEffect(visible) {
        if (visible) {
            delay(durationMillis)
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
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = compHeigh60)
                .background(color = IberdrolaTheme.colors.successBannerBackground)
                .padding(all = Spacing.dp12)
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_check_circle),
                contentDescription = null,
                tint = IberdrolaTheme.colors.iberdrolaDarkGreen,
                modifier = Modifier
                    .size(Spacing.dp20)
                    .align(Alignment.CenterVertically)

            )

            Text(
                text = stringResource(R.string.banner_resend_sms_success),
                fontSize = TextSize.sp14,
                fontWeight = FontWeight.Medium,
                color = IberdrolaTheme.colors.black,
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = Spacing.dp12)
                    .align(Alignment.CenterVertically)
            )

            Icon(
                painter = painterResource(R.drawable.ic_close),
                contentDescription = "Close",
                tint = IberdrolaTheme.colors.black,
                modifier = Modifier
                    .size(Spacing.dp18)
                    .clickable { onDismiss() }
                    .align(Alignment.CenterVertically)
            )
        }
    }
}

@Preview(name = "SuccessBanner - Light", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_NO)
@Preview(name = "SuccessBanner - Dark", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun SuccessBannerSMSPreview() {
    IberdrolaTheme {
        SuccessBannerSMS(visible = true, onDismiss = {})
    }
}
