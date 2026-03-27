package com.iberdrola.practicas2026.FranciscoPG.presentation.electronicinvoice.viewmodel

import androidx.lifecycle.ViewModel
import com.iberdrola.practicas2026.FranciscoPG.domain.usecase.ValidateEmailUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class ActivateElectronicInvoiceViewModel @Inject constructor(
    private val validateEmailUseCase: ValidateEmailUseCase
) : ViewModel() {

    private val _email = MutableStateFlow("")
    val email: StateFlow<String> = _email.asStateFlow()

    private val _legalAccepted = MutableStateFlow(false)
    val legalAccepted: StateFlow<Boolean> = _legalAccepted.asStateFlow()

    private val _isEmailValid = MutableStateFlow(false)
    val isEmailValid: StateFlow<Boolean> = _isEmailValid.asStateFlow()

    private val _verificationCode = MutableStateFlow("")
    val verificationCode: StateFlow<String> = _verificationCode.asStateFlow()

    fun onVerificationCodeChanged(value: String) {
        _verificationCode.value = value
    }

    fun onResendCode() {
        // TODO: lógica de reenvío de código
    }

    fun onEmailChanged(value: String) {
        _email.value = value
        _isEmailValid.value = validateEmailUseCase(value)
    }

    fun onLegalAcceptedChanged(value: Boolean) {
        _legalAccepted.value = value
    }
}
