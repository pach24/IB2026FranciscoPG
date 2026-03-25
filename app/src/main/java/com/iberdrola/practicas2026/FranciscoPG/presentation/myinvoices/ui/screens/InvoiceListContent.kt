package com.iberdrola.practicas2026.FranciscoPG.presentation.myinvoices.ui.screens

import android.content.res.Configuration
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.PullToRefreshDefaults
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.tooling.preview.Preview
import com.iberdrola.practicas2026.FranciscoPG.R
import com.iberdrola.practicas2026.FranciscoPG.domain.model.InvoiceStatus
import com.iberdrola.practicas2026.FranciscoPG.presentation.myinvoices.model.InvoiceListItem
import com.iberdrola.practicas2026.FranciscoPG.presentation.myinvoices.model.LatestInvoiceUiModel
import androidx.compose.foundation.layout.Box
import com.iberdrola.practicas2026.FranciscoPG.presentation.myinvoices.ui.components.list.InvoiceHeaderItemComposable
import com.iberdrola.practicas2026.FranciscoPG.presentation.myinvoices.ui.components.list.InvoiceRowItemComposable
import com.iberdrola.practicas2026.FranciscoPG.presentation.myinvoices.ui.components.list.LatestInvoiceCardComposable
import com.iberdrola.practicas2026.FranciscoPG.presentation.myinvoices.ui.components.ShimmerHost
import com.iberdrola.practicas2026.FranciscoPG.presentation.myinvoices.ui.components.list.SkeletonInvoiceHeaderItemComposable
import com.iberdrola.practicas2026.FranciscoPG.presentation.myinvoices.ui.components.list.SkeletonInvoiceRowItemComposable
import com.iberdrola.practicas2026.FranciscoPG.presentation.myinvoices.ui.components.list.SkeletonLatestInvoiceCardComposable
import com.iberdrola.practicas2026.FranciscoPG.presentation.myinvoices.ui.components.list.SkeletonStickyInvoiceHeaderComposable
import com.iberdrola.practicas2026.FranciscoPG.presentation.myinvoices.ui.components.list.StickyInvoiceHeaderComposable
import com.iberdrola.practicas2026.FranciscoPG.presentation.theme.IberdrolaTheme
import com.iberdrola.practicas2026.FranciscoPG.presentation.theme.Spacing

@OptIn(
    ExperimentalFoundationApi::class,
    ExperimentalMaterial3Api::class,
    ExperimentalMaterial3ExpressiveApi::class
)
@Composable
fun InvoiceListComposeScreen(
    isLoading: Boolean,
    isRefreshing: Boolean,
    latestInvoice: LatestInvoiceUiModel?,
    historyItems: List<InvoiceListItem>,
    onLatestInvoiceClick: () -> Unit,
    onFilterClick: () -> Unit,
    onHistoryItemClick: (InvoiceListItem.InvoiceItem) -> Unit,
    onRefresh: () -> Unit,
    modifier: Modifier = Modifier,
    activeFilterCount: Int = 0,
    isFiltered: Boolean = false,
    listState: LazyListState = rememberLazyListState()
) {
    val pullToRefreshState = rememberPullToRefreshState()

    PullToRefreshBox(
        state = pullToRefreshState,
        isRefreshing = isRefreshing,
        onRefresh = onRefresh,
        modifier = modifier.fillMaxSize(),
        indicator = {
            PullToRefreshDefaults.LoadingIndicator(
                state = pullToRefreshState,
                isRefreshing = isRefreshing,
                color = MaterialTheme.colorScheme.primary,
                containerColor = MaterialTheme.colorScheme.surface,
                modifier = Modifier.align(Alignment.TopCenter)
            )
        }
    ) {
        if (isLoading) {
            ShimmerHost {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(bottom = Spacing.dp32)
                ) {
                    // Skeleton: Última factura (card)
                    item {
                        Spacer(modifier = Modifier.height(Spacing.dp18))
                        SkeletonLatestInvoiceCardComposable(
                            modifier = Modifier.padding(horizontal = Spacing.dp24)
                        )
                    }

                    // Skeleton: Sticky header (histórico de facturas + botón filtro)
                    item { SkeletonStickyInvoiceHeaderComposable() }

                    // Skeleton: Cabecera de año ("2024", "2023"...)
                    item { SkeletonInvoiceHeaderItemComposable() }

                    // Skeleton: Filas de facturas
                    items(6) { SkeletonInvoiceRowItemComposable() }
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                state = listState,
                contentPadding = PaddingValues(bottom = Spacing.dp32)
            ) {
                // Última factura (card)
                item {
                    Spacer(modifier = Modifier.height(Spacing.dp18))
                    if (latestInvoice != null) {
                        LatestInvoiceCardComposable(
                            amount = latestInvoice.amount,
                            dateRange = latestInvoice.dateRange,
                            supplyType = latestInvoice.supplyTypeLabel,
                            status = latestInvoice.statusText,
                            invoiceStatus = latestInvoice.status,
                            iconRes = latestInvoice.iconRes,
                            onClick = onLatestInvoiceClick,
                            modifier = Modifier.padding(horizontal = Spacing.dp24)
                        )
                    }
                }

                // Sticky header (histórico de facturas + botón filtro)
                stickyHeader {
                    StickyInvoiceHeaderComposable(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(IberdrolaTheme.colors.background)
                            .padding(vertical = Spacing.dp8),
                        activeFilterCount = activeFilterCount,
                        isFiltered = isFiltered,
                        onFilterClick = onFilterClick
                    )
                }

                // Histórico de facturas (cabeceras de año + filas)
                items(historyItems) { item ->
                    when (item) {
                        // Cabecera de año ("2024", "2023"...)
                        is InvoiceListItem.HeaderYear -> {
                            InvoiceHeaderItemComposable(year = item.year)
                        }
                        // Fila de factura (fecha, tipo, estado, importe)
                        is InvoiceListItem.InvoiceItem -> {
                            InvoiceRowItemComposable(
                                date = item.date,
                                type = item.type,
                                status = item.statusText,
                                amount = item.amount,
                                invoiceStatus = item.status,
                                onClick = { onHistoryItemClick(item) }
                            )
                        }
                    }
                }
            }
        }
    }
}

