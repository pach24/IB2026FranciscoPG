package com.iberdrola.practicas2026.FranciscoPG.presentation.home.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iberdrola.practicas2026.FranciscoPG.domain.analytics.AnalyticsEvent
import com.iberdrola.practicas2026.FranciscoPG.domain.analytics.AnalyticsTracker
import com.iberdrola.practicas2026.FranciscoPG.domain.analytics.CrashReporter
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
    private val getInvoicesUseCase: GetInvoicesUseCase,
    private val analyticsTracker: AnalyticsTracker,
    private val crashReporter: CrashReporter
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

    fun refreshLatestInvoice() = fetchLatestInvoice(showShimmer = false)

    private fun fetchLatestInvoice(showShimmer: Boolean = true) {

        Log.d("DEBUG_REFRESH", "Refrescando datos...")

        fetchJob?.cancel()
        fetchJob = viewModelScope.launch {
            if (showShimmer) _isLoadingInvoice.value = true
            try {
                val minDelay = if (showShimmer) async { delay(MIN_LOADING_MS) } else null
                val electricityInvoices = async { getInvoicesUseCase(SupplyType.ELECTRICITY, forceRefresh = true) }
                val gasInvoices = async { getInvoicesUseCase(SupplyType.GAS, forceRefresh = true) }

                val all = (electricityInvoices.await().getOrNull().orEmpty() +
                        gasInvoices.await().getOrNull().orEmpty())

                val latest = all.maxByOrNull { it.chargeDate }

                _latestInvoiceAmount.value = if (latest != null) {
                    String.format(Locale("es", "ES"), "%.2f", latest.amount)
                } else {
                    "- -"
                }
                minDelay?.await()
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

    fun onMyInvoicesCardClick() {
        analyticsTracker.logEvent(AnalyticsEvent.TAP_MY_INVOICES_CARD)
    }

    fun onElectronicInvoiceCardClick() {
        analyticsTracker.logEvent(AnalyticsEvent.TAP_ELECTRONIC_INVOICE_CARD)
    }

    fun onForceCrashClick() {
        analyticsTracker.logEvent(AnalyticsEvent.TAP_FORCE_CRASH)
        crashReporter.forceCrash()
    }
}