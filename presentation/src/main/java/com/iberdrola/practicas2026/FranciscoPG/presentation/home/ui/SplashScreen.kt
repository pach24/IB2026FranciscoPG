package com.iberdrola.practicas2026.FranciscoPG.presentation.home.ui

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.clipRect
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.iberdrola.practicas2026.FranciscoPG.presentation.R
import com.iberdrola.practicas2026.FranciscoPG.presentation.theme.IberFontBold
import com.iberdrola.practicas2026.FranciscoPG.presentation.theme.IberFontRegular
import com.iberdrola.practicas2026.FranciscoPG.presentation.theme.IberGreen
import com.iberdrola.practicas2026.FranciscoPG.presentation.theme.Spacing
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun SplashScreen(onSplashFinished: () -> Unit) {

    val progress = remember { Animatable(0f) }
    val logoScale = remember { Animatable(0f) }
    val logoAlpha = remember { Animatable(1f) }
    val spreadOffset = remember { Animatable(0f) }
    val textAlpha = remember { Animatable(0f) }

    LaunchedEffect(Unit) {

        // 1. Logo aparece centrado
        logoScale.animateTo(1f, animationSpec = tween(700, easing = FastOutSlowInEasing))
        // Pausa con el logo visible
        delay(500L)

        // 2. Logo se desliza a la izquierda, texto surge desde detrás hacia la derecha
        launch { delay(100L)
                 textAlpha.animateTo(1f, animationSpec = tween(300, easing = FastOutSlowInEasing)) }
        spreadOffset.animateTo(1f, animationSpec = tween(600, easing = FastOutSlowInEasing))
        delay(500L)

        // 3. Todo se desvanece y el verde sube
        launch { logoAlpha.animateTo(0f, animationSpec = tween(400, easing = FastOutSlowInEasing)) }
        launch { textAlpha.animateTo(0f, animationSpec = tween(400, easing = FastOutSlowInEasing)) }
        progress.animateTo(1f, animationSpec = tween(900, easing = FastOutSlowInEasing))
        delay(200L)
        onSplashFinished()
    }

    // Medir la altura exacta del header usando los componentes REALES de Home
    var headerHeightPx by remember { mutableIntStateOf(0) }
    val density = LocalDensity.current

    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.Center
    ) {
        // Medidor invisible: mismos componentes que MainScreen para altura pixel-perfect
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.TopStart)
                .onGloballyPositioned { headerHeightPx = it.size.height }
                .graphicsLayer { alpha = 0f }
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
            ) {
                HeaderContent(userName = "")
                PromoCard()
                Spacer(modifier = Modifier.height(Spacing.dp48))
            }
        }

        // Verde con la curva exacta de bg_header_curved (viewport SVG 400×380)
        val progressVal = progress.value

        Canvas(modifier = Modifier.fillMaxSize()) {
            val w = size.width
            val h = size.height
            val hdr = headerHeightPx.toFloat()

            if (hdr <= 0f) {
                drawRect(IberGreen)
                return@Canvas
            }

            // Offset: progress 0 → curva al fondo; progress 1 → curva en posición del header
            val extraMargin = with(density) { 40.dp.toPx() }
            val offset = (1f - progressVal) * (h - hdr + extraMargin)

            // Proporciones del SVG bg_header_curved (viewport 400×380)
            val cornerRx = w * 30f / 400f
            val flatY = hdr * 350f / 380f + offset
            val rightY = hdr + offset
            val leftY = hdr * 320f / 380f + offset

            val path = Path().apply {
                moveTo(0f, 0f)
                lineTo(w, 0f)
                lineTo(w, rightY)
                // Curva inferior derecha (SVG: C 400,380 400,350 370,350)
                cubicTo(w, rightY, w, flatY, w - cornerRx, flatY)
                // Borde inferior plano
                lineTo(cornerRx, flatY)
                // Curva inferior izquierda (SVG: C 0,350 0,320 0,320)
                cubicTo(0f, flatY, 0f, leftY, 0f, leftY)
                close()
            }

            drawPath(path = path, color = IberGreen, style = Fill)
        }

        // Distancia horizontal: logo se mueve poco, texto queda cerca
        val logoSlide = with(density) { 60.dp.toPx() }
        val textSlide = with(density) { 60.dp.toPx() }
        val currentSpread = spreadOffset.value

        // Logo: se desliza hacia la izquierda
        Image(
            painter = painterResource(R.drawable.ic_launcher_foreground),
            contentDescription = null,
            modifier = Modifier
                .size(200.dp)
                .offset {
                    IntOffset((-currentSpread * logoSlide).toInt(), 0)
                }
                .graphicsLayer {
                    scaleX = logoScale.value
                    scaleY = logoScale.value
                    alpha = logoAlpha.value
                }
        )

        // Texto "Iberdrola": surge desde detrás del logo con cutout de izquierda a derecha
        Text(
            text = "Iberdrola",
            color = Color.White,
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = IberFontRegular,
            modifier = Modifier
                .align(Alignment.Center)
                .offset {
                    IntOffset((currentSpread * textSlide).toInt(), 0)
                }
                .drawWithContent {
                    // Cutout: revela el texto de izquierda a derecha según el progreso
                    clipRect(right = size.width * currentSpread) {
                        this@drawWithContent.drawContent()
                    }
                }
                .graphicsLayer {
                    alpha = textAlpha.value
                }
        )
    }
}
