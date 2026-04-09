package com.iberdrola.practicas2026.FranciscoPG.presentation.myinvoices.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iberdrola.practicas2026.FranciscoPG.core.error.ErrorClassifier
import com.iberdrola.practicas2026.FranciscoPG.domain.model.Invoice
import com.iberdrola.practicas2026.FranciscoPG.domain.model.InvoiceFilters
import com.iberdrola.practicas2026.FranciscoPG.domain.model.SupplyType
import com.iberdrola.practicas2026.FranciscoPG.domain.model.maxAmount
import com.iberdrola.practicas2026.FranciscoPG.domain.model.minAmount
import com.iberdrola.practicas2026.FranciscoPG.domain.model.newestDate
import com.iberdrola.practicas2026.FranciscoPG.domain.model.oldestDate
import com.iberdrola.practicas2026.FranciscoPG.domain.usecase.FilterInvoicesUseCase
import com.iberdrola.practicas2026.FranciscoPG.domain.usecase.GetInvoicesUseCase
import com.iberdrola.practicas2026.FranciscoPG.presentation.myinvoices.mapper.InvoiceUiMapper
import com.iberdrola.practicas2026.FranciscoPG.presentation.myinvoices.model.InvoiceListUiState
import com.iberdrola.practicas2026.FranciscoPG.presentation.myinvoices.model.InvoicesUiState
import com.iberdrola.practicas2026.FranciscoPG.presentation.myinvoices.ui.screens.resolvePreferredTabIndex
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.math.ceil
import kotlin.math.floor

