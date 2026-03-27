package com.iberdrola.practicas2026.FranciscoPG.presentation.electronicinvoice.ui

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

    ElectronicInvoiceWizardScreen(
        email = email,
        legalAccepted = legalAccepted,
        isEmailValid = isEmailValid,
        verificationCode = verificationCode,
        isLoading = isLoading,
        showBanner = showBanner,
        onEmailChanged = viewModel::onEmailChanged,
        onLegalAcceptedChanged = viewModel::onLegalAcceptedChanged,
        onVerificationCodeChanged = viewModel::onVerificationCodeChanged,
        onResendCode = viewModel::onResendCode,
        onBannerDismissed = viewModel::onBannerDismissed,
        onNavigateBack = onNavigateBack
    )
}
