package com.iberdrola.practicas2026.FranciscoPG.presentation.invoices.ui.screens

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import com.iberdrola.practicas2026.FranciscoPG.R
import com.iberdrola.practicas2026.FranciscoPG.presentation.invoices.ui.components.EmptyStateComposable
import com.iberdrola.practicas2026.FranciscoPG.presentation.invoices.ui.components.ErrorStateComposable
import com.iberdrola.practicas2026.FranciscoPG.presentation.invoices.viewmodel.InvoiceListUiState

@Composable
fun InvoiceTabContent(
    uiState: InvoiceListUiState,
    listState: LazyListState,
    onFeatureNotAvailable: () -> Unit,
    onRefresh: () -> Unit
) {
    var lastSuccess by remember { mutableStateOf<InvoiceListUiState.Success?>(null) }

    LaunchedEffect(uiState) {
        if (uiState is InvoiceListUiState.Success) {
            lastSuccess = uiState
        }
    }

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

        is InvoiceListUiState.Empty -> {
            EmptyStateComposable(
                title = stringResource(R.string.empty_state_tab_title),
                subtitle = stringResource(R.string.empty_state_tab_subtitle)
            )
        }

        is InvoiceListUiState.ServerError -> {
            ErrorStateComposable(
                title = stringResource(R.string.error_server_title),
                subtitle = stringResource(R.string.error_server_subtitle),
                iconRes = R.drawable.ic_server_off,
                onRetryClick = onRefresh
            )
        }

        is InvoiceListUiState.ConnectionError -> {
            ErrorStateComposable(
                title = stringResource(R.string.error_connection_title),
                subtitle = stringResource(R.string.error_connection_subtitle),
                iconRes = R.drawable.ic_connection_error,
                onRetryClick = onRefresh
            )
        }

        is InvoiceListUiState.Success -> {
            InvoiceListComposeScreen(
                isLoading = false,
                isRefreshing = false,
                latestInvoiceAmount = uiState.latestInvoice?.amount ?: "",
                latestInvoiceDateRange = uiState.latestInvoice?.dateRange ?: "",
                latestInvoiceType = uiState.latestInvoice?.supplyTypeLabel ?: "",
                latestInvoiceStatus = uiState.latestInvoice?.status ?: "",
                latestInvoiceIsPaid = uiState.latestInvoice?.isPaid ?: false,
                latestInvoiceIconRes = uiState.latestInvoice?.iconRes ?: R.drawable.ic_light,
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
