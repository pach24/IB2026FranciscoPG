package com.iberdrola.practicas2026.FranciscoPG.presentation.invoices.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iberdrola.practicas2026.FranciscoPG.core.error.ErrorClassifier
import com.iberdrola.practicas2026.FranciscoPG.domain.model.Invoice
import com.iberdrola.practicas2026.FranciscoPG.domain.model.InvoiceFilters
import com.iberdrola.practicas2026.FranciscoPG.domain.model.SupplyType
import com.iberdrola.practicas2026.FranciscoPG.domain.model.maxAmount
import com.iberdrola.practicas2026.FranciscoPG.domain.model.newestDate
import com.iberdrola.practicas2026.FranciscoPG.domain.model.oldestDate
import com.iberdrola.practicas2026.FranciscoPG.domain.usecase.FilterInvoicesUseCase
import com.iberdrola.practicas2026.FranciscoPG.domain.usecase.GetInvoicesUseCase
import com.iberdrola.practicas2026.FranciscoPG.presentation.invoices.mapper.InvoiceUiMapper
import com.iberdrola.practicas2026.FranciscoPG.presentation.invoices.model.InvoiceListItem
import com.iberdrola.practicas2026.FranciscoPG.presentation.invoices.ui.components.filter.InvoiceFilterUIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.ZoneOffset
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
    private val filterInvoicesUseCase: FilterInvoicesUseCase,
    private val invoiceUiMapper: InvoiceUiMapper,
    private val errorClassifier: ErrorClassifier
) : ViewModel() {

    private val _uiState = MutableStateFlow<InvoiceUiState>(InvoiceUiState.Loading)
    val uiState: StateFlow<InvoiceUiState> = _uiState.asStateFlow()

    private val _showDialogEvent = MutableStateFlow(false)
    val showDialogEvent: StateFlow<Boolean> = _showDialogEvent.asStateFlow()

    // Facturas originales sin filtrar
    private val _allInvoices = MutableStateFlow<List<Invoice>>(emptyList())

    // Filtros que el usuario está modificando en la UI (draft)
    private val _filterState = MutableStateFlow(InvoiceFilterUIState())
    val filterState: StateFlow<InvoiceFilterUIState> = _filterState.asStateFlow()

    // Filtros confirmados que realmente se aplican a la lista
    private val _appliedFilters = MutableStateFlow(InvoiceFilters())

    // Estado de carga / error separado
    private val _loadingState = MutableStateFlow<InvoiceListUiState>(InvoiceListUiState.Loading)

    private var fetchGeneration = 0
    private var hasLoaded = false
    private var currentSupplyType: SupplyType = SupplyType.ELECTRICITY

    // ── Reactive filtered list: combine invoices + applied filters ─────────
    val listUiState: StateFlow<InvoiceListUiState> = combine(
        _allInvoices,
        _appliedFilters,
        _loadingState
    ) { invoices, filters, loadingState ->
        // Si estamos en loading o error, priorizar ese estado
        if (loadingState is InvoiceListUiState.Loading ||
            loadingState is InvoiceListUiState.ServerError ||
            loadingState is InvoiceListUiState.ConnectionError
        ) {
            return@combine loadingState
        }

        if (invoices.isEmpty()) return@combine InvoiceListUiState.Empty

        val filtered = filterInvoicesUseCase(invoices, filters)
        buildListUiState(filtered, currentSupplyType)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = InvoiceListUiState.Loading
    )

    fun fetchInvoices(supplyType: SupplyType, useMock: Boolean = true, forceRefresh: Boolean = false) {
        if (hasLoaded && !forceRefresh) return
        Log.d(TAG, "fetchInvoices(supplyType=$supplyType, mock=$useMock, forceRefresh=$forceRefresh)")
        hasLoaded = false
        currentSupplyType = supplyType
        val currentGeneration = ++fetchGeneration
        _uiState.value = InvoiceUiState.Loading
        _loadingState.value = InvoiceListUiState.Loading

        viewModelScope.launch {
            try {
                val result = getInvoicesUseCase(supplyType, forceRefresh = forceRefresh)
                if (currentGeneration != fetchGeneration) return@launch
                result.fold(
                    onSuccess = { invoices ->
                        Log.d(TAG, "Fetched ${invoices.size} invoices")
                        hasLoaded = true
                        _uiState.value = InvoiceUiState.Success(invoices)
                        // Actualizar estadísticas de filtro con los datos reales
                        updateFilterBounds(invoices)
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

    // ── Filter actions ────────────────────────────────────────────────────────

    fun updateFilters(filters: InvoiceFilters) {
        _filterState.value = _filterState.value.copy(filters = filters.normalize())
    }

    fun applyFilters() {
        _appliedFilters.value = _filterState.value.filters
    }

    fun clearFilters() {
        val maxAmount = _filterState.value.statistics.maxAmount
        val cleanFilters = InvoiceFilters(
            minAmount = 0.0,
            maxAmount = maxAmount,
            startDate = null,
            endDate = null,
            filteredStatuses = emptySet()
        )
        _filterState.value = _filterState.value.copy(filters = cleanFilters)
        _appliedFilters.value = InvoiceFilters()
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

    private fun updateFilterBounds(invoices: List<Invoice>) {
        val newMaxAmount = invoices.maxAmount()
        val oldestDate = invoices.oldestDate()
        val newestDate = invoices.newestDate()

        val newStats = InvoiceFilterUIState.FilterStatistics(
            maxAmount = newMaxAmount,
            oldestDateMillis = oldestDate.toEpochMilli(),
            newestDateMillis = newestDate.toEpochMilli()
        )

        val currentUI = _filterState.value
        val oldFilters = currentUI.filters
        val oldStats = currentUI.statistics

        // Ajustar slider al nuevo rango
        val finalMin = (oldFilters.minAmount ?: 0.0).coerceIn(0.0, newMaxAmount)
        val finalMax = if (oldFilters.maxAmount != null && oldFilters.maxAmount!! >= oldStats.maxAmount * 0.99) {
            newMaxAmount
        } else {
            (oldFilters.maxAmount ?: newMaxAmount).coerceIn(finalMin, newMaxAmount)
        }

        _filterState.value = currentUI.copy(
            filters = oldFilters.copy(minAmount = finalMin, maxAmount = finalMax),
            statistics = newStats
        )
    }

    private fun LocalDate?.toEpochMilli(): Long =
        this?.atStartOfDay(ZoneOffset.UTC)?.toInstant()?.toEpochMilli() ?: 0L

    private fun handleError(error: Throwable) {
        val msg = error.message ?: "Error desconocido"
        _uiState.value = InvoiceUiState.Error(msg)
        _loadingState.value = errorClassifier.classify(error)
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
