package com.iberdrola.practicas2026.FranciscoPG.presentation.common

import android.content.res.Configuration
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.EaseInOut
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.iberdrola.practicas2026.FranciscoPG.presentation.theme.IberFontBold
import com.iberdrola.practicas2026.FranciscoPG.presentation.theme.IberdrolaTheme
import com.iberdrola.practicas2026.FranciscoPG.presentation.theme.Radius
import com.iberdrola.practicas2026.FranciscoPG.presentation.theme.Spacing
import com.iberdrola.practicas2026.FranciscoPG.presentation.theme.Stroke
import com.iberdrola.practicas2026.FranciscoPG.presentation.theme.TextSize
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun OtpInput(
    code: String,
    onCodeChanged: (String) -> Unit,
    title: String,
    modifier: Modifier = Modifier
) {
    val colors = IberdrolaTheme.colors
    val focusRequester = remember { FocusRequester() }
    val isComplete = code.length == 6
    val boxScales = remember { List(6) { Animatable(1f) } }

    LaunchedEffect(Unit) {
        delay(300L)
        focusRequester.requestFocus()
    }

    LaunchedEffect(isComplete) {
        if (isComplete) {
            boxScales.forEachIndexed { index, animatable ->
                launch {
                    delay(index * 60L)
                    animatable.animateTo(1.18f, animationSpec = tween(80))
                    animatable.animateTo(1f, animationSpec = spring(dampingRatio = 0.4f, stiffness = 500f))
                }
            }
        }
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = Spacing.dp24)
            .padding(bottom = Spacing.dp40),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = title,
            fontFamily = IberFontBold,
            fontWeight = FontWeight.Bold,
            fontSize = TextSize.sp17,
            color = colors.textPrimary
        )

        Spacer(modifier = Modifier.height(Spacing.dp24))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { focusRequester.requestFocus() },
            horizontalArrangement = Arrangement.spacedBy(Spacing.dp8)
        ) {
            repeat(6) { index ->
                OtpDigitBox(
                    digit = code.getOrNull(index)?.toString() ?: "",
                    isActive = index == code.length && !isComplete,
                    scale = boxScales[index].value,
                    modifier = Modifier.weight(1f)
                )
            }
        }

        BasicTextField(
            value = TextFieldValue(
                text = code,
                selection = TextRange(code.length)
            ),
            onValueChange = { onCodeChanged(it.text) },
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = ImeAction.None),
            textStyle = TextStyle(color = Color.Transparent),
            cursorBrush = SolidColor(Color.Transparent),
            modifier = Modifier
                .size(1.dp)
                .alpha(0f)
                .focusRequester(focusRequester)
        )
    }
}

@Composable
private fun OtpDigitBox(
    digit: String,
    isActive: Boolean,
    scale: Float,
    modifier: Modifier = Modifier
) {
    val colors = IberdrolaTheme.colors
    val shape = RoundedCornerShape(Radius.dp8)

    val borderColor by animateColorAsState(
        targetValue = if (digit.isNotEmpty()) colors.iberdrolaGreen else colors.strokeNeutral,
        animationSpec = tween(durationMillis = 250),
        label = "otpBorder"
    )

    Box(
        modifier = modifier
            .height(56.dp)
            .graphicsLayer { scaleX = scale; scaleY = scale }
            .border(
                width = Stroke.dp2,
                color = borderColor,
                shape = shape
            ),
        contentAlignment = Alignment.Center
    ) {
        if (digit.isNotEmpty()) {
            Text(
                text = digit,
                fontFamily = IberFontBold,
                fontWeight = FontWeight.Bold,
                fontSize = TextSize.sp22,
                color = colors.textPrimary
            )
        } else if (isActive) {
            val infiniteTransition = rememberInfiniteTransition(label = "cursor")
            val cursorHeight by infiniteTransition.animateFloat(
                initialValue = 22f,
                targetValue = 0f,
                animationSpec = infiniteRepeatable(
                    animation = tween(durationMillis = 500, delayMillis = 50, easing = EaseInOut),
                    repeatMode = RepeatMode.Reverse
                ),
                label = "cursorHeight"
            )
            Box(
                modifier = Modifier
                    .width(Stroke.dp2)
                    .height(cursorHeight.dp)
                    .background(colors.iberdrolaGreen)
            )
        }
    }
}

@Preview(name = "OtpInput - Light", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_NO)
@Preview(name = "OtpInput - Dark", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun OtpInputPreview() {
    IberdrolaTheme {
        OtpInput(
            code = "123",
            onCodeChanged = {},
            title = "Introduce tu código de verificación"
        )
    }
}
