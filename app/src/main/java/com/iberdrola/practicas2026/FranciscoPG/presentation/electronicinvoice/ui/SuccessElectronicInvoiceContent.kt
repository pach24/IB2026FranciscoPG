package com.iberdrola.practicas2026.FranciscoPG.presentation.electronicinvoice.ui

import android.app.Activity
import android.content.res.Configuration
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.EaseOutBack
import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import com.iberdrola.practicas2026.FranciscoPG.R
import com.iberdrola.practicas2026.FranciscoPG.presentation.theme.IberFontBold
import com.iberdrola.practicas2026.FranciscoPG.presentation.theme.IberFontRegular
import com.iberdrola.practicas2026.FranciscoPG.presentation.theme.IberGreen
import com.iberdrola.practicas2026.FranciscoPG.presentation.theme.IberdrolaTheme
import com.iberdrola.practicas2026.FranciscoPG.presentation.theme.IconSize
import com.iberdrola.practicas2026.FranciscoPG.presentation.theme.Radius
import com.iberdrola.practicas2026.FranciscoPG.presentation.theme.Spacing
import com.iberdrola.practicas2026.FranciscoPG.presentation.theme.Stroke
import com.iberdrola.practicas2026.FranciscoPG.presentation.theme.TextSize
import kotlinx.coroutines.delay

// ── Posición manual de los destellos  ──────────────────────────
private val SPARKLES_OFFSET_X = 70.dp   // positivo → derecha, negativo → izquierda
private val SPARKLES_OFFSET_Y = (-80).dp // positivo → abajo,   negativo → arriba
// ─────────────────────────────────────────────────────────────────────────────

@Composable
fun SuccessElectronicInvoiceContent(
    email: String,
    onAccept: () -> Unit,
    onClose: () -> Unit,
    modifier: Modifier = Modifier,
    title: String = stringResource(R.string.success_einvoice_title),
    // TODO: eliminar antes de merge
    onDebugBack: (() -> Unit)? = null
) {
    // Pintar system bars de verde y restaurar al salir
    val view = LocalView.current
    if (!view.isInEditMode) {
        DisposableEffect(Unit) {
            val window = (view.context as Activity).window
            val prevStatusBar = window.statusBarColor
            val prevNavBar = window.navigationBarColor
            val insetsController = WindowCompat.getInsetsController(window, view)
            val prevLightStatus = insetsController.isAppearanceLightStatusBars
            val prevLightNav = insetsController.isAppearanceLightNavigationBars

            window.statusBarColor = IberGreen.toArgb()
            window.navigationBarColor = IberGreen.toArgb()
            insetsController.isAppearanceLightStatusBars = false
            insetsController.isAppearanceLightNavigationBars = false

            onDispose {
                window.statusBarColor = prevStatusBar
                window.navigationBarColor = prevNavBar
                insetsController.isAppearanceLightStatusBars = prevLightStatus
                insetsController.isAppearanceLightNavigationBars = prevLightNav
            }
        }
    }

    // Overshoot más exagerado: llega al ~25% por encima de 1f antes de asentarse
    val strongEaseOutBack = CubicBezierEasing(0.34f, 1.82f, 0.64f, 1f)

    val imageScale = remember { Animatable(0.15f) }
    LaunchedEffect(Unit) {
        imageScale.animateTo(
            targetValue = 1f,
            animationSpec = tween(durationMillis = 550, easing = strongEaseOutBack)
        )
    }

    val sparkleScale = remember { Animatable(0.15f) }
    LaunchedEffect(Unit) {
        delay(150L)
        sparkleScale.animateTo(
            targetValue = 1f,
            animationSpec = tween(durationMillis = 500, easing = strongEaseOutBack)
        )
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(IberGreen)
            .statusBarsPadding()
            .navigationBarsPadding()
    ) {
        // Botón cerrar — misma posición y tamaño que CloseTopBar
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = Spacing.dp24, vertical = Spacing.dp18),
            contentAlignment = Alignment.CenterEnd
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_close),
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier
                    .size(IconSize.dp28)
                    .clickable { onClose() }
            )
        }

        // Contenido central
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = Spacing.dp28),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.weight(1f))

            // Thumb up con destellos posicionados libremente
            Box(contentAlignment = Alignment.Center) {
                Image(
                    painter = painterResource(R.drawable.ic_thumb),
                    contentDescription = null,
                    modifier = Modifier
                        .size(280.dp)
                        .graphicsLayer {
                            scaleX = imageScale.value
                            scaleY = imageScale.value
                        },
                    contentScale = ContentScale.Fit
                )
                Image(
                    painter = painterResource(R.drawable.ic_sparkles),
                    contentDescription = null,
                    modifier = Modifier
                        .size(80.dp)
                        .offset(x = SPARKLES_OFFSET_X, y = SPARKLES_OFFSET_Y)
                        .graphicsLayer {
                            scaleX = sparkleScale.value
                            scaleY = sparkleScale.value
                        },
                    contentScale = ContentScale.Fit
                )
            }

            Spacer(modifier = Modifier.height(Spacing.dp40))

            Text(
                text = title,
                color = Color.White,
                fontFamily = IberFontBold,
                fontWeight = FontWeight.Bold,
                fontSize = TextSize.sp18,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(Spacing.dp16))

            Text(
                text = stringResource(R.string.success_einvoice_subtitle, email),
                color = Color.White.copy(alpha = 0.85f),
                fontFamily = IberFontRegular,
                fontSize = TextSize.sp13,
                textAlign = TextAlign.Center,
                lineHeight = TextSize.sp22
            )

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = onAccept,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(Radius.dp50),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.White,
                    contentColor = IberGreen
                )
            ) {
                Text(
                    text = stringResource(R.string.success_einvoice_accept),
                    fontFamily = IberFontBold,
                    fontWeight = FontWeight.Bold,
                    fontSize = TextSize.sp16
                )
            }

            Spacer(modifier = Modifier.height(Spacing.dp32))

        }
    }
}

@Preview(name = "Success - Light", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_NO)
@Preview(name = "Success - Dark", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun SuccessElectronicInvoiceContentPreview() {
    IberdrolaTheme {
        SuccessElectronicInvoiceContent(
            email = "b*******5@imfaya.com",
            onAccept = {},
            onClose = {}
        )
    }
}
