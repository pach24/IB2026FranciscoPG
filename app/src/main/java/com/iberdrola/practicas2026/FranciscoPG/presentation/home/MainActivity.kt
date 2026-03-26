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
import androidx.compose.runtime.remember
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.iberdrola.practicas2026.FranciscoPG.R
import com.iberdrola.practicas2026.FranciscoPG.presentation.home.ui.MainScreen
import com.iberdrola.practicas2026.FranciscoPG.presentation.home.viewmodel.MainViewModel
import com.iberdrola.practicas2026.FranciscoPG.presentation.electronicinvoice.ui.ElectronicInvoiceScreen
import com.iberdrola.practicas2026.FranciscoPG.presentation.myinvoices.ui.screens.InvoicesRoute
import com.iberdrola.practicas2026.FranciscoPG.presentation.theme.IberdrolaTheme
import dagger.hilt.android.AndroidEntryPoint

private object AppRoutes {
    const val HOME = "home"
    const val MY_INVOICES = "my_invoices"
    const val ELECTRONIC_INVOICE = "electronic_invoice"
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
                    if (useMock) IberdrolaTheme.colors.snackbar
                    else IberdrolaTheme.colors.snackbarGreen

                val snackbarContent =
                    if (useMock) IberdrolaTheme.colors.black
                    else IberdrolaTheme.colors.black

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

                    composable(
                        AppRoutes.HOME,
                        enterTransition = { slideInHorizontally { -it } },
                        exitTransition = { slideOutHorizontally { -it } },
                        popEnterTransition = { slideInHorizontally { -it } },
                        popExitTransition = { slideOutHorizontally { it } }
                    ) {
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
                            onElectronicInvoiceClick = {
                                navController.navigate(AppRoutes.ELECTRONIC_INVOICE) {
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

                    composable(
                        AppRoutes.MY_INVOICES,
                        enterTransition = { slideInHorizontally { it } },
                        exitTransition = { slideOutHorizontally { it } },
                        popEnterTransition = { slideInHorizontally { -it } },
                        popExitTransition = { slideOutHorizontally { it } }
                    ) {
                        InvoicesRoute(
                            useMock = useMock,
                            onNavigateBack = { navController.popBackStack() }
                        )
                    }

                    composable(
                        AppRoutes.ELECTRONIC_INVOICE,
                        enterTransition = { slideInHorizontally { it } },
                        exitTransition = { slideOutHorizontally { it } },
                        popEnterTransition = { slideInHorizontally { -it } },
                        popExitTransition = { slideOutHorizontally { it } }
                    ) {
                        ElectronicInvoiceScreen(
                            onNavigateBack = { navController.popBackStack() }
                        )
                    }
                }
            }
        }
    }
}
