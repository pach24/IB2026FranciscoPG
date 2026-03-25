package com.iberdrola.practicas2026.FranciscoPG.presentation.myinvoices.ui.screens

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.iberdrola.practicas2026.FranciscoPG.presentation.myinvoices.viewmodel.FeedbackViewModel
import com.iberdrola.practicas2026.FranciscoPG.presentation.myinvoices.viewmodel.FilterViewModel
import com.iberdrola.practicas2026.FranciscoPG.presentation.myinvoices.viewmodel.InvoicesEvent
import com.iberdrola.practicas2026.FranciscoPG.presentation.myinvoices.model.InvoiceListUiState
import com.iberdrola.practicas2026.FranciscoPG.presentation.myinvoices.viewmodel.InvoicesViewModel

/**
 * Determina el tab preferido en función del estado de cada suministro.
 * Si el tab actual no tiene contenido (vacío o filtrado vacío) pero el otro sí,
 * devuelve el otro tab. En cualquier otro caso, mantiene el tab actual.
 */
fun resolvePreferredTabIndex(
    electricityState: InvoiceListUiState,
    gasState: InvoiceListUiState,
    bothLoaded: Boolean,
    currentTab: Int = 0
): Int {
    if (!bothLoaded) return currentTab

    fun hasNoContent(state: InvoiceListUiState) =
        state is InvoiceListUiState.Empty || state is InvoiceListUiState.FilteredEmpty

    return when {
        currentTab == 0 && hasNoContent(electricityState) && !hasNoContent(gasState) -> 1
        currentTab == 1 && hasNoContent(gasState) && !hasNoContent(electricityState) -> 0
        else -> currentTab
    }
}

@Composable
fun InvoicesRoute(
    useMock: Boolean,
    onNavigateBack: () -> Unit
) {
    val viewModel: InvoicesViewModel = hiltViewModel()
    val filterViewModel: FilterViewModel = hiltViewModel()
    val feedbackViewModel: FeedbackViewModel = hiltViewModel()

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val feedbackState by feedbackViewModel.sheetState.collectAsStateWithLifecycle()

    // One-time wiring between ViewModels
    LaunchedEffect(Unit) {
        viewModel.connectFilters(filterViewModel.appliedFilters, filterViewModel.isFilterModeActive)
        viewModel.connectStatistics(filterViewModel)
    }

    // React to mock mode changes
    LaunchedEffect(useMock) {
        viewModel.onEvent(InvoicesEvent.OnMockModeChanged(useMock))
    }

    // Navigate back when feedback flow completes
    LaunchedEffect(Unit) {
        feedbackViewModel.navigateBack.collect { onNavigateBack() }
    }

    InvoicesScreen(
        uiState = uiState,
        feedbackSheetState = feedbackState,
        filterViewModel = filterViewModel,
        onEvent = viewModel::onEvent,
        onBackClick = feedbackViewModel::onExitInvoices,
        onFeedbackRated = feedbackViewModel::onFeedbackRated,
        onFeedbackLater = feedbackViewModel::onFeedbackLater,
        onFeedbackDismiss = feedbackViewModel::onSheetDismissed,
        onGetFilteredCount = viewModel::getFilteredTotalCount,
        onRestoreFilters = filterViewModel::restoreFilters
    )
}
