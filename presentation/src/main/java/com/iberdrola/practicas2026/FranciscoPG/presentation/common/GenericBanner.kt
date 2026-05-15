package com.iberdrola.practicas2026.FranciscoPG.presentation.common
import com.iberdrola.practicas2026.FranciscoPG.presentation.R

import android.content.res.Configuration
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import com.iberdrola.practicas2026.FranciscoPG.presentation.theme.Component.compHeigh80
import com.iberdrola.practicas2026.FranciscoPG.presentation.theme.IberdrolaTheme
import com.iberdrola.practicas2026.FranciscoPG.presentation.theme.Radius
import com.iberdrola.practicas2026.FranciscoPG.presentation.theme.Spacing
import com.iberdrola.practicas2026.FranciscoPG.presentation.theme.TextSize
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun GenericBanner(
    visible: Boolean,
    text: String,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
    durationMillis: Long = 4000L
) {
    val progress = remember { Animatable(0f) }
    val scale = remember { Animatable(0f) }

    LaunchedEffect(visible) {
        if (visible) {
            launch {
                progress.animateTo(
                    targetValue = 1f,
                    animationSpec = spring(
                        dampingRatio = Spring.DampingRatioLowBouncy,
                        stiffness = Spring.StiffnessMediumLow
                    )
                )
            }
            launch {
                scale.animateTo(
                    targetValue = 1f,
                    animationSpec = spring(
                        dampingRatio = Spring.DampingRatioMediumBouncy,
                        stiffness = Spring.StiffnessMedium
                    )
                )
            }
            delay(durationMillis)
            onDismiss()
        } else {
            launch {
                progress.animateTo(
                    targetValue = 0f,
                    animationSpec = tween(durationMillis = 250)
                )
            }
            launch {
                scale.animateTo(
                    targetValue = 0f,
                    animationSpec = tween(durationMillis = 200)
                )
            }
        }
    }

    if (progress.value > 0f || scale.value > 0f) {
        val shape = RoundedCornerShape(Radius.dp16)

        Row(
            modifier = modifier
                .padding(horizontal = Spacing.dp12)
                .graphicsLayer {
                    translationY = (1f - progress.value) * 200f
                    alpha = progress.value.coerceIn(0f, 1f)
                    scaleX = scale.value.coerceIn(0f, 1.2f)
                    scaleY = scale.value.coerceIn(0f, 1.2f)
                }
                .shadow(
                    elevation = Spacing.dp12,
                    shape = shape,
                    ambientColor = IberdrolaTheme.colors.snackbarIcon.copy(alpha = 0.3f),
                    spotColor = IberdrolaTheme.colors.snackbarIcon.copy(alpha = 0.3f)
                )
                .clip(shape)
                .fillMaxWidth()
                .heightIn(min = compHeigh80)
                .background(color = IberdrolaTheme.colors.snackbar)
                .padding(all = Spacing.dp16)
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_warning),
                contentDescription = null,
                tint = IberdrolaTheme.colors.snackbarIcon,
                modifier = Modifier
                    .size(Spacing.dp24)
                    .align(Alignment.Top)
                    .padding(top = Spacing.dp2)
            )

            Text(
                text = text,
                fontSize = TextSize.sp18,
                fontWeight = FontWeight.Medium,
                color = IberdrolaTheme.colors.black,
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = Spacing.dp16)
                    .align(Alignment.CenterVertically)
            )

            Icon(
                painter = painterResource(R.drawable.ic_close),
                contentDescription = "Close",
                tint = IberdrolaTheme.colors.black,
                modifier = Modifier
                    .align(Alignment.Top)
                    .clickable(
                        interactionSource = null,
                        indication = null
                    ) { onDismiss() }
                    .padding(Spacing.dp12)
                    .size(Spacing.dp28)
            )
        }
    }
}

@Preview(name = "GenericBanner - Light", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_NO)
@Preview(name = "GenericBanner - Dark", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun GenericBannerPreview() {
    IberdrolaTheme {
        GenericBanner(
            visible = true,
            text = "Esta factura aún no está disponible",
            onDismiss = {}
        )
    }
}
