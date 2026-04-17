package com.iberdrola.practicas2026.FranciscoPG.presentation.home
import com.iberdrola.practicas2026.FranciscoPG.presentation.R

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import com.iberdrola.practicas2026.FranciscoPG.presentation.home.ui.MainScreen
import com.iberdrola.practicas2026.FranciscoPG.presentation.home.ui.SplashScreen
import com.iberdrola.practicas2026.FranciscoPG.presentation.home.viewmodel.MainViewModel
import com.iberdrola.practicas2026.FranciscoPG.presentation.electronicinvoice.ui.activate.ActivateElectronicInvoiceRoute
import com.iberdrola.practicas2026.FranciscoPG.presentation.electronicinvoice.ui.list.ElectronicInvoiceRoute
import com.iberdrola.practicas2026.FranciscoPG.presentation.electronicinvoice.ui.modify.ModifyEmailScreen
import com.iberdrola.practicas2026.FranciscoPG.presentation.electronicinvoice.ui.modify.ModifyEmailWizardRoute
import com.iberdrola.practicas2026.FranciscoPG.presentation.myinvoices.ui.screens.InvoicesRoute
import com.iberdrola.practicas2026.FranciscoPG.presentation.theme.IberdrolaTheme
import androidx.navigation.NavType
import androidx.navigation.navArgument
import dagger.hilt.android.AndroidEntryPoint
import java.net.URLDecoder
import java.net.URLEncoder

private object AppRoutes {
    const val SPLASH = "splash"
    const val HOME = "home"
    const val MY_INVOICES = "my_invoices"
    const val ELECTRONIC_INVOICE = "electronic_invoice"
    const val ACTIVATE_ELECTRONIC_INVOICE = "activate_electronic_invoice"
    const val MODIFY_EMAIL = "modify_email"
    const val MODIFY_EMAIL_WIZARD = "modify_email_wizard"
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
                val latestInvoiceAmount by viewModel.latestInvoiceAmount.collectAsStateWithLifecycle()
                val isLoadingInvoice by viewModel.isLoadingInvoice.collectAsStateWithLifecycle()

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
                var previousRoute by remember { mutableStateOf(currentRoute) }
                LaunchedEffect(currentRoute) {
                    snackbarHostState.currentSnackbarData?.dismiss()
                    if (currentRoute == AppRoutes.HOME && previousRoute != null
                        && previousRoute != AppRoutes.HOME && previousRoute != AppRoutes.SPLASH
                    ) {
                        viewModel.refreshLatestInvoice()
                    }
                    previousRoute = currentRoute
                }

