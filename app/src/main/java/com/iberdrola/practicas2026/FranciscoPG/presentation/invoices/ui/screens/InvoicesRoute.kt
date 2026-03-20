package com.iberdrola.practicas2026.FranciscoPG.presentation.invoices.ui.screens

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.iberdrola.practicas2026.FranciscoPG.R
import com.iberdrola.practicas2026.FranciscoPG.domain.model.InvoiceFilters
import com.iberdrola.practicas2026.FranciscoPG.domain.model.SupplyType
import com.iberdrola.practicas2026.FranciscoPG.presentation.invoices.model.InvoiceListUiState
import com.iberdrola.practicas2026.FranciscoPG.presentation.invoices.viewmodel.FeedbackViewModel
import com.iberdrola.practicas2026.FranciscoPG.presentation.invoices.viewmodel.FilterViewModel
import com.iberdrola.practicas2026.FranciscoPG.presentation.invoices.viewmodel.InvoicesCoordinatorViewModel
import com.iberdrola.practicas2026.FranciscoPG.presentation.invoices.viewmodel.MyInvoicesViewModel
import com.iberdrola.practicas2026.FranciscoPG.presentation.theme.IberdrolaTheme
import com.iberdrola.practicas2026.FranciscoPG.presentation.theme.Spacing
import kotlinx.coroutines.launch

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
    val electricityViewModel: MyInvoicesViewModel =
        hiltViewModel(key = "electricity_invoices_vm")
    val gasViewModel: MyInvoicesViewModel =
        hiltViewModel(key = "gas_invoices_vm")
    val filterViewModel: FilterViewModel = hiltViewModel()
    val feedbackViewModel: FeedbackViewModel = hiltViewModel()
    val coordinator: InvoicesCoordinatorViewModel = hiltViewModel()

    val electricityState by electricityViewModel.listUiState.collectAsStateWithLifecycle()
    val gasState by gasViewModel.listUiState.collectAsStateWithLifecycle()
    val electricityShowDialog by electricityViewModel.showDialogEvent.collectAsStateWithLifecycle()
    val gasShowDialog by gasViewModel.showDialogEvent.collectAsStateWithLifecycle()
    val sheetState by feedbackViewModel.sheetState.collectAsStateWithLifecycle()
    val appliedFilters by filterViewModel.appliedFilters.collectAsStateWithLifecycle()
    val activeFilterCount = appliedFilters.activeCount

    // Feed states into coordinator
    LaunchedEffect(electricityState) { coordinator.setElectricityState(electricityState) }
    LaunchedEffect(gasState) { coordinator.setGasState(gasState) }

    val electricityInvoices by electricityViewModel.allInvoices.collectAsStateWithLifecycle()
    val gasInvoices by gasViewModel.allInvoices.collectAsStateWithLifecycle()
    LaunchedEffect(electricityInvoices) { coordinator.setElectricityInvoices(electricityInvoices) }
    LaunchedEffect(gasInvoices) { coordinator.setGasInvoices(gasInvoices) }

    // Start coordinator syncs once
    LaunchedEffect(Unit) {
        coordinator.startFilterSync(filterViewModel, electricityViewModel, gasViewModel)
        coordinator.startStatisticsSync(filterViewModel)
        coordinator.startAutoSwitch()
    }

    // Collect coordinator derived states
    val bothLoaded by coordinator.bothLoaded.collectAsStateWithLifecycle()
    val isGlobalEmpty by coordinator.isGlobalEmpty.collectAsStateWithLifecycle()
    val preferredTabIndex by coordinator.preferredTabIndex.collectAsStateWithLifecycle()
    val scrollCompleted by coordinator.scrollCompleted.collectAsStateWithLifecycle()

    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    val electricityListState = rememberLazyListState()
    val gasListState = rememberLazyListState()

    // Snackbar messages (resolved outside coroutine)
    val filtersClearedMsg = stringResource(R.string.snackbar_filters_cleared)
    val undoLabel = stringResource(R.string.snackbar_undo)

    var showFilter by remember { mutableStateOf(false) }

    // Recargar facturas al entrar o al cambiar de modo (mock/retrofit)
    var previousMock by remember { mutableStateOf(useMock) }
    LaunchedEffect(useMock) {
        val modeChanged = useMock != previousMock
        previousMock = useMock
        electricityViewModel.fetchInvoices(SupplyType.ELECTRICITY, useMock, forceRefresh = modeChanged)
        gasViewModel.fetchInvoices(SupplyType.GAS, useMock, forceRefresh = modeChanged)
    }

    val effectiveElectricityState = if (
        electricityState is InvoiceListUiState.Empty
        && !isGlobalEmpty
        && !scrollCompleted
    ) InvoiceListUiState.Loading else electricityState

    val refreshBoth = {
        electricityViewModel.fetchInvoices(SupplyType.ELECTRICITY, useMock, forceRefresh = true)
        gasViewModel.fetchInvoices(SupplyType.GAS, useMock, forceRefresh = true)
    }

    Box(modifier = Modifier.fillMaxSize()) {
        AnimatedContent(
            targetState = showFilter,
            transitionSpec = {
                if (targetState) {
                    slideInHorizontally { it } togetherWith slideOutHorizontally { -it }
                } else {
                    slideInHorizontally { -it } togetherWith slideOutHorizontally { it }
                }
            },
            label = "filter_transition"
        ) { isFilterVisible ->
            if (isFilterVisible) {
                BackHandler { showFilter = false }
                FilterRoute(
                    onBack = { showFilter = false },
                    filterViewModel = filterViewModel,
                    onFiltersApplied = {
                        scope.launch {
                            snackbarHostState.currentSnackbarData?.dismiss()
                            // Small delay to let the list state update after filters apply
                            kotlinx.coroutines.delay(300)
                            val elecCount = (electricityViewModel.listUiState.value as? InvoiceListUiState.Success)?.invoiceCount ?: 0
                            val gasCount = (gasViewModel.listUiState.value as? InvoiceListUiState.Success)?.invoiceCount ?: 0
                            val total = elecCount + gasCount
                            snackbarHostState.showSnackbar(
                                message = "$total facturas coinciden con los filtros",
                                duration = SnackbarDuration.Short
                            )
                        }
                    },
                    onFiltersCleared = { previousFilters ->
                        scope.launch {
                            snackbarHostState.currentSnackbarData?.dismiss()
                            val result = snackbarHostState.showSnackbar(
                                message = filtersClearedMsg,
                                actionLabel = undoLabel,
                                duration = SnackbarDuration.Short
                            )
                            if (result == SnackbarResult.ActionPerformed) {
                                filterViewModel.restoreFilters(previousFilters)
                            }
                        }
                    }
                )
            } else {
            MyInvoicesComposeScreen(
                address = stringResource(R.string.my_invoices_mock_address),
                feedbackSheetState = sheetState,
                isGlobalEmpty = isGlobalEmpty,
                preferredTabIndex = preferredTabIndex,
                onTabChanged = { coordinator.onTabChanged(it) },
                onBackClick = {
                    Log.d("InvoicesRoute", "Back pressed, evaluating feedback")
                    feedbackViewModel.onExitInvoices()
                },
                onFeedbackFaceClick = { feedbackViewModel.onFeedbackRated() },
                onFeedbackLaterClick = { feedbackViewModel.onFeedbackLater() },
                onFeedbackDismiss = { feedbackViewModel.onSheetDismissed() },
                onTabReselected = { index ->
                    scope.launch {
                        if (index == 0) electricityListState.animateScrollToItem(0)
                        else gasListState.animateScrollToItem(0)
                    }
                },
                electricityTabContent = {
                    InvoiceTabContent(
                        uiState = effectiveElectricityState,
                        listState = electricityListState,
                        onFeatureNotAvailable = electricityViewModel::onFeatureNotAvailable,
                        onFilterClick = { showFilter = true },
                        onRefresh = refreshBoth,
                        activeFilterCount = activeFilterCount
                    )
                },
                gasTabContent = {
                    InvoiceTabContent(
                        uiState = gasState,
                        listState = gasListState,
                        onFeatureNotAvailable = gasViewModel::onFeatureNotAvailable,
                        onFilterClick = { showFilter = true },
                        onRefresh = refreshBoth,
                        activeFilterCount = activeFilterCount
                    )
                }
            )
            }
        }

        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = Spacing.dp16)
        ) { data ->
            Snackbar(
                snackbarData = data,
                containerColor = IberdrolaTheme.colors.iberdrolaDarkGreen,
                actionColor = IberdrolaTheme.colors.iberdrolaGreen
            )
        }
    }

    LaunchedEffect(Unit) {
        feedbackViewModel.navigateBack.collect {
            onNavigateBack()
        }
    }

    if (electricityShowDialog || gasShowDialog) {
        AlertDialog(
            onDismissRequest = {
                if (electricityShowDialog) electricityViewModel.onDialogHandled()
                if (gasShowDialog) gasViewModel.onDialogHandled()
            },
            title = { Text(text = stringResource(R.string.dialog_info_title)) },
            text = { Text(text = stringResource(R.string.dialog_feature_not_available)) },
            confirmButton = {
                TextButton(
                    onClick = {
                        if (electricityShowDialog) electricityViewModel.onDialogHandled()
                        if (gasShowDialog) gasViewModel.onDialogHandled()
                    }
                ) {
                    Text(text = stringResource(R.string.dialog_accept))
                }
            }
        )
    }
}
