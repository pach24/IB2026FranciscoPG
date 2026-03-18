package com.iberdrola.practicas2026.FranciscoPG.presentation.invoices.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iberdrola.practicas2026.FranciscoPG.core.error.ErrorClassifier
import com.iberdrola.practicas2026.FranciscoPG.domain.model.Invoice
import com.iberdrola.practicas2026.FranciscoPG.domain.model.SupplyType
import com.iberdrola.practicas2026.FranciscoPG.domain.usecase.GetInvoicesUseCase
import com.iberdrola.practicas2026.FranciscoPG.presentation.invoices.mapper.InvoiceUiMapper
import com.iberdrola.practicas2026.FranciscoPG.presentation.invoices.model.InvoiceListItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

// ── UI States ─────────────────────────────────────────────────────────────────

sealed class InvoiceUiState {
    object Loading : InvoiceUiState()
    data class Success(val invoices: List<Invoice>) : InvoiceUiState()
    data class Error(val message: String) : InvoiceUiState()
}

data class LatestInvoiceUiModel(
    val amount: String,
    val dateRange: String,
    val supplyTypeLabel: String,
    val status: String,
    val isPaid: Boolean,
    val iconRes: Int
)

sealed class InvoiceListUiState {
    object Loading : InvoiceListUiState()

    data class Success(
        val latestInvoice: LatestInvoiceUiModel?,
        val historyItems: List<InvoiceListItem>,
        val invoiceCount: Int
    ) : InvoiceListUiState()

    object Empty : InvoiceListUiState()

    data class ServerError(val message: String) : InvoiceListUiState()

    data class ConnectionError(val message: String) : InvoiceListUiState()
}

// ── ViewModel ─────────────────────────────────────────────────────────────────

@HiltViewModel
class MyInvoicesViewModel @Inject constructor(
    private val getInvoicesUseCase: GetInvoicesUseCase,
    private val invoiceUiMapper: InvoiceUiMapper,
    private val errorClassifier: ErrorClassifier
) : ViewModel() {

    private val _uiState = MutableStateFlow<InvoiceUiState>(InvoiceUiState.Loading)
    val uiState: StateFlow<InvoiceUiState> = _uiState.asStateFlow()

    private val _listUiState = MutableStateFlow<InvoiceListUiState>(InvoiceListUiState.Loading)
    val listUiState: StateFlow<InvoiceListUiState> = _listUiState.asStateFlow()

    private val _showDialogEvent = MutableStateFlow(false)
    val showDialogEvent: StateFlow<Boolean> = _showDialogEvent.asStateFlow()

    private var fetchGeneration = 0

    fun fetchInvoices(supplyType: SupplyType, useMock: Boolean = true, forceRefresh: Boolean = false) {
        Log.d("MyInvoicesVM", "fetchInvoices($supplyType, mock=$useMock, force=$forceRefresh)")
        val currentGeneration = ++fetchGeneration
        _uiState.value = InvoiceUiState.Loading
        _listUiState.value = InvoiceListUiState.Loading

        viewModelScope.launch {
            try {
                val result = getInvoicesUseCase(supplyType, forceRefresh = forceRefresh)
                if (currentGeneration != fetchGeneration) return@launch
                result.fold(
                    onSuccess = { invoices ->
                        Log.d("MyInvoicesVM", "SUCCESS: ${invoices.size} facturas")
                        _uiState.value = InvoiceUiState.Success(invoices)
                        _listUiState.value = buildListUiState(invoices, supplyType)
                    },
                    onFailure = { error ->
                        Log.e("MyInvoicesVM", "ERROR", error)
                        handleError(error)
                    }
                )
            } catch (e: Exception) {
                if (currentGeneration != fetchGeneration) return@launch
                Log.e("MyInvoicesVM", "EXCEPTION", e)
                handleError(e)
            }
        }
    }

    fun onFeatureNotAvailable() {
        _showDialogEvent.value = true
    }

    fun onDialogHandled() {
        _showDialogEvent.value = false
    }

    // ── Helpers privados ──────────────────────────────────────────────────────

    private fun handleError(error: Throwable) {
        val msg = error.message ?: "Error desconocido"
        _uiState.value = InvoiceUiState.Error(msg)
        _listUiState.value = errorClassifier.classify(error)
    }

    private fun buildListUiState(
        invoices: List<Invoice>,
        supplyType: SupplyType
    ): InvoiceListUiState {
        if (invoices.isEmpty()) return InvoiceListUiState.Empty

        val uiModel = invoiceUiMapper.map(invoices, supplyType)
        return InvoiceListUiState.Success(
            latestInvoice = uiModel.latestInvoice,
            historyItems = uiModel.historyItems,
            invoiceCount = invoices.size
        )
    }
}
