package com.iberdrola.practicas2026.FranciscoPG.presentation.invoices.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.PullToRefreshDefaults
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.iberdrola.practicas2026.FranciscoPG.R
import com.iberdrola.practicas2026.FranciscoPG.presentation.invoices.ui.components.EmptyStateComposable
import com.iberdrola.practicas2026.FranciscoPG.presentation.invoices.ui.components.ErrorStateComposable
import com.iberdrola.practicas2026.FranciscoPG.presentation.invoices.ui.components.StickyInvoiceHeaderComposable
import com.iberdrola.practicas2026.FranciscoPG.presentation.invoices.model.InvoiceListUiState
import com.iberdrola.practicas2026.FranciscoPG.presentation.theme.IberdrolaTheme
import com.iberdrola.practicas2026.FranciscoPG.presentation.theme.Spacing

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun InvoiceTabContent(
    uiState: InvoiceListUiState,
    listState: LazyListState,
    onFeatureNotAvailable: () -> Unit,
    onFilterClick: () -> Unit,
    onRefresh: () -> Unit,
    activeFilterCount: Int = 0
) {
    var lastContentState by remember { mutableStateOf<InvoiceListUiState?>(null) }

    LaunchedEffect(uiState) {
        if (uiState is InvoiceListUiState.Success || uiState is InvoiceListUiState.FilteredEmpty) {
            lastContentState = uiState
        }
    }

    when (uiState) {
        is InvoiceListUiState.Loading -> {
            val cached = lastContentState
            if (cached == null) {
                InvoiceListComposeScreen(
                    isLoading = true,
                    isRefreshing = false,
                    latestInvoice = null,
                    historyItems = emptyList(),
                    listState = listState,
                    onLatestInvoiceClick = {},
                    onFilterClick = {},
                    onHistoryItemClick = {},
                    onRefresh = onRefresh
                )
            } else when (cached) {
                is InvoiceListUiState.Success -> {
                    InvoiceListComposeScreen(
                        isLoading = false,
                        isRefreshing = true,
                        latestInvoice = cached.latestInvoice,
                        historyItems = cached.historyItems,
                        listState = listState,
                        onLatestInvoiceClick = onFeatureNotAvailable,
                        onFilterClick = onFilterClick,
                        onHistoryItemClick = { onFeatureNotAvailable() },
                        onRefresh = onRefresh,
                        activeFilterCount = activeFilterCount
                    )
                }
                is InvoiceListUiState.FilteredEmpty -> {
                    val pullState = rememberPullToRefreshState()
                    PullToRefreshBox(
                        state = pullState,
                        isRefreshing = true,
                        onRefresh = onRefresh,
                        modifier = Modifier.fillMaxSize(),
                        indicator = {
                            PullToRefreshDefaults.LoadingIndicator(
                                state = pullState,
                                isRefreshing = true,
                                color = MaterialTheme.colorScheme.primary,
                                containerColor = MaterialTheme.colorScheme.surface,
                                modifier = Modifier.align(Alignment.TopCenter)
                            )
                        }
                    ) {
                        Column(modifier = Modifier.fillMaxSize()) {
                            StickyInvoiceHeaderComposable(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(IberdrolaTheme.colors.background)
                                    .padding(vertical = Spacing.dp8),
                                activeFilterCount = activeFilterCount,
                                onFilterClick = onFilterClick
                            )
                            EmptyStateComposable(
                                title = stringResource(R.string.empty_state_filtered_title),
                                subtitle = stringResource(R.string.empty_state_filtered_subtitle)
                            )
                        }
                    }
                }
                else -> {}
            }
        }

        is InvoiceListUiState.Empty -> {
            val pullState = rememberPullToRefreshState()
            PullToRefreshBox(
                state = pullState,
                isRefreshing = false,
                onRefresh = onRefresh,
                modifier = Modifier.fillMaxSize(),
                indicator = {
                    PullToRefreshDefaults.LoadingIndicator(
                        state = pullState,
                        isRefreshing = false,
                        color = MaterialTheme.colorScheme.primary,
                        containerColor = MaterialTheme.colorScheme.surface,
                        modifier = Modifier.align(Alignment.TopCenter)
                    )
                }
            ) {
                EmptyStateComposable(
                    title = stringResource(R.string.empty_state_tab_title),
                    subtitle = stringResource(R.string.empty_state_tab_subtitle)
                )
            }
        }

        // Hay facturas pero los filtros las excluyen todas: sticky header + filtro + empty state
        is InvoiceListUiState.FilteredEmpty -> {
            val pullState = rememberPullToRefreshState()
            PullToRefreshBox(
                state = pullState,
                isRefreshing = false,
                onRefresh = onRefresh,
                modifier = Modifier.fillMaxSize(),
                indicator = {
                    PullToRefreshDefaults.LoadingIndicator(
                        state = pullState,
                        isRefreshing = false,
                        color = MaterialTheme.colorScheme.primary,
                        containerColor = MaterialTheme.colorScheme.surface,
                        modifier = Modifier.align(Alignment.TopCenter)
                    )
                }
            ) {
                Column(modifier = Modifier.fillMaxSize()) {
                    StickyInvoiceHeaderComposable(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(IberdrolaTheme.colors.background)
                            .padding(vertical = Spacing.dp8),
                        activeFilterCount = activeFilterCount,
                        onFilterClick = onFilterClick
                    )
                    EmptyStateComposable(
                        title = stringResource(R.string.empty_state_filtered_title),
                        subtitle = stringResource(R.string.empty_state_filtered_subtitle)
                    )
                }
            }
        }

        is InvoiceListUiState.ServerError -> {
            val pullState = rememberPullToRefreshState()
            PullToRefreshBox(
                state = pullState,
                isRefreshing = false,
                onRefresh = onRefresh,
                modifier = Modifier.fillMaxSize(),
                indicator = {
                    PullToRefreshDefaults.LoadingIndicator(
                        state = pullState,
                        isRefreshing = false,
                        color = MaterialTheme.colorScheme.primary,
                        containerColor = MaterialTheme.colorScheme.surface,
                        modifier = Modifier.align(Alignment.TopCenter)
                    )
                }
            ) {
                ErrorStateComposable(
                    title = stringResource(R.string.error_server_title),
                    subtitle = stringResource(R.string.error_server_subtitle),
                    iconRes = R.drawable.ic_server_off,
                    onRetryClick = onRefresh
                )
            }
        }

        is InvoiceListUiState.ConnectionError -> {
            val pullState = rememberPullToRefreshState()
            PullToRefreshBox(
                state = pullState,
                isRefreshing = false,
                onRefresh = onRefresh,
                modifier = Modifier.fillMaxSize(),
                indicator = {
                    PullToRefreshDefaults.LoadingIndicator(
                        state = pullState,
                        isRefreshing = false,
                        color = MaterialTheme.colorScheme.primary,
                        containerColor = MaterialTheme.colorScheme.surface,
                        modifier = Modifier.align(Alignment.TopCenter)
                    )
                }
            ) {
                ErrorStateComposable(
                    title = stringResource(R.string.error_connection_title),
                    subtitle = stringResource(R.string.error_connection_subtitle),
                    iconRes = R.drawable.ic_connection_error,
                    onRetryClick = onRefresh
                )
            }
        }

        is InvoiceListUiState.Success -> {
            InvoiceListComposeScreen(
                isLoading = false,
                isRefreshing = false,
                latestInvoice = uiState.latestInvoice,
                historyItems = uiState.historyItems,
                listState = listState,
                onLatestInvoiceClick = onFeatureNotAvailable,
                onFilterClick = onFilterClick,
                onHistoryItemClick = { onFeatureNotAvailable() },
                onRefresh = onRefresh,
                activeFilterCount = activeFilterCount
            )
        }
    }
}
