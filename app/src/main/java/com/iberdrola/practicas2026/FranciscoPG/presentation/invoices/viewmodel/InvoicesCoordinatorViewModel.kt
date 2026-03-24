package com.iberdrola.practicas2026.FranciscoPG.presentation.invoices.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iberdrola.practicas2026.FranciscoPG.domain.model.Invoice
import com.iberdrola.practicas2026.FranciscoPG.domain.model.InvoiceFilters
import com.iberdrola.practicas2026.FranciscoPG.domain.model.maxAmount
import kotlin.math.ceil
import com.iberdrola.practicas2026.FranciscoPG.domain.model.newestDate
import com.iberdrola.practicas2026.FranciscoPG.domain.model.oldestDate
import com.iberdrola.practicas2026.FranciscoPG.presentation.invoices.model.InvoiceListUiState
import com.iberdrola.practicas2026.FranciscoPG.presentation.invoices.ui.screens.resolvePreferredTabIndex
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
class InvoicesCoordinatorViewModel @Inject constructor() : ViewModel() {

    // ── Inputs from child ViewModels (set by the Route) ─────────────────────
    private val _electricityState = MutableStateFlow<InvoiceListUiState>(InvoiceListUiState.Loading)
    private val _gasState = MutableStateFlow<InvoiceListUiState>(InvoiceListUiState.Loading)
    private val _electricityInvoices = MutableStateFlow<List<Invoice>>(emptyList())
    private val _gasInvoices = MutableStateFlow<List<Invoice>>(emptyList())

    private val _activeTab = MutableStateFlow(0)
    val activeTab: StateFlow<Int> = _activeTab.asStateFlow()

    private val _preferredTabIndex = MutableStateFlow(0)
    val preferredTabIndex: StateFlow<Int> = _preferredTabIndex.asStateFlow()

    private val _scrollCompleted = MutableStateFlow(false)
    val scrollCompleted: StateFlow<Boolean> = _scrollCompleted.asStateFlow()

    // ── Derived states ──────────────────────────────────────────────────────

    val bothLoaded: StateFlow<Boolean> = combine(
        _electricityState, _gasState
    ) { elec, gas ->
        elec !is InvoiceListUiState.Loading && gas !is InvoiceListUiState.Loading
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), false)

    val isGlobalEmpty: StateFlow<Boolean> = combine(
        _electricityState, _gasState, bothLoaded
    ) { elec, gas, loaded ->
        loaded && elec is InvoiceListUiState.Empty && gas is InvoiceListUiState.Empty
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), false)

    // ── Actions ─────────────────────────────────────────────────────────────

    fun onTabChanged(tab: Int) {
        _activeTab.value = tab
    }

    fun setElectricityState(state: InvoiceListUiState) {
        _electricityState.value = state
    }

    fun setGasState(state: InvoiceListUiState) {
        _gasState.value = state
    }

    fun setElectricityInvoices(invoices: List<Invoice>) {
        _electricityInvoices.value = invoices
    }

    fun setGasInvoices(invoices: List<Invoice>) {
        _gasInvoices.value = invoices
    }

    // ── Filter sync: push applied filters to both invoice VMs ───────────────

    fun startFilterSync(filterViewModel: FilterViewModel, vararg invoiceViewModels: MyInvoicesViewModel) {
        viewModelScope.launch {
            filterViewModel.appliedFilters.collect { filters ->
                invoiceViewModels.forEach { it.setAppliedFilters(filters) }
            }
        }
        viewModelScope.launch {
            filterViewModel.isFilterModeActive.collect { active ->
                invoiceViewModels.forEach { it.setFilterModeActive(active) }
            }
        }
    }

    // ── Statistics sync: combine invoices from both tabs ─────────────────────

    fun startStatisticsSync(filterViewModel: FilterViewModel) {
        viewModelScope.launch {
            combine(_electricityInvoices, _gasInvoices) { elec, gas -> elec + gas }
                .collect { allInvoices ->
                    if (allInvoices.isNotEmpty()) {
                        filterViewModel.updateStatistics(
                            maxAmount = ceil(allInvoices.maxAmount()),
                            oldestDate = allInvoices.oldestDate(),
                            newestDate = allInvoices.newestDate()
                        )
                    }
                }
        }
    }

    // ── Tab auto-switch: react to state changes ─────────────────────────────

    fun startAutoSwitch() {
        viewModelScope.launch {
            combine(_electricityState, _gasState, bothLoaded, _activeTab) { elec, gas, loaded, tab ->
                if (loaded) {
                    resolvePreferredTabIndex(elec, gas, loaded, tab)
                } else tab
            }.collect { resolved ->
                if (resolved != _activeTab.value) {
                    _preferredTabIndex.value = resolved
                    _scrollCompleted.value = true
                }
            }
        }
    }
}