private val mockHistoryItems = listOf(
    InvoiceListItem.HeaderYear("2024"),
    InvoiceListItem.InvoiceItem("1", "8 de marzo", "Factura Luz", "45,20 €", "Pagada", InvoiceStatus.PAID),
    InvoiceListItem.InvoiceItem("2", "10 de febrero", "Factura Gas", "32,50 €", "Pagada", InvoiceStatus.PAID),
    InvoiceListItem.InvoiceItem("3", "12 de enero", "Factura Luz", "58,90 €", "Pendiente de Pago", InvoiceStatus.PENDING),
    InvoiceListItem.HeaderYear("2023"),
    InvoiceListItem.InvoiceItem("4", "5 de diciembre", "Factura Luz", "41,00 €", "Anulada", InvoiceStatus.CANCELLED),
    InvoiceListItem.InvoiceItem("5", "3 de noviembre", "Factura Gas", "29,80 €", "Pagada", InvoiceStatus.PAID),
    InvoiceListItem.InvoiceItem("6", "7 de octubre", "Factura Luz", "37,60 €", "En trámite de cobro", InvoiceStatus.PROCESSING)
)

// Pantalla de facturas con datos mockeados
@Preview(name = "Invoice List - Light", showBackground = true)
@Preview(name = "Invoice List - Dark", uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
private fun PreviewInvoiceListComposeScreen() {
    IberdrolaTheme {
        InvoiceListComposeScreen(
            isLoading = false,
            isRefreshing = false,
            latestInvoice = LatestInvoiceUiModel(
                amount = "20,00 €",
                dateRange = "01 feb. 2024 - 04 mar. 2024",
                supplyTypeLabel = "Factura Luz",
                statusText = "Pendiente de Pago",
                status = InvoiceStatus.PENDING,
                iconRes = R.drawable.ic_light
            ),
            historyItems = mockHistoryItems,
            onLatestInvoiceClick = {},
            onFilterClick = {},
            onHistoryItemClick = {},
            onRefresh = {}
        )
    }
}

// Pantalla de facturas en estado skeleton
@Preview(name = "Invoice List Skeleton - Light", showBackground = true)
@Preview(name = "Invoice List Skeleton - Dark", uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
private fun PreviewInvoiceListComposeScreenLoading() {
    IberdrolaTheme {
        InvoiceListComposeScreen(
            isLoading = true,
            isRefreshing = false,
            latestInvoice = null,
            historyItems = emptyList(),
            onLatestInvoiceClick = {},
            onFilterClick = {},
            onHistoryItemClick = {},
            onRefresh = {}
        )
    }
}

// Overlay: datos reales + skeleton superpuesto con opacidad
@OptIn(ExperimentalFoundationApi::class)
@Preview(name = "Overlay - Light", showBackground = true)
@Preview(name = "Overlay - Dark", uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
private fun PreviewInvoiceListOverlay() {
    IberdrolaTheme {
        Box {
            // Pantalla con datos reales
            InvoiceListComposeScreen(
                isLoading = false,
                isRefreshing = false,
                latestInvoice = LatestInvoiceUiModel(
                    amount = "20,00 €",
                    dateRange = "01 feb. 2024 - 04 mar. 2024",
                    supplyTypeLabel = "Factura Luz",
                    statusText = "Pendiente de Pago",
                    status = InvoiceStatus.PENDING,
                    iconRes = R.drawable.ic_light
                ),
                historyItems = mockHistoryItems,
                onLatestInvoiceClick = {},
                onFilterClick = {},
                onHistoryItemClick = {},
                onRefresh = {}
            )
            // Skeleton superpuesto con opacidad
            InvoiceListComposeScreen(
                isLoading = true,
                isRefreshing = false,
                latestInvoice = null,
                historyItems = emptyList(),
                onLatestInvoiceClick = {},
                onFilterClick = {},
                onHistoryItemClick = {},
                onRefresh = {},
                modifier = Modifier.alpha(0.8f)
            )
        }
    }
}
