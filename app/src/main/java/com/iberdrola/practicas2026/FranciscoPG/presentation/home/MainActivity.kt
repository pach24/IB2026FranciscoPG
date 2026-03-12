package com.iberdrola.practicas2026.FranciscoPG.presentation.home

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import com.iberdrola.practicas2026.FranciscoPG.presentation.theme.IberdrolaTheme
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.iberdrola.practicas2026.FranciscoPG.R
import com.iberdrola.practicas2026.FranciscoPG.presentation.invoices.view.InvoiceListComposeScreen
import com.iberdrola.practicas2026.FranciscoPG.presentation.invoices.view.MyInvoicesComposeScreen
import com.iberdrola.practicas2026.FranciscoPG.presentation.invoices.viewmodel.InvoiceListUiState
import com.iberdrola.practicas2026.FranciscoPG.presentation.invoices.viewmodel.MyInvoicesViewModel
import com.iberdrola.practicas2026.FranciscoPG.presentation.main.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

private object AppRoutes {
    const val HOME = "home"
    const val MY_INVOICES = "my_invoices"
}

private object SupplyType {
    const val LIGHT = "luz"
    const val GAS = "gas"
}

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        setContent {
            IberdrolaTheme {

            val userName by viewModel.userName.observeAsState("FRANCISCO")
            val useMock by viewModel.useMock.observeAsState(true)
            val mockModeChanged by viewModel.mockModeChanged.observeAsState()

            val snackbarHostState = remember { SnackbarHostState() }
            val navController = rememberNavController()

            val mockModeMessage =
                if (useMock) stringResource(R.string.main_mock_activated)
                else stringResource(R.string.main_mock_disabled)

            val snackbarContainer =
                if (useMock) colorResource(R.color.snackbar)
                else colorResource(R.color.iberdrola_green)

            val snackbarContent =
                if (useMock) colorResource(R.color.black)
                else colorResource(R.color.white)

            LaunchedEffect(mockModeChanged) {
                mockModeChanged?.let {
                    snackbarHostState.showSnackbar(
                        message = mockModeMessage,
                        duration = SnackbarDuration.Long
                    )
                }
            }

            NavHost(
                navController = navController,
                startDestination = AppRoutes.HOME
            ) {

                composable(AppRoutes.HOME) {

                    MainScreen(
                        userName = userName,
                        isMockEnabled = useMock,
                        onMockModeChanged = viewModel::updateMockMode,
                        onInvoicesCardClick = {

                            navController.navigate(AppRoutes.MY_INVOICES) {

                                launchSingleTop = true
                                restoreState = true

                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                            }
                        },
                        snackbarHostState = snackbarHostState,
                        snackbarContainerColor = snackbarContainer,
                        snackbarContentColor = snackbarContent
                    )
                }

                composable(AppRoutes.MY_INVOICES) {

                    val lightViewModel: MyInvoicesViewModel =
                        hiltViewModel(key = "light_invoices_vm")

                    val gasViewModel: MyInvoicesViewModel =
                        hiltViewModel(key = "gas_invoices_vm")

                    val lightState by lightViewModel.listUiState
                        .observeAsState(InvoiceListUiState.Loading)

                    val gasState by gasViewModel.listUiState
                        .observeAsState(InvoiceListUiState.Loading)

                    val lightShowDialog by lightViewModel.showDialogEvent.observeAsState(false)
                    val gasShowDialog by gasViewModel.showDialogEvent.observeAsState(false)

                    val scope = rememberCoroutineScope()

                    val lightListState = rememberLazyListState()
                    val gasListState = rememberLazyListState()

                    LaunchedEffect(useMock) {
                        lightViewModel.fetchInvoices(SupplyType.LIGHT, useMock)
                        gasViewModel.fetchInvoices(SupplyType.GAS, useMock)
                    }

                    MyInvoicesComposeScreen(
                        address = stringResource(R.string.my_invoices_mock_address),
                        onBackClick = { navController.popBackStack() },
                        onTabReselected = { index ->
                            scope.launch {
                                if (index == 0) {
                                    lightListState.animateScrollToItem(0)
                                } else {
                                    gasListState.animateScrollToItem(0)
                                }
                            }
                        },
                        lightTabContent = {

                            InvoiceTabContent(
                                uiState = lightState,
                                listState = lightListState,
                                onFeatureNotAvailable = lightViewModel::onFeatureNotAvailable,
                                onRefresh = { lightViewModel.fetchInvoices(SupplyType.LIGHT, useMock) }
                            )
                        },
                        gasTabContent = {

                            InvoiceTabContent(
                                uiState = gasState,
                                listState = gasListState,
                                onFeatureNotAvailable = gasViewModel::onFeatureNotAvailable,
                                onRefresh = { gasViewModel.fetchInvoices(SupplyType.GAS, useMock) }
                            )
                        }
                    )

                    if (lightShowDialog || gasShowDialog) {

                        AlertDialog(
                            onDismissRequest = {

                                if (lightShowDialog) lightViewModel.onDialogHandled()
                                if (gasShowDialog) gasViewModel.onDialogHandled()
                            },
                            title = { Text("Informacion") },
                            text = { Text("Esta funcionalidad aun no esta disponible.") },
                            confirmButton = {

                                TextButton(
                                    onClick = {
                                        if (lightShowDialog) lightViewModel.onDialogHandled()
                                        if (gasShowDialog) gasViewModel.onDialogHandled()
                                    }
                                ) {
                                    Text("Aceptar")
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun InvoiceTabContent(
    uiState: InvoiceListUiState,
    listState: androidx.compose.foundation.lazy.LazyListState,
    onFeatureNotAvailable: () -> Unit,
    onRefresh: () -> Unit
) {
    var lastSuccess by remember { mutableStateOf<InvoiceListUiState.Success?>(null) }

    LaunchedEffect(uiState) {
        if (uiState is InvoiceListUiState.Success) {
            lastSuccess = uiState
        }
    }

    val isRefreshing = uiState is InvoiceListUiState.Loading && lastSuccess != null

    when (uiState) {
        is InvoiceListUiState.Loading -> {
            val cached = lastSuccess

            if (cached == null) {
                InvoiceListComposeScreen(
                    isLoading = true,
                    isRefreshing = false,
                    latestInvoiceAmount = "",
                    latestInvoiceDateRange = "",
                    latestInvoiceType = "",
                    latestInvoiceStatus = "",
                    latestInvoiceIsPaid = false,
                    latestInvoiceIconRes = R.drawable.ic_light,
                    historyItems = emptyList(),
                    listState = listState,
                    onLatestInvoiceClick = onFeatureNotAvailable,
                    onFilterClick = onFeatureNotAvailable,
                    onHistoryItemClick = { onFeatureNotAvailable() },
                    onRefresh = onRefresh
                )
            } else {
                InvoiceListComposeScreen(
                    isLoading = false,
                    isRefreshing = true,
                    latestInvoiceAmount = cached.latestInvoice?.amount ?: "",
                    latestInvoiceDateRange = cached.latestInvoice?.dateRange ?: "",
                    latestInvoiceType = cached.latestInvoice?.supplyTypeLabel ?: "",
                    latestInvoiceStatus = cached.latestInvoice?.status ?: "",
                    latestInvoiceIsPaid = cached.latestInvoice?.isPaid ?: false,
                    latestInvoiceIconRes = cached.latestInvoice?.iconRes ?: R.drawable.ic_light,
                    historyItems = cached.historyItems,
                    listState = listState,
                    onLatestInvoiceClick = onFeatureNotAvailable,
                    onFilterClick = onFeatureNotAvailable,
                    onHistoryItemClick = { onFeatureNotAvailable() },
                    onRefresh = onRefresh
                )
            }
        }

        is InvoiceListUiState.Error -> {
            InvoiceListComposeScreen(
                isLoading = false,
                isRefreshing = false,
                latestInvoiceAmount = "",
                latestInvoiceDateRange = uiState.message,
                latestInvoiceType = "",
                latestInvoiceStatus = "",
                latestInvoiceIsPaid = false,
                latestInvoiceIconRes = R.drawable.ic_light,
                historyItems = emptyList(),
                listState = listState,
                onLatestInvoiceClick = onFeatureNotAvailable,
                onFilterClick = onFeatureNotAvailable,
                onHistoryItemClick = { onFeatureNotAvailable() },
                onRefresh = onRefresh
            )
        }

        is InvoiceListUiState.Success -> {
            val latestInvoice = uiState.latestInvoice

            InvoiceListComposeScreen(
                isLoading = false,
                isRefreshing = false,
                latestInvoiceAmount = latestInvoice?.amount ?: "",
                latestInvoiceDateRange = latestInvoice?.dateRange ?: "",
                latestInvoiceType = latestInvoice?.supplyTypeLabel ?: "",
                latestInvoiceStatus = latestInvoice?.status ?: "",
                latestInvoiceIsPaid = latestInvoice?.isPaid ?: false,
                latestInvoiceIconRes = latestInvoice?.iconRes ?: R.drawable.ic_light,
                historyItems = uiState.historyItems,
                listState = listState,
                onLatestInvoiceClick = onFeatureNotAvailable,
                onFilterClick = onFeatureNotAvailable,
                onHistoryItemClick = { onFeatureNotAvailable() },
                onRefresh = onRefresh
            )
        }
    }
}
}









