package com.iberdrola.practicas2026.FranciscoPG.presentation.electronicinvoice.ui.activate

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.iberdrola.practicas2026.FranciscoPG.presentation.electronicinvoice.viewmodel.ActivateElectronicInvoiceViewModel

@Composable
fun ActivateElectronicInvoiceRoute(
    onNavigateBack: () -> Unit
) {
    val viewModel: ActivateElectronicInvoiceViewModel = hiltViewModel()
    val email by viewModel.email.collectAsStateWithLifecycle()
    val legalAccepted by viewModel.legalAccepted.collectAsStateWithLifecycle()
    val isEmailValid by viewModel.isEmailValid.collectAsStateWithLifecycle()
    val verificationCode by viewModel.verificationCode.collectAsStateWithLifecycle()
    val isLoading by viewModel.isLoading.collectAsStateWithLifecycle()
    val showBanner by viewModel.showBanner.collectAsStateWithLifecycle()
    val resendAttemptsLeft by viewModel.resendAttemptsLeft.collectAsStateWithLifecycle()
    val censoredEmail by viewModel.censoredEmail.collectAsStateWithLifecycle()

    ElectronicInvoiceWizardScreen(
        email = email,
        censoredEmail = censoredEmail,
        legalAccepted = legalAccepted,
        isEmailValid = isEmailValid,
        verificationCode = verificationCode,
        isLoading = isLoading,
        showBanner = showBanner,
        resendAttemptsLeft = resendAttemptsLeft,
        onEmailChanged = viewModel::onEmailChanged,
        onLegalAcceptedChanged = viewModel::onLegalAcceptedChanged,
        onVerificationCodeChanged = viewModel::onVerificationCodeChanged,
        onResendCode = viewModel::onResendCode,
        onBannerDismissed = viewModel::onBannerDismissed,
        onConfirmed = viewModel::onActivationConfirmed,
        onNavigateBack = onNavigateBack,
        onNextPage = viewModel::onNextPage,
        onAbandonWizard = viewModel::onAbandonWizard,
        onEmailFieldFocused = viewModel::onEmailFieldFocused,
        onConditionsLinkClick = viewModel::onConditionsLinkClick,
        onMoreInfoClick = viewModel::onMoreInfoClick,
        onOtpFieldTap = viewModel::onOtpFieldTap
    )
}
