package com.iberdrola.practicas2026.FranciscoPG.presentation.electronicinvoice.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iberdrola.practicas2026.FranciscoPG.domain.model.SupplyType
import com.iberdrola.practicas2026.FranciscoPG.domain.usecase.CensorEmailUseCase
import com.iberdrola.practicas2026.FranciscoPG.domain.usecase.ResendCodeUseCase
import com.iberdrola.practicas2026.FranciscoPG.domain.usecase.UpdateContractEmailUseCase
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
    private val validateEmailUseCase: ValidateEmailUseCase,
    private val resendCodeUseCase: ResendCodeUseCase,
    private val censorEmailUseCase: CensorEmailUseCase,
    private val updateContractEmailUseCase: UpdateContractEmailUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val supplyType: SupplyType = SupplyType.fromApiValue(
        savedStateHandle.get<String>("supplyType") ?: "LUZ"
    )

    private val _email = MutableStateFlow("")
    val email: StateFlow<String> = _email.asStateFlow()

    private val _censoredEmail = MutableStateFlow("")
    val censoredEmail: StateFlow<String> = _censoredEmail.asStateFlow()

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

    private val _resendAttemptsLeft = MutableStateFlow(resendCodeUseCase.attemptsLeft)
    val resendAttemptsLeft: StateFlow<Int> = _resendAttemptsLeft.asStateFlow()

    fun onVerificationCodeChanged(value: String) {
        _verificationCode.value = value
    }

    fun onResendCode() {
        if (!resendCodeUseCase.canResend) return
        viewModelScope.launch {
            _isLoading.value = true
            _showBanner.value = false
            resendCodeUseCase.resend()
            _resendAttemptsLeft.value = resendCodeUseCase.attemptsLeft
            delay(2000)
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
        _censoredEmail.value = censorEmailUseCase(value)
    }

    fun onLegalAcceptedChanged(value: Boolean) {
        _legalAccepted.value = value
    }

    fun onActivationConfirmed() {
        viewModelScope.launch {
            updateContractEmailUseCase(supplyType, _email.value)
        }
    }
}
