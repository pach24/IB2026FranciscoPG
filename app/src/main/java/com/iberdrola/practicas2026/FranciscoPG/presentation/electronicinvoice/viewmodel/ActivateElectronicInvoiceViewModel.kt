package com.iberdrola.practicas2026.FranciscoPG.presentation.electronicinvoice.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iberdrola.practicas2026.FranciscoPG.domain.usecase.ValidateEmailUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
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

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _showBanner = MutableStateFlow(false)
    val showBanner: StateFlow<Boolean> = _showBanner.asStateFlow()

    fun onVerificationCodeChanged(value: String) {
        _verificationCode.value = value
    }

    fun onResendCode() {
        viewModelScope.launch {
            _isLoading.value = true
            _showBanner.value = false
            delay(5000)
            _isLoading.value = false
            _showBanner.value = true
        }
    }

    fun onBannerDismissed() {
        _showBanner.value = false
    }

    fun onEmailChanged(value: String) {
        _email.value = value
        _isEmailValid.value = validateEmailUseCase(value)
    }

    fun onLegalAcceptedChanged(value: Boolean) {
        _legalAccepted.value = value
    }
}
