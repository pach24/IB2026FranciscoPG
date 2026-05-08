package com.iberdrola.practicas2026.FranciscoPG.presentation.electronicinvoice.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iberdrola.practicas2026.FranciscoPG.domain.model.SupplyType
import com.iberdrola.practicas2026.FranciscoPG.domain.usecase.CensorEmailUseCase
import com.iberdrola.practicas2026.FranciscoPG.domain.usecase.GetContractsUseCase
import com.iberdrola.practicas2026.FranciscoPG.domain.usecase.ResendCodeUseCase
import com.iberdrola.practicas2026.FranciscoPG.domain.usecase.UpdateContractEmailUseCase
import com.iberdrola.practicas2026.FranciscoPG.domain.usecase.ValidateEmailUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ModifyEmailViewModel @Inject constructor(
    private val validateEmailUseCase: ValidateEmailUseCase,
    private val resendCodeUseCase: ResendCodeUseCase,
    private val censorEmailUseCase: CensorEmailUseCase,
    private val updateContractEmailUseCase: UpdateContractEmailUseCase,
    private val getContractsUseCase: GetContractsUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val supplyType: SupplyType = SupplyType.fromApiValue(
        savedStateHandle.get<String>("supplyType") ?: "LUZ"
    )

    private val _currentCensoredEmail = MutableStateFlow("")
    val currentCensoredEmail: StateFlow<String> = _currentCensoredEmail.asStateFlow()

    private var currentRawEmail: String = ""

    private val _email = MutableStateFlow("")
    val email: StateFlow<String> = _email.asStateFlow()

    private val _censoredEmail = MutableStateFlow("")
    val censoredEmail: StateFlow<String> = _censoredEmail.asStateFlow()

    private val _isEmailValid = MutableStateFlow(false)
    val isEmailValid: StateFlow<Boolean> = _isEmailValid.asStateFlow()

    private val _isSameAsCurrentEmail = MutableStateFlow(false)
    val isSameAsCurrentEmail: StateFlow<Boolean> = _isSameAsCurrentEmail.asStateFlow()

    private val _verificationCode = MutableStateFlow("")
    val verificationCode: StateFlow<String> = _verificationCode.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _showBanner = MutableStateFlow(false)
    val showBanner: StateFlow<Boolean> = _showBanner.asStateFlow()

    private val _resendAttemptsLeft = MutableStateFlow(resendCodeUseCase.attemptsLeft)
    val resendAttemptsLeft: StateFlow<Int> = _resendAttemptsLeft.asStateFlow()

    init {
        loadCurrentEmail()
    }

    private fun loadCurrentEmail() {
        viewModelScope.launch {
            getContractsUseCase().onSuccess { contracts ->
                val contract = contracts.find { it.supplyType == supplyType }
                val email = contract?.email ?: ""
                currentRawEmail = email
                _currentCensoredEmail.value = if (email.isNotEmpty()) censorEmailUseCase(email) else ""
            }
        }
    }

    fun onEmailChanged(value: String) {
        val isSame = value.isNotEmpty() && value == currentRawEmail
        _email.value = value
        _isSameAsCurrentEmail.value = isSame
        _isEmailValid.value = validateEmailUseCase(value) && !isSame
        _censoredEmail.value = censorEmailUseCase(value)
    }

    fun onVerificationCodeChanged(value: String) {
        _verificationCode.value = value
    }

    private var resendJob: Job? = null

    fun onResendCode() {
        if (!resendCodeUseCase.canResend) return
        _isLoading.value = true
        _showBanner.value = false
        resendJob = viewModelScope.launch {
            resendCodeUseCase.resend()
            _resendAttemptsLeft.value = resendCodeUseCase.attemptsLeft
            delay(2000)
            _isLoading.value = false
            _showBanner.value = true
        }
    }

    fun onBannerDismissed() {
        resendJob?.cancel()
        resendJob = null
        _isLoading.value = false
        _showBanner.value = false
    }

    fun onModificationConfirmed(): Boolean {
        if (_isLoading.value) return false
        viewModelScope.launch {
            updateContractEmailUseCase(supplyType, _email.value)
        }
        return true
    }
}
