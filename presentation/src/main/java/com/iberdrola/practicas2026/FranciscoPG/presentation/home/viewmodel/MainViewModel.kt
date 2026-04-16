package com.iberdrola.practicas2026.FranciscoPG.presentation.home.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iberdrola.practicas2026.FranciscoPG.domain.model.SupplyType
import com.iberdrola.practicas2026.FranciscoPG.domain.usecase.GetInvoicesUseCase
import com.iberdrola.practicas2026.FranciscoPG.domain.usecase.GetMockModeUseCase
import com.iberdrola.practicas2026.FranciscoPG.domain.usecase.SetMockModeUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val getMockModeUseCase: GetMockModeUseCase,
    private val setMockModeUseCase: SetMockModeUseCase,
    private val getInvoicesUseCase: GetInvoicesUseCase
) : ViewModel() {

    private val _userName = MutableStateFlow("")
    val userName: StateFlow<String> = _userName.asStateFlow()

    private val _useMock = MutableStateFlow(getMockModeUseCase())
    val useMock: StateFlow<Boolean> = _useMock.asStateFlow()

    // Evento one-shot para avisar a la UI del cambio de modo (null = no hay evento pendiente)
    private val _mockModeChanged = MutableStateFlow<Boolean?>(null)
    val mockModeChanged: StateFlow<Boolean?> = _mockModeChanged.asStateFlow()

    private val _latestInvoiceAmount = MutableStateFlow("- -")
    val latestInvoiceAmount: StateFlow<String> = _latestInvoiceAmount.asStateFlow()

    private val _isLoadingInvoice = MutableStateFlow(true)
    val isLoadingInvoice: StateFlow<Boolean> = _isLoadingInvoice.asStateFlow()

    private var fetchJob: Job? = null

    init {
        loadUserData()
        fetchLatestInvoice()
    }

    private fun loadUserData() {
        _userName.value = "FRANCISCO"
    }

    private fun fetchLatestInvoice() {
        fetchJob?.cancel()
        fetchJob = viewModelScope.launch {
            _isLoadingInvoice.value = true
            try {
                val minDelay = async { delay(MIN_LOADING_MS) }
                val result = getInvoicesUseCase(SupplyType.ELECTRICITY)
                val amount = result.getOrNull()?.firstOrNull()?.amount
                _latestInvoiceAmount.value = if (amount != null) {
                    String.format(Locale("es", "ES"), "%.2f", amount)
                } else {
                    "- -"
                }
                minDelay.await()
            } finally {
                _isLoadingInvoice.value = false
            }
        }
    }

    companion object {
        private const val MIN_LOADING_MS = 1500L
    }

    fun updateMockMode(isEnabled: Boolean) {
        viewModelScope.launch {
            setMockModeUseCase(isEnabled)
            _useMock.value = isEnabled
            _mockModeChanged.value = isEnabled
            fetchLatestInvoice()
        }
    }

    fun onMockModeEventConsumed() {
        _mockModeChanged.value = null
    }
}