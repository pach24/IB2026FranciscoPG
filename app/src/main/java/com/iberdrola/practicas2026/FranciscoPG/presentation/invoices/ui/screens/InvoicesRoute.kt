package com.iberdrola.practicas2026.FranciscoPG.presentation.invoices.ui.screens

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.iberdrola.practicas2026.FranciscoPG.R
import com.iberdrola.practicas2026.FranciscoPG.domain.model.SupplyType
import com.iberdrola.practicas2026.FranciscoPG.presentation.invoices.viewmodel.FeedbackViewModel
import com.iberdrola.practicas2026.FranciscoPG.presentation.invoices.viewmodel.InvoiceListUiState
import com.iberdrola.practicas2026.FranciscoPG.presentation.invoices.viewmodel.MyInvoicesViewModel
import kotlinx.coroutines.launch

/**
 * Determina el tab inicial preferido en función del estado de cada suministro.
 * Devuelve 1 (Gas) solo si ambos han cargado, Electricidad está vacía y Gas no.
 */
fun resolvePreferredTabIndex(
    electricityState: InvoiceListUiState,
    gasState: InvoiceListUiState,
    bothLoaded: Boolean
): Int {
    val wantsAutoSwitch = bothLoaded
            && electricityState is InvoiceListUiState.Empty
            && gasState !is InvoiceListUiState.Empty
    return if (wantsAutoSwitch) 1 else 0
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
    val feedbackViewModel: FeedbackViewModel = hiltViewModel()

    val electricityState by electricityViewModel.listUiState.collectAsStateWithLifecycle()
    val gasState by gasViewModel.listUiState.collectAsStateWithLifecycle()
    val electricityShowDialog by electricityViewModel.showDialogEvent.collectAsStateWithLifecycle()
    val gasShowDialog by gasViewModel.showDialogEvent.collectAsStateWithLifecycle()
    val sheetState by feedbackViewModel.sheetState.collectAsStateWithLifecycle()

    val scope = rememberCoroutineScope()
    val electricityListState = rememberLazyListState()
    val gasListState = rememberLazyListState()

    // Filter screen state: null = list, 0 = electricity filter, 1 = gas filter
    var showFilterForTab by remember { mutableStateOf<Int?>(null) }

    // Recargar facturas al entrar o al cambiar de modo (mock/retrofit)
    var previousMock by remember { mutableStateOf(useMock) }
    LaunchedEffect(useMock) {
        val modeChanged = useMock != previousMock
        previousMock = useMock
        electricityViewModel.fetchInvoices(SupplyType.ELECTRICITY, useMock, forceRefresh = modeChanged)
        gasViewModel.fetchInvoices(SupplyType.GAS, useMock, forceRefresh = modeChanged)
    }

    // Determinar si ambos tabs han terminado de cargar
    val bothLoaded = electricityState !is InvoiceListUiState.Loading
            && gasState !is InvoiceListUiState.Loading

    // Empty state global: ambos tabs terminaron y ninguno tiene facturas
    val isGlobalEmpty = bothLoaded
            && electricityState is InvoiceListUiState.Empty
            && gasState is InvoiceListUiState.Empty

    // Auto-switch de tab (una sola vez al entrar):
    var hasAutoSwitched by remember { mutableStateOf(false) }
    var scrollCompleted by remember { mutableStateOf(false) }

    if (resolvePreferredTabIndex(electricityState, gasState, bothLoaded) == 1) {
        hasAutoSwitched = true
    }

    val preferredTabIndex = if (hasAutoSwitched) 1 else 0

    LaunchedEffect(preferredTabIndex) {
        if (preferredTabIndex == 1) scrollCompleted = true
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

    // Track active tab for filter
    var activeTab by remember { mutableIntStateOf(0) }

    AnimatedContent(
        targetState = showFilterForTab,
        transitionSpec = {
            if (targetState != null) {
                slideInHorizontally { it } togetherWith slideOutHorizontally { -it }
            } else {
                slideInHorizontally { -it } togetherWith slideOutHorizontally { it }
            }
        },
        label = "filter_transition"
    ) { filterTab ->
        if (filterTab != null) {
            val activeVm = if (filterTab == 0) electricityViewModel else gasViewModel
            BackHandler { showFilterForTab = null }
            FilterRoute(
                tab = filterTab,
                onBack = { showFilterForTab = null },
                viewModel = activeVm
            )
        } else {
            MyInvoicesComposeScreen(
                address = stringResource(R.string.my_invoices_mock_address),
                feedbackSheetState = sheetState,
                isGlobalEmpty = isGlobalEmpty,
                preferredTabIndex = preferredTabIndex,
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
                        onFilterClick = { showFilterForTab = 0 },
                        onRefresh = refreshBoth
                    )
                },
                gasTabContent = {
                    InvoiceTabContent(
                        uiState = gasState,
                        listState = gasListState,
                        onFeatureNotAvailable = gasViewModel::onFeatureNotAvailable,
                        onFilterClick = { showFilterForTab = 1 },
                        onRefresh = refreshBoth
                    )
                }
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
