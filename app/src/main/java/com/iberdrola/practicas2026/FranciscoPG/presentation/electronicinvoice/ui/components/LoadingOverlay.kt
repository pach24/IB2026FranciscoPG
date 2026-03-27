package com.iberdrola.practicas2026.FranciscoPG.presentation.electronicinvoice.ui.components

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.iberdrola.practicas2026.FranciscoPG.presentation.theme.IberdrolaTheme

private val SpinnerSize   = 100.dp
private val SpinnerStroke = 9.dp
private val RailStroke    = 9.dp
private val OverlayAlpha  = 0.85f

@Composable
fun LoadingOverlay() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = OverlayAlpha))
            .pointerInput(Unit) {},
        contentAlignment = Alignment.Center
    ) {
        // Carril: círculo completo en gris verdoso
        CircularProgressIndicator(
            progress = { 1f },
            modifier = Modifier.size(SpinnerSize),
            color = IberdrolaTheme.colors.loadingSpinnerRail,
            strokeWidth = RailStroke,
            trackColor = Color.Transparent
        )
        // Spinner animado encima
        CircularProgressIndicator(
            modifier = Modifier.size(SpinnerSize),
            color = IberdrolaTheme.colors.iberdrolaGreen,
            strokeWidth = SpinnerStroke
        )
    }
}

@Preview(name = "LoadingOverlay - Light", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_NO)
@Preview(name = "LoadingOverlay - Dark", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun LoadingOverlayPreview() {
    IberdrolaTheme {
        Box(modifier = Modifier.size(360.dp, 640.dp)) {
            LoadingOverlay()
        }
    }
}