                NavHost(
                    navController = navController,
                    startDestination = AppRoutes.SPLASH
                ) {

                    composable(
                        AppRoutes.SPLASH,
                        enterTransition = { EnterTransition.None },
                        exitTransition = { ExitTransition.None }
                    ) {
                        SplashScreen(
                            onSplashFinished = {
                                navController.navigate(AppRoutes.HOME) {
                                    popUpTo(AppRoutes.SPLASH) { inclusive = true }
                                }
                            }
                        )
                    }

                    composable(
                        AppRoutes.HOME,
                        enterTransition = {
                            if (initialState.destination.route == AppRoutes.SPLASH) {
                                fadeIn(tween(durationMillis = 600, delayMillis = 200))
                            } else {
                                slideInHorizontally { -it }
                            }
                        },
                        exitTransition = { slideOutHorizontally { -it } },
                        popEnterTransition = { slideInHorizontally { -it } },
                        popExitTransition = { slideOutHorizontally { it } }
                    ) {
                        MainScreen(
                            userName = userName,
                            isMockEnabled = useMock,
                            latestInvoiceAmount = latestInvoiceAmount,
                            isLoadingInvoice = isLoadingInvoice,
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
                            onNavigateBack = {
                                if (navController.previousBackStackEntry != null) {
                                    navController.popBackStack()
                                }
                            }
                        )
                    }

                    composable(
                        AppRoutes.ELECTRONIC_INVOICE,
                        enterTransition = { slideInHorizontally { it } },
                        exitTransition = { slideOutHorizontally { it } },
                        popEnterTransition = { slideInHorizontally { -it } },
                        popExitTransition = { slideOutHorizontally { it } }
                    ) {
                        ElectronicInvoiceRoute(
                            onNavigateBack = {
                                if (navController.previousBackStackEntry != null) {
                                    navController.popBackStack()
                                }
                            },
                            onNavigateToActivate = { supplyType ->
                                navController.navigate("${AppRoutes.ACTIVATE_ELECTRONIC_INVOICE}/$supplyType") {
                                    launchSingleTop = true
                                }
                            },
                            onNavigateToModify = { supplyType, censoredEmail ->
                                val encoded = URLEncoder.encode(censoredEmail, "UTF-8")
                                navController.navigate("${AppRoutes.MODIFY_EMAIL}/$supplyType/$encoded") {
                                    launchSingleTop = true
                                }
                            }
                        )
                    }

                    composable(
                        "${AppRoutes.ACTIVATE_ELECTRONIC_INVOICE}/{supplyType}",
                        arguments = listOf(navArgument("supplyType") { type = NavType.StringType }),
                        enterTransition = { slideInHorizontally { it } },
                        exitTransition = { slideOutHorizontally { it } },
                        popEnterTransition = { slideInHorizontally { -it } },
                        popExitTransition = { slideOutHorizontally { it } }
                    ) {
                        ActivateElectronicInvoiceRoute(
                            onNavigateBack = { navController.popBackStack() }
                        )
                    }

                    composable(
                        "${AppRoutes.MODIFY_EMAIL}/{supplyType}/{censoredEmail}",
                        arguments = listOf(
                            navArgument("supplyType") { type = NavType.StringType },
                            navArgument("censoredEmail") { type = NavType.StringType; defaultValue = "" }
                        ),
                        enterTransition = { slideInHorizontally { it } },
                        exitTransition = { slideOutHorizontally { it } },
                        popEnterTransition = { slideInHorizontally { -it } },
                        popExitTransition = { slideOutHorizontally { it } }
                    ) { entry ->
                        val supplyType = entry.arguments?.getString("supplyType") ?: "LUZ"
                        val initialEmail = URLDecoder.decode(
                            entry.arguments?.getString("censoredEmail") ?: "", "UTF-8"
                        )
                        var currentEmail by remember { mutableStateOf(initialEmail) }
                        val savedEmail = entry.savedStateHandle.get<String>("modified_email")
                        if (savedEmail != null) {
                            currentEmail = savedEmail
                            entry.savedStateHandle.remove<String>("modified_email")
                        }

                        ModifyEmailScreen(
                            currentEmail = currentEmail,
                            onModifyClick = {
                                navController.navigate("${AppRoutes.MODIFY_EMAIL_WIZARD}/$supplyType") {
                                    launchSingleTop = true
                                }
                            },
                            onNavigateBack = { navController.popBackStack() }
                        )
                    }

                    composable(
                        "${AppRoutes.MODIFY_EMAIL_WIZARD}/{supplyType}",
                        arguments = listOf(navArgument("supplyType") { type = NavType.StringType }),
                        enterTransition = { slideInHorizontally { it } },
                        exitTransition = { slideOutHorizontally { it } },
                        popEnterTransition = { slideInHorizontally { -it } },
                        popExitTransition = { slideOutHorizontally { it } }
                    ) {
                        ModifyEmailWizardRoute(
                            onNavigateBack = { navController.popBackStack() },
                            onComplete = { censoredEmail ->
                                navController.previousBackStackEntry
                                    ?.savedStateHandle
                                    ?.set("modified_email", censoredEmail)
                                navController.popBackStack()
                            }
                        )
                    }
                }
            }
        }
    }
}
