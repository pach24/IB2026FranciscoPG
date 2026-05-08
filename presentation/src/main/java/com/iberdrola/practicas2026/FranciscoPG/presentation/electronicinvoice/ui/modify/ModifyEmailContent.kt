package com.iberdrola.practicas2026.FranciscoPG.presentation.electronicinvoice.ui.modify
import com.iberdrola.practicas2026.FranciscoPG.presentation.R

import android.content.res.Configuration
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import com.iberdrola.practicas2026.FranciscoPG.presentation.theme.IberFontBold
import com.iberdrola.practicas2026.FranciscoPG.presentation.theme.IberFontRegular
import com.iberdrola.practicas2026.FranciscoPG.presentation.theme.IberdrolaTheme
import com.iberdrola.practicas2026.FranciscoPG.presentation.theme.Spacing
import com.iberdrola.practicas2026.FranciscoPG.presentation.theme.Stroke
import com.iberdrola.practicas2026.FranciscoPG.presentation.theme.TextSize

@Composable
fun ModifyEmailContent(
    email: String,
    isEmailValid: Boolean,
    isSameAsCurrentEmail: Boolean,
    currentCensoredEmail: String,
    onEmailChanged: (String) -> Unit,
    validationTrigger: Int = 0,
    modifier: Modifier = Modifier
) {
    val colors = IberdrolaTheme.colors
    val focusManager = LocalFocusManager.current
    val focusRequester = remember { FocusRequester() }
    var emailHasBlurred by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) { focusRequester.requestFocus() }
    LaunchedEffect(validationTrigger) { if (validationTrigger > 0) emailHasBlurred = true }
    val showError = emailHasBlurred && email.isNotEmpty() && (!isEmailValid || isSameAsCurrentEmail)
    val shakeOffset = remember { Animatable(0f) }

    LaunchedEffect(showError) {
        if (showError) {
            for (target in listOf(12f, -12f, 8f, -8f, 4f, 0f)) {
                shakeOffset.animateTo(target, animationSpec = tween(durationMillis = 50))
            }
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) { focusManager.clearFocus() }
            .verticalScroll(rememberScrollState())
    ) {
        Spacer(modifier = Modifier.height(Spacing.dp24))

        Text(
            text = stringResource(R.string.modify_email_subtitle),
            color = colors.textPrimary,
            fontFamily = IberFontBold,
            fontWeight = FontWeight.Bold,
            fontSize = TextSize.sp17,
            modifier = Modifier.padding(horizontal = Spacing.dp24)
        )

        Spacer(modifier = Modifier.height(Spacing.dp8))

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = Spacing.dp24)
                .graphicsLayer { translationX = shakeOffset.value }
        ) {
            BasicTextField(
                value = email,
                onValueChange = onEmailChanged,
                singleLine = true,
                textStyle = TextStyle(
                    fontFamily = IberFontRegular,
                    fontSize = TextSize.sp15,
                    color = colors.textPrimary
                ),
                cursorBrush = SolidColor(colors.iberdrolaDarkGreen),
                modifier = Modifier
                    .fillMaxWidth()
                    .focusRequester(focusRequester)
                    .onFocusChanged { focusState ->
                        if (focusState.isFocused) {
                            emailHasBlurred = false
                        } else if (email.isNotEmpty()) {
                            emailHasBlurred = true
                        }
                    },
                decorationBox = { innerTextField ->
                    val underlineColor = when {
                        showError -> colors.errorTextForm
                        isEmailValid && email.isNotEmpty() -> colors.iberdrolaGreen
                        else -> colors.lightGrey.copy(alpha = 0.5f)
                    }

                    Column {
                        Box(modifier = Modifier.padding(bottom = Spacing.dp8, top = Spacing.dp20)) {
                            if (email.isEmpty()) {
                                Text(
                                    text = stringResource(R.string.modify_email_input_label),
                                    fontFamily = IberFontRegular,
                                    color = colors.lightGrey,
                                    fontSize = TextSize.sp14
                                )
                            }
                            innerTextField()
                        }
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(Stroke.dp2)
                                .background(underlineColor)
                        )
                        if (showError) {
                            val errorText = if (isSameAsCurrentEmail)
                                stringResource(R.string.modify_email_input_same_email_error)
                            else
                                stringResource(R.string.modify_email_input_error)
                            Text(
                                text = errorText,
                                color = colors.errorTextForm,
                                fontFamily = IberFontRegular,
                                fontSize = TextSize.sp12,
                                modifier = Modifier.padding(top = Spacing.dp4)
                            )
                        }
                    }
                }
            )
        }
    }
}

@Preview(name = "Modify Email Content - Light", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_NO)
@Preview(name = "Modify Email Content - Dark", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun ModifyEmailContentPreview() {
    IberdrolaTheme {
        ModifyEmailContent(
            email = "",
            isEmailValid = true,
            isSameAsCurrentEmail = false,
            currentCensoredEmail = "p**2@gmail.com",
            onEmailChanged = {}
        )
    }
}
