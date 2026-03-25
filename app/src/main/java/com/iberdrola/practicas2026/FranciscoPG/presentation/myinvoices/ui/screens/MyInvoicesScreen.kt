package com.iberdrola.practicas2026.FranciscoPG.presentation.myinvoices.ui.screens

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.iberdrola.practicas2026.FranciscoPG.R
import com.iberdrola.practicas2026.FranciscoPG.domain.model.InvoiceFilters
import com.iberdrola.practicas2026.FranciscoPG.presentation.myinvoices.ui.showFilterResultSnackbar
import com.iberdrola.practicas2026.FranciscoPG.presentation.myinvoices.ui.showFiltersClearedSnackbar
import com.iberdrola.practicas2026.FranciscoPG.presentation.myinvoices.model.InvoicesUiState
import com.iberdrola.practicas2026.FranciscoPG.presentation.myinvoices.ui.components.UnavailableBanner
import com.iberdrola.practicas2026.FranciscoPG.presentation.myinvoices.viewmodel.FeedbackSheetState
import com.iberdrola.practicas2026.FranciscoPG.presentation.myinvoices.viewmodel.FilterViewModel
import com.iberdrola.practicas2026.FranciscoPG.presentation.myinvoices.viewmodel.InvoicesEvent
import com.iberdrola.practicas2026.FranciscoPG.presentation.theme.IberdrolaTheme
import com.iberdrola.practicas2026.FranciscoPG.presentation.theme.Spacing
import kotlinx.coroutines.launch

@Composable
fun InvoicesScreen(
    uiState: InvoicesUiState,
    feedbackSheetState: FeedbackSheetState,
    filterViewModel: FilterViewModel,
    onEvent: (InvoicesEvent) -> Unit,
    onBackClick: () -> Unit,
    onFeedbackRated: () -> Unit,
    onFeedbackLater: () -> Unit,
    onFeedbackDismiss: () -> Unit,
    onGetFilteredCount: () -> Int,
    onRestoreFilters: (InvoiceFilters, InvoiceFilters) -> Unit
) {
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    val filtersClearedMsg = stringResource(R.string.snackbar_filters_cleared)
    val undoLabel = stringResource(R.string.snackbar_undo)

    var showFilter by remember { mutableStateOf(false) }

    val electricityListState = rememberLazyListState()
    val gasListState = rememberLazyListState()

    // Dismiss snackbar on screen exit
    DisposableEffect(Unit) {
        onDispose { snackbarHostState.currentSnackbarData?.dismiss() }
    }

    // Dismiss snackbar when toggling filter view
    LaunchedEffect(showFilter) {
        snackbarHostState.currentSnackbarData?.dismiss()
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
                val handleBackPress = {
                    filterViewModel.clearFilters()
                    showFilter = false
                }
                BackHandler { handleBackPress() }
                FilterRoute(
                    onBack = { handleBackPress() },
                    filterViewModel = filterViewModel,
                    onFiltersApplied = {
                        scope.showFilterResultSnackbar(snackbarHostState, onGetFilteredCount())
                        showFilter = false
                    },
                    onFiltersCleared = { previousDraft, previousApplied ->
                        scope.showFiltersClearedSnackbar(
                            snackbarHostState = snackbarHostState,
                            message = filtersClearedMsg,
                            undoLabel = undoLabel,
                            onUndo = { onRestoreFilters(previousDraft, previousApplied) }
                        )
                    },
                    onFilterInteraction = {
                        snackbarHostState.currentSnackbarData?.dismiss()
                    }
                )
            } else {
                MyInvoicesComposeScreen(
                    address = stringResource(R.string.my_invoices_mock_address),
                    feedbackSheetState = feedbackSheetState,
                    isGlobalEmpty = uiState.isGlobalEmpty,
                    preferredTabIndex = uiState.preferredTabIndex,
                    onTabChanged = { onEvent(InvoicesEvent.OnTabChanged(it)) },
                    onBackClick = {
                        Log.d("InvoicesScreen", "Back pressed, evaluating feedback")
                        onBackClick()
                    },
                    onFeedbackFaceClick = onFeedbackRated,
                    onFeedbackLaterClick = onFeedbackLater,
                    onFeedbackDismiss = onFeedbackDismiss,
                    onTabReselected = { index ->
                        scope.launch {
                            if (index == 0) electricityListState.animateScrollToItem(0)
                            else gasListState.animateScrollToItem(0)
                        }
                    },
                    electricityTabContent = {
                        InvoiceTabContent(
                            uiState = uiState.electricityState,
                            listState = electricityListState,
                            onFeatureNotAvailable = { onEvent(InvoicesEvent.OnFeatureNotAvailable) },
                            onFilterClick = { showFilter = true },
                            onRefresh = { onEvent(InvoicesEvent.OnRefresh) },
                            activeFilterCount = uiState.activeFilterCount,
                            isFiltered = uiState.isFiltered
                        )
                    },
                    gasTabContent = {
                        InvoiceTabContent(
                            uiState = uiState.gasState,
                            listState = gasListState,
                            onFeatureNotAvailable = { onEvent(InvoicesEvent.OnFeatureNotAvailable) },
                            onFilterClick = { showFilter = true },
                            onRefresh = { onEvent(InvoicesEvent.OnRefresh) },
                            activeFilterCount = uiState.activeFilterCount,
                            isFiltered = uiState.isFiltered
                        )
                    }
                )
            }
        }

        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = Spacing.dp64)
        ) { data ->
            Snackbar(
                snackbarData = data,
                containerColor = IberdrolaTheme.colors.snackbar,
                contentColor = IberdrolaTheme.colors.black,
                actionColor = IberdrolaTheme.colors.black
            )
        }

        UnavailableBanner(
            visible = uiState.showBanner,
            onDismiss = { onEvent(InvoicesEvent.OnBannerDismissed) },
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .navigationBarsPadding()
                .padding(bottom = Spacing.dp32)
        )
    }
}
