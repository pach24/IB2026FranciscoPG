package com.iberdrola.practicas2026.FranciscoPG.presentation.electronicinvoice.ui.modify

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.iberdrola.practicas2026.FranciscoPG.presentation.electronicinvoice.viewmodel.ModifyEmailViewModel

@Composable
fun ModifyEmailWizardRoute(
    onNavigateBack: () -> Unit,
    onComplete: (String) -> Unit
) {
    val viewModel: ModifyEmailViewModel = hiltViewModel()
    val email by viewModel.email.collectAsStateWithLifecycle()
    val isEmailValid by viewModel.isEmailValid.collectAsStateWithLifecycle()
    val isSameAsCurrentEmail by viewModel.isSameAsCurrentEmail.collectAsStateWithLifecycle()
    val verificationCode by viewModel.verificationCode.collectAsStateWithLifecycle()
    val isLoading by viewModel.isLoading.collectAsStateWithLifecycle()
    val showBanner by viewModel.showBanner.collectAsStateWithLifecycle()
    val resendAttemptsLeft by viewModel.resendAttemptsLeft.collectAsStateWithLifecycle()
    val censoredEmail by viewModel.censoredEmail.collectAsStateWithLifecycle()
    val currentCensoredEmail by viewModel.currentCensoredEmail.collectAsStateWithLifecycle()

    ModifyEmailWizardScreen(
        email = email,
        censoredEmail = censoredEmail,
        currentCensoredEmail = currentCensoredEmail,
        isEmailValid = isEmailValid,
        isSameAsCurrentEmail = isSameAsCurrentEmail,
        verificationCode = verificationCode,
        isLoading = isLoading,
        showBanner = showBanner,
        resendAttemptsLeft = resendAttemptsLeft,
        onEmailChanged = viewModel::onEmailChanged,
        onEmailFocused = viewModel::onEmailFieldFocused,
        onNextTapped = viewModel::onNextTapped,
        onOtpFieldTapped = viewModel::onOtpFieldTapped,
        onVerificationCodeChanged = viewModel::onVerificationCodeChanged,
        onResendCode = viewModel::onResendCode,
        onBannerDismissed = viewModel::onBannerDismissed,
        onConfirmed = viewModel::onModificationConfirmed,
        onNavigateBack = onNavigateBack,
        onComplete = { viewModel.onWizardComplete(); onComplete(censoredEmail) },
        onWizardAbandoned = viewModel::onWizardAbandoned
    )
}
