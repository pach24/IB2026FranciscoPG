package com.iberdrola.practicas2026.FranciscoPG.presentation.home


import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.core.view.WindowInsetsControllerCompat
import com.iberdrola.practicas2026.FranciscoPG.R
import com.iberdrola.practicas2026.FranciscoPG.presentation.main.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.iberdrola.practicas2026.FranciscoPG.presentation.invoices.model.InvoiceListItem
import com.iberdrola.practicas2026.FranciscoPG.presentation.invoices.view.InvoiceListComposeScreen
import com.iberdrola.practicas2026.FranciscoPG.presentation.invoices.view.MyInvoicesComposeScreen


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

        WindowInsetsControllerCompat(window, window.decorView)
            .isAppearanceLightStatusBars = false

        setContent {
            val userName by viewModel.userName.observeAsState("FRANCISCO")
            val useMock by viewModel.useMock.observeAsState(true)
            val mockModeChanged by viewModel.mockModeChanged.observeAsState()
            val snackbarHostState = remember { SnackbarHostState() }
            val navController = rememberNavController()

            val mockModeMessage = if (useMock) {
                stringResource(R.string.main_mock_activated)
            } else {
                stringResource(R.string.main_mock_disabled)
            }
            val snackbarContainer = if (useMock) {
                colorResource(R.color.snackbar)
            } else {
                colorResource(R.color.iberdrola_green)
            }
            val snackbarContent = if (useMock) {
                colorResource(R.color.black)
            } else {
                colorResource(R.color.white)
            }

            LaunchedEffect(mockModeChanged) {
                if (mockModeChanged != null) {
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
                    MyInvoicesComposeScreen(
                        address = stringResource(R.string.my_invoices_mock_address),
                        onBackClick = { navController.popBackStack() },
                        lightTabContent = {
                            InvoiceListComposeScreen(
                                isLoading = false,
                                latestInvoiceAmount = "20,00 €",
                                latestInvoiceDateRange = "01 feb. 2024 - 04 mar. 2024",
                                latestInvoiceType = "Factura Luz",
                                latestInvoiceStatus = "Pendiente de Pago",
                                historyItems = sampleInvoices("Factura Luz"),
                                onLatestInvoiceClick = {},
                                onFilterClick = {},
                                onHistoryItemClick = {}
                            )
                        },
                        gasTabContent = {
                            InvoiceListComposeScreen(
                                isLoading = false,
                                latestInvoiceAmount = "34,50 €",
                                latestInvoiceDateRange = "11 feb. 2024 - 10 mar. 2024",
                                latestInvoiceType = "Factura Gas",
                                latestInvoiceStatus = "Pendiente de Pago",
                                historyItems = sampleInvoices("Factura Gas"),
                                onLatestInvoiceClick = {},
                                onFilterClick = {},
                                onHistoryItemClick = {}
                            )
                        }
                    )
                }
            }

        }

         // <--- Notice that setContent is now properly closed down here!
    }
}
private fun sampleInvoices(type: String): List<InvoiceListItem> = listOf(
    InvoiceListItem.HeaderYear("2024"),
    InvoiceListItem.InvoiceItem(
        id = "inv-1-$type",
        date = "8 de marzo",
        type = type,
        amount = "20,00 €",
        statusText = "Pendiente de Pago",
        isPaid = false
    ),
    InvoiceListItem.InvoiceItem(
        id = "inv-2-$type",
        date = "1 de febrero",
        type = type,
        amount = "54,21 €",
        statusText = "Pagada",
        isPaid = true
    )
)