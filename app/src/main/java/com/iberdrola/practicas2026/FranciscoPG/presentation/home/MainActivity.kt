package com.iberdrola.practicas2026.FranciscoPG.presentation.home

import android.os.Bundle
import android.util.Log
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.iberdrola.practicas2026.FranciscoPG.R
import com.iberdrola.practicas2026.FranciscoPG.presentation.home.ui.MainScreen
import com.iberdrola.practicas2026.FranciscoPG.presentation.home.viewmodel.MainViewModel
import com.iberdrola.practicas2026.FranciscoPG.presentation.invoices.ui.screens.InvoiceTabContent
import com.iberdrola.practicas2026.FranciscoPG.presentation.invoices.ui.screens.MyInvoicesComposeScreen
import com.iberdrola.practicas2026.FranciscoPG.presentation.invoices.viewmodel.FeedbackViewModel
import com.iberdrola.practicas2026.FranciscoPG.presentation.invoices.viewmodel.InvoiceListUiState
import com.iberdrola.practicas2026.FranciscoPG.domain.model.SupplyType
import com.iberdrola.practicas2026.FranciscoPG.presentation.invoices.viewmodel.MyInvoicesViewModel
import com.iberdrola.practicas2026.FranciscoPG.presentation.theme.IberdrolaTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

private object AppRoutes {
    const val HOME = "home"
    const val MY_INVOICES = "my_invoices"
}

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            IberdrolaTheme {

                val userName by viewModel.userName.collectAsStateWithLifecycle()
                val useMock by viewModel.useMock.collectAsStateWithLifecycle()
                val mockModeChanged by viewModel.mockModeChanged.collectAsStateWithLifecycle()

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
                            duration = SnackbarDuration.Short
                        )
                        viewModel.onMockModeEventConsumed()
                    }
                }

                // Descartar snackbar al cambiar de pantalla
                val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route
                LaunchedEffect(currentRoute) {
                    snackbarHostState.currentSnackbarData?.dismiss()
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
                        InvoicesRoute(useMock = useMock, navController = navController)
                    }
                }
            }
        }
    }
}

// ── Ruta de facturas (extraída de MainActivity para SRP) ─────────────────────

@Composable
private fun InvoicesRoute(
    useMock: Boolean,
    navController: NavController
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

    // Auto-switch de tab:
    // Si Electricidad no tiene facturas pero Gas sí, se cambia
    // automáticamente al tab de Gas. Mientras tanto, Electricidad
    // muestra shimmer para evitar un flash de Empty State.
    // Una vez completado el auto-switch, si el usuario vuelve
    // al tab de Electricidad manualmente, verá el Empty State real.
    // Y viceversa
    // Auto-switch de tab (una sola vez al entrar):
    // Si Electricidad no tiene facturas pero Gas sí, se cambia
    // automáticamente al tab de Gas. hasAutoSwitched se activa
    // de forma síncrona para que preferredTabIndex sea 1 desde
    // la primera composición, evitando flash de Empty State.
    // scrollCompleted se activa tras el scroll real, controlando
    // el shimmer en el tab de Electricidad mientras tanto.
    // En refreshes posteriores, hasAutoSwitched impide re-disparar.
    var hasAutoSwitched by remember { mutableStateOf(false) }
    var scrollCompleted by remember { mutableStateOf(false) }

    val wantsAutoSwitch = bothLoaded
            && electricityState is InvoiceListUiState.Empty
            && gasState !is InvoiceListUiState.Empty

    if (wantsAutoSwitch) hasAutoSwitched = true

    val preferredTabIndex = if (hasAutoSwitched) 1 else 0

    LaunchedEffect(preferredTabIndex) {
        if (preferredTabIndex == 1) scrollCompleted = true
    }

    val effectiveElectricityState = if (
        electricityState is InvoiceListUiState.Empty
        && !isGlobalEmpty
        && !scrollCompleted
    ) InvoiceListUiState.Loading else electricityState

    MyInvoicesComposeScreen(
        address = stringResource(R.string.my_invoices_mock_address),
        feedbackSheetState = sheetState,
        isGlobalEmpty = isGlobalEmpty,
        preferredTabIndex = preferredTabIndex,
        onBackClick = {
            Log.d("Feedback", "Back pulsado en facturas -> evaluando feedback")
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
                onRefresh = {
                    electricityViewModel.fetchInvoices(SupplyType.ELECTRICITY, useMock, forceRefresh = true)
                    gasViewModel.fetchInvoices(SupplyType.GAS, useMock, forceRefresh = true)
                }
            )
        },
        gasTabContent = {
            InvoiceTabContent(
                uiState = gasState,
                listState = gasListState,
                onFeatureNotAvailable = gasViewModel::onFeatureNotAvailable,
                onRefresh = {
                    electricityViewModel.fetchInvoices(SupplyType.ELECTRICITY, useMock, forceRefresh = true)
                    gasViewModel.fetchInvoices(SupplyType.GAS, useMock, forceRefresh = true)
                }
            )
        }
    )

    LaunchedEffect(Unit) {
        feedbackViewModel.navigateBack.collect {
            navController.popBackStack()
        }
    }

    if (electricityShowDialog || gasShowDialog) {
        AlertDialog(
            onDismissRequest = {
                if (electricityShowDialog) electricityViewModel.onDialogHandled()
                if (gasShowDialog) gasViewModel.onDialogHandled()
            },
            title = { Text(text = "Informacion") },
            text = { Text(text = "Esta funcionalidad aun no esta disponible.") },
            confirmButton = {
                TextButton(
                    onClick = {
                        if (electricityShowDialog) electricityViewModel.onDialogHandled()
                        if (gasShowDialog) gasViewModel.onDialogHandled()
                    }
                ) {
                    Text(text = "Aceptar")
                }
            }
        )
    }
}