@HiltViewModel
class InvoicesViewModel @Inject constructor(
    private val getInvoicesUseCase: GetInvoicesUseCase,
    private val filterInvoicesUseCase: FilterInvoicesUseCase,
    private val invoiceUiMapper: InvoiceUiMapper,
    private val errorClassifier: ErrorClassifier
) : ViewModel() {

    // ── Supply streams ───────────────────────────────────────────────────────

    private val electricity = SupplyStream(SupplyType.ELECTRICITY)
    private val gas = SupplyStream(SupplyType.GAS)

    // ── Filters (connected from FilterViewModel via Route) ───────────────────

    private val _appliedFilters = MutableStateFlow(InvoiceFilters())
    private val _isFilterModeActive = MutableStateFlow(false)

    // ── Tab / UI state ───────────────────────────────────────────────────────

    private val _activeTab = MutableStateFlow(0)
    private val _preferredTabIndex = MutableStateFlow(0)
    private val _scrollCompleted = MutableStateFlow(false)
    private val _showBanner = MutableStateFlow(false)

    private var currentMock = true
    private var filtersConnected = false

    // ── Derived: per-supply list states ──────────────────────────────────────

    private val electricityListState: StateFlow<InvoiceListUiState> = combine(
        electricity.allInvoices, _appliedFilters, electricity.loadingState, _isFilterModeActive
    ) { invoices, filters, loadState, filterModeActive ->
        resolveListState(invoices, filters, loadState, filterModeActive, SupplyType.ELECTRICITY)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), InvoiceListUiState.Loading)

    private val gasListState: StateFlow<InvoiceListUiState> = combine(
        gas.allInvoices, _appliedFilters, gas.loadingState, _isFilterModeActive
    ) { invoices, filters, loadState, filterModeActive ->
        resolveListState(invoices, filters, loadState, filterModeActive, SupplyType.GAS)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), InvoiceListUiState.Loading)

    // ── Derived: coordination states ─────────────────────────────────────────

    private val bothLoaded: StateFlow<Boolean> = combine(
        electricityListState, gasListState
    ) { elec, gas ->
        elec !is InvoiceListUiState.Loading && gas !is InvoiceListUiState.Loading
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), false)

    private val isGlobalEmpty: StateFlow<Boolean> = combine(
        electricityListState, gasListState, bothLoaded
    ) { elec, gas, loaded ->
        loaded && elec is InvoiceListUiState.Empty && gas is InvoiceListUiState.Empty
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), false)

    // ── Public: unified UI state ─────────────────────────────────────────────

    @Suppress("UNCHECKED_CAST")
    val uiState: StateFlow<InvoicesUiState> = combine(
        listOf(
            electricityListState, gasListState, _appliedFilters,
            _isFilterModeActive, _preferredTabIndex, isGlobalEmpty,
            _scrollCompleted, _showBanner
        )
    ) { values ->
        val elecState = values[0] as InvoiceListUiState
        val gasState = values[1] as InvoiceListUiState
        val filters = values[2] as InvoiceFilters
        val isFiltered = values[3] as Boolean
        val preferredTab = values[4] as Int
        val globalEmpty = values[5] as Boolean
        val scrollDone = values[6] as Boolean
        val banner = values[7] as Boolean

        val effectiveElecState = if (
            elecState is InvoiceListUiState.Empty && !globalEmpty && !scrollDone
        ) InvoiceListUiState.Loading else elecState

        InvoicesUiState(
            electricityState = effectiveElecState,
            gasState = gasState,
            activeFilterCount = filters.activeCount,
            isFiltered = isFiltered,
            isGlobalEmpty = globalEmpty,
            preferredTabIndex = preferredTab,
            showBanner = banner
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), InvoicesUiState())

    // ── Init: auto-switch tab ────────────────────────────────────────────────

    init {
        viewModelScope.launch {
            combine(electricityListState, gasListState, bothLoaded, _activeTab) { elec, gas, loaded, tab ->
                if (loaded) resolvePreferredTabIndex(elec, gas, loaded, tab) else tab
            }.collect { resolved ->
                if (resolved != _activeTab.value) {
                    _preferredTabIndex.value = resolved
                    _scrollCompleted.value = true
                }
            }
        }
    }

    // ── Public API ───────────────────────────────────────────────────────────

    fun onEvent(event: InvoicesEvent) {
        when (event) {
            is InvoicesEvent.OnMockModeChanged -> onMockModeChanged(event.useMock)
            is InvoicesEvent.OnRefresh -> refresh()
            is InvoicesEvent.OnTabChanged -> _activeTab.value = event.index
            is InvoicesEvent.OnFeatureNotAvailable -> _showBanner.value = true
            is InvoicesEvent.OnBannerDismissed -> _showBanner.value = false
        }
    }

    fun connectFilters(
        appliedFilters: StateFlow<InvoiceFilters>,
        isFilterModeActive: StateFlow<Boolean>
    ) {
        if (filtersConnected) return
        filtersConnected = true
        viewModelScope.launch { appliedFilters.collect { _appliedFilters.value = it } }
        viewModelScope.launch { isFilterModeActive.collect { _isFilterModeActive.value = it } }
    }

    fun connectStatistics(filterViewModel: FilterViewModel) {
        viewModelScope.launch {
            combine(electricity.allInvoices, gas.allInvoices) { elec, gas -> elec + gas }
                .collect { allInvoices ->
                    if (allInvoices.isNotEmpty()) {
                        filterViewModel.updateStatistics(
                            minAmount = floor(allInvoices.minAmount()),
                            maxAmount = ceil(allInvoices.maxAmount()),
                            oldestDate = allInvoices.oldestDate(),
                            newestDate = allInvoices.newestDate()
                        )
                    }
                }
        }
    }

    fun getFilteredTotalCount(): Int {
        val elecCount = (electricityListState.value as? InvoiceListUiState.Success)?.invoiceCount ?: 0
        val gasCount = (gasListState.value as? InvoiceListUiState.Success)?.invoiceCount ?: 0
        return elecCount + gasCount
    }

    // ── Private: fetch logic ─────────────────────────────────────────────────

    private fun onMockModeChanged(useMock: Boolean) {
        val modeChanged = useMock != currentMock
        currentMock = useMock
        fetchBoth(forceRefresh = modeChanged)
    }

    private fun refresh() {
        fetchBoth(forceRefresh = true)
    }

    private fun fetchBoth(forceRefresh: Boolean) {
        fetchSupply(electricity, forceRefresh)
        fetchSupply(gas, forceRefresh)
    }

    private fun fetchSupply(stream: SupplyStream, forceRefresh: Boolean) {
        if (stream.hasLoaded && !forceRefresh) return
        Log.d(TAG, "fetchSupply(${stream.supplyType}, mock=$currentMock, force=$forceRefresh)")

        val currentGeneration = ++stream.fetchGeneration
        if (!stream.hasLoaded) {
            stream.loadingState.value = InvoiceListUiState.Loading
        }
        stream.hasLoaded = false

        viewModelScope.launch {
            try {
                val result = getInvoicesUseCase(stream.supplyType, forceRefresh = forceRefresh)
                if (currentGeneration != stream.fetchGeneration) return@launch
                result.fold(
                    onSuccess = { invoices ->
                        Log.d(TAG, "Fetched ${invoices.size} ${stream.supplyType} invoices")
                        stream.hasLoaded = true
                        stream.allInvoices.value = invoices
                        stream.loadingState.value = InvoiceListUiState.Empty // placeholder, combine decides
                    },
                    onFailure = { error ->
                        Log.e(TAG, "Error fetching ${stream.supplyType} invoices", error)
                        stream.loadingState.value = errorClassifier.classify(error)
                    }
                )
            } catch (e: Exception) {
                if (currentGeneration != stream.fetchGeneration) return@launch
                Log.e(TAG, "Unexpected exception fetching ${stream.supplyType} invoices", e)
                stream.loadingState.value = errorClassifier.classify(e)
            }
        }
    }

    // ── Private: list state resolution ───────────────────────────────────────

    private fun resolveListState(
        invoices: List<Invoice>,
        filters: InvoiceFilters,
        loadState: InvoiceListUiState,
        filterModeActive: Boolean,
        supplyType: SupplyType
    ): InvoiceListUiState {
        if (loadState is InvoiceListUiState.Loading ||
            loadState is InvoiceListUiState.ServerError ||
            loadState is InvoiceListUiState.ConnectionError
        ) return loadState

        if (invoices.isEmpty()) return InvoiceListUiState.Empty

        val filtered = filterInvoicesUseCase(invoices, filters)
        if (filtered.isEmpty()) return InvoiceListUiState.FilteredEmpty

        val uiModel = invoiceUiMapper.map(filtered, supplyType)
        return InvoiceListUiState.Success(
            latestInvoice = uiModel.latestInvoice,
            historyItems = uiModel.historyItems,
            isFiltered = filterModeActive,
            invoiceCount = filtered.size
        )
    }

    // ── Inner class: per-supply mutable state ────────────────────────────────

    private inner class SupplyStream(val supplyType: SupplyType) {
        val allInvoices = MutableStateFlow<List<Invoice>>(emptyList())
        val loadingState = MutableStateFlow<InvoiceListUiState>(InvoiceListUiState.Loading)
        var fetchGeneration = 0
        var hasLoaded = false
    }

    companion object {
        private const val TAG = "InvoicesVM"
    }
}
