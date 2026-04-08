package com.iberdrola.practicas2026.FranciscoPG.presentation.electronicinvoice.ui

import android.content.res.Configuration
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import com.iberdrola.practicas2026.FranciscoPG.R
import com.iberdrola.practicas2026.FranciscoPG.presentation.common.CloseTopBar
import com.iberdrola.practicas2026.FranciscoPG.presentation.common.ConfirmDialog
import com.iberdrola.practicas2026.FranciscoPG.presentation.common.StepBottomButtonBar
import com.iberdrola.practicas2026.FranciscoPG.presentation.common.StepProgressBar
import com.iberdrola.practicas2026.FranciscoPG.presentation.common.SuccessBannerSMS
import com.iberdrola.practicas2026.FranciscoPG.presentation.electronicinvoice.ui.components.LoadingOverlay
import com.iberdrola.practicas2026.FranciscoPG.presentation.theme.IberFontBold
import com.iberdrola.practicas2026.FranciscoPG.presentation.theme.IberdrolaTheme
import com.iberdrola.practicas2026.FranciscoPG.presentation.theme.Spacing
import com.iberdrola.practicas2026.FranciscoPG.presentation.theme.TextSize
import kotlinx.coroutines.launch

private const val WIZARD_PAGE_COUNT = 2

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ModifyEmailWizardScreen(
    email: String,
    censoredEmail: String,
    currentCensoredEmail: String,
    isEmailValid: Boolean,
    verificationCode: String,
    isLoading: Boolean,
    showBanner: Boolean,
    resendAttemptsLeft: Int,
    onEmailChanged: (String) -> Unit,
    onVerificationCodeChanged: (String) -> Unit,
    onResendCode: () -> Unit,
    onBannerDismissed: () -> Unit,
    onNavigateBack: () -> Unit,
    onComplete: (String) -> Unit
) {
    val colors = IberdrolaTheme.colors
    val scope = rememberCoroutineScope()
    val pagerState = rememberPagerState(
        initialPage = 0,
        pageCount = { WIZARD_PAGE_COUNT }
    )

    val currentPage = pagerState.currentPage
    val hasData = email.isNotEmpty()
    var showExitDialog by remember { mutableStateOf(false) }
    var showSuccess by remember { mutableStateOf(false) }

    BackHandler {
        when {
            showSuccess -> onComplete(email)
            currentPage > 0 -> {
                onBannerDismissed()
                scope.launch { pagerState.animateScrollToPage(currentPage - 1) }
            }
            hasData -> showExitDialog = true
            else -> onNavigateBack()
        }
    }

    if (showExitDialog) {
        ConfirmDialog(
            title = stringResource(R.string.exit_wizard_dialog_title),
            message = stringResource(R.string.exit_wizard_dialog_message),
            confirmText = stringResource(R.string.exit_wizard_dialog_confirm),
            dismissText = stringResource(R.string.exit_wizard_dialog_cancel),
            onConfirm = { showExitDialog = false; onNavigateBack() },
            onDismiss = { showExitDialog = false }
        )
    }

    Box(modifier = Modifier.fillMaxSize()) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(colors.background)
                .padding(top = Spacing.dp24)
        ) {
            CloseTopBar(onClose = { if (hasData) showExitDialog = true else onNavigateBack() })

            Text(
                text = stringResource(R.string.modify_email_title),
                color = colors.textPrimary,
                fontFamily = IberFontBold,
                fontWeight = FontWeight.Bold,
                fontSize = TextSize.sp23,
                modifier = Modifier.padding(horizontal = Spacing.dp24)
            )

            Spacer(modifier = Modifier.height(Spacing.dp12))

            StepProgressBar(
                currentStep = if (showSuccess) 4 else currentPage + 2,
                totalSteps = 4,
                modifier = Modifier.padding(horizontal = Spacing.dp12)
            )

            HorizontalPager(
                state = pagerState,
                modifier = Modifier.weight(1f),
                userScrollEnabled = false
            ) { page ->
                when (page) {
                    0 -> ModifyEmailContent(
                        email = email,
                        isEmailValid = isEmailValid,
                        currentCensoredEmail = currentCensoredEmail,
                        onEmailChanged = onEmailChanged
                    )
                    1 -> ConfirmElectronicInvoiceContent(
                        verificationCode = verificationCode,
                        onVerificationCodeChanged = onVerificationCodeChanged,
                        onResendCode = onResendCode,
                        resendAttemptsLeft = resendAttemptsLeft
                    )
                }
            }

            SuccessBannerSMS(
                visible = showBanner && currentPage == 1,
                onDismiss = onBannerDismissed
            )

            StepBottomButtonBar(
                onBack = {
                    if (currentPage == 0) {
                        if (hasData) showExitDialog = true else onNavigateBack()
                    } else {
                        onBannerDismissed()
                        scope.launch { pagerState.animateScrollToPage(currentPage - 1) }
                    }
                },
                onNext = {
                    if (currentPage == 1) {
                        showSuccess = true
                    } else {
                        scope.launch { pagerState.animateScrollToPage(currentPage + 1) }
                    }
                },
                isNextEnabled = when (currentPage) {
                    0 -> isEmailValid
                    1 -> verificationCode.length == 6
                    else -> false
                }
            )
        }

        if (isLoading) {
            LoadingOverlay()
        }

        AnimatedVisibility(
            visible = showSuccess,
            enter = slideInHorizontally(
                initialOffsetX = { fullWidth -> fullWidth },
                animationSpec = tween(durationMillis = 450)
            ) + fadeIn(animationSpec = tween(300)),
            exit = fadeOut()
        ) {
            SuccessElectronicInvoiceContent(
                email = censoredEmail,
                onAccept = { onComplete(email) },
                onClose = { onComplete(email) },
                title = stringResource(R.string.success_modify_email_title),
                onDebugBack = { showSuccess = false }
            )
        }
    }
}

@Preview(name = "Modify Wizard - Light", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_NO)
@Preview(name = "Modify Wizard - Dark", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun ModifyEmailWizardScreenPreview() {
    IberdrolaTheme {
        ModifyEmailWizardScreen(
            email = "",
            censoredEmail = "",
            currentCensoredEmail = "p**2@gmail.com",
            isEmailValid = true,
            verificationCode = "",
            isLoading = false,
            showBanner = false,
            resendAttemptsLeft = 3,
            onEmailChanged = {},
            onVerificationCodeChanged = {},
            onResendCode = {},
            onBannerDismissed = {},
            onNavigateBack = {},
            onComplete = {}
        )
    }
}
