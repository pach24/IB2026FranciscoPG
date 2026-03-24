package com.iberdrola.practicas2026.FranciscoPG.presentation.invoices.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iberdrola.practicas2026.FranciscoPG.core.error.ErrorClassifier
import com.iberdrola.practicas2026.FranciscoPG.domain.model.Invoice
import com.iberdrola.practicas2026.FranciscoPG.domain.model.InvoiceFilters
import com.iberdrola.practicas2026.FranciscoPG.domain.model.SupplyType
import com.iberdrola.practicas2026.FranciscoPG.domain.usecase.FilterInvoicesUseCase
import com.iberdrola.practicas2026.FranciscoPG.domain.usecase.GetInvoicesUseCase
import com.iberdrola.practicas2026.FranciscoPG.presentation.invoices.mapper.InvoiceUiMapper
import com.iberdrola.practicas2026.FranciscoPG.presentation.invoices.model.InvoiceListUiState
import com.iberdrola.practicas2026.FranciscoPG.presentation.invoices.model.InvoiceUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyInvoicesViewModel @Inject constructor(
    private val getInvoicesUseCase: GetInvoicesUseCase,
    private val filterInvoicesUseCase: FilterInvoicesUseCase,
    private val invoiceUiMapper: InvoiceUiMapper,
    private val errorClassifier: ErrorClassifier
) : ViewModel() {

    private val _uiState = MutableStateFlow<InvoiceUiState>(InvoiceUiState.Loading)
    val uiState: StateFlow<InvoiceUiState> = _uiState.asStateFlow()

    private val _showDialogEvent = MutableStateFlow(false)
    val showDialogEvent: StateFlow<Boolean> = _showDialogEvent.asStateFlow()

    // Facturas originales sin filtrar (accesible para calcular estadísticas combinadas)
    private val _allInvoices = MutableStateFlow<List<Invoice>>(emptyList())
    val allInvoices: StateFlow<List<Invoice>> = _allInvoices.asStateFlow()

    // Filtros aplicados — controlados externamente por el FilterViewModel compartido
    private val _appliedFilters = MutableStateFlow(InvoiceFilters())

    // Indica si el usuario ha pulsado "Aplicar" (independiente del valor de los filtros)
    private val _isFilterModeActive = MutableStateFlow(false)

    // Estado de carga / error separado
    private val _loadingState = MutableStateFlow<InvoiceListUiState>(InvoiceListUiState.Loading)

    private var fetchGeneration = 0
    private var hasLoaded = false
    private var currentSupplyType: SupplyType = SupplyType.ELECTRICITY

    // ── Reactive filtered list: combine invoices + applied filters ─────────
    val listUiState: StateFlow<InvoiceListUiState> = combine(
        _allInvoices,
        _appliedFilters,
        _loadingState,
        _isFilterModeActive
    ) { invoices, filters, loadingState, filterModeActive ->
        // Si estamos en loading o error, priorizar ese estado
        if (loadingState is InvoiceListUiState.Loading ||
            loadingState is InvoiceListUiState.ServerError ||
            loadingState is InvoiceListUiState.ConnectionError
        ) {
            return@combine loadingState
        }

        if (invoices.isEmpty()) return@combine InvoiceListUiState.Empty

        val filtered = filterInvoicesUseCase(invoices, filters)
        if (filtered.isEmpty()) return@combine InvoiceListUiState.FilteredEmpty
        buildListUiState(filtered, currentSupplyType, isFiltered = filterModeActive)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = InvoiceListUiState.Loading
    )

    fun fetchInvoices(supplyType: SupplyType, useMock: Boolean = true, forceRefresh: Boolean = false) {
        if (hasLoaded && !forceRefresh) return
        Log.d(TAG, "fetchInvoices(supplyType=$supplyType, mock=$useMock, forceRefresh=$forceRefresh)")
        currentSupplyType = supplyType
        val currentGeneration = ++fetchGeneration
        _uiState.value = InvoiceUiState.Loading
        // Solo mostrar shimmer en la primera carga; en refresh mantener datos previos
        if (!hasLoaded) {
            _loadingState.value = InvoiceListUiState.Loading
        }
        hasLoaded = false

        viewModelScope.launch {
            try {
                val result = getInvoicesUseCase(supplyType, forceRefresh = forceRefresh)
                if (currentGeneration != fetchGeneration) return@launch
                result.fold(
                    onSuccess = { invoices ->
                        Log.d(TAG, "Fetched ${invoices.size} invoices")
                        hasLoaded = true
                        _uiState.value = InvoiceUiState.Success(invoices)
                        // Emitir facturas → combine reacciona automáticamente
                        _allInvoices.value = invoices
                        // Marcar que la carga terminó OK
                        _loadingState.value = InvoiceListUiState.Empty // placeholder, combine decide
                    },
                    onFailure = { error ->
                        Log.e(TAG, "Error fetching invoices", error)
                        handleError(error)
                    }
                )
            } catch (e: Exception) {
                if (currentGeneration != fetchGeneration) return@launch
                Log.e(TAG, "Unexpected exception fetching invoices", e)
                handleError(e)
            }
        }
    }

    // ── Filter sync (controlado por FilterViewModel via InvoicesRoute) ──────

    fun setAppliedFilters(filters: InvoiceFilters) {
        _appliedFilters.value = filters
    }

    fun setFilterModeActive(active: Boolean) {
        _isFilterModeActive.value = active
    }

    fun onFeatureNotAvailable() {
        _showDialogEvent.value = true
    }

    fun onDialogHandled() {
        _showDialogEvent.value = false
    }

    companion object {
        private const val TAG = "MyInvoicesVM"
    }

    // ── Helpers privados ──────────────────────────────────────────────────────

    private fun handleError(error: Throwable) {
        val msg = error.message ?: "Error desconocido"
        _uiState.value = InvoiceUiState.Error(msg)
        _loadingState.value = errorClassifier.classify(error)
    }

    private fun buildListUiState(
        invoices: List<Invoice>,
        supplyType: SupplyType,
        isFiltered: Boolean = false
    ): InvoiceListUiState {
        val uiModel = invoiceUiMapper.map(invoices, supplyType)
        return InvoiceListUiState.Success(
            latestInvoice = uiModel.latestInvoice,
            historyItems = uiModel.historyItems,
            isFiltered = isFiltered,
            invoiceCount = invoices.size
        )
    }
}
