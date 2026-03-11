package com.iberdrola.practicas2026.FranciscoPG.presentation.invoices.view

import android.content.res.Configuration
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.iberdrola.practicas2026.FranciscoPG.R
import com.iberdrola.practicas2026.FranciscoPG.presentation.invoices.model.InvoiceListItem

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun InvoiceListComposeScreen(
    isLoading: Boolean,
    latestInvoiceAmount: String,
    latestInvoiceDateRange: String,
    latestInvoiceType: String,
    latestInvoiceStatus: String,
    latestInvoiceIconRes: Int,
    historyItems: List<InvoiceListItem>,
    onLatestInvoiceClick: () -> Unit,
    onFilterClick: () -> Unit,
    onHistoryItemClick: (InvoiceListItem.InvoiceItem) -> Unit,
    modifier: Modifier = Modifier,
    listState: LazyListState = rememberLazyListState()
) {
    if (isLoading) {
        // Skeleton usando los composables que ya existen en el proyecto
        LazyColumn(
            modifier = modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 32.dp)
        ) {
            item {
                Spacer(modifier = Modifier.height(16.dp))
                SkeletonLatestInvoiceCardComposable(
                    modifier = Modifier.padding(horizontal = 24.dp)
                )
            }

            item {
                SkeletonStickyInvoiceHeaderComposable(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(colorResource(R.color.color_background))
                )
            }

            // Mostrar 6 filas skeleton como placeholder de la lista
            items(6) {
                SkeletonInvoiceRowItemComposable()
            }
        }
    } else {
        LazyColumn(
            modifier = modifier.fillMaxSize(),
            state = listState,
            contentPadding = PaddingValues(bottom = 32.dp)
        ) {
            /* ITEM 1: MARGIN + TARJETA (Se colapsa al hacer scroll) */
            item {
                Spacer(modifier = Modifier.height(16.dp))
                LatestInvoiceCardComposable(
                    amount = latestInvoiceAmount,
                    dateRange = latestInvoiceDateRange,
                    supplyType = latestInvoiceType,   // parámetro correcto: supplyType
                    status = latestInvoiceStatus,
                    iconRes = latestInvoiceIconRes,
                    modifier = Modifier
                        .padding(horizontal = 24.dp)
                        .clickable { onLatestInvoiceClick() }
                )
            }

            /* STICKY HEADER: HISTÓRICO (Se queda fijo arriba) */
            stickyHeader {
                StickyInvoiceHeaderComposable(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(colorResource(R.color.color_background))
                        .padding(vertical = 8.dp),
                    onFilterClick = onFilterClick
                )
            }

            /* LISTA DE FACTURAS */
            items(historyItems) { item ->
                when (item) {
                    is InvoiceListItem.HeaderYear -> {
                        InvoiceHeaderItemComposable(year = item.year)
                    }
                    is InvoiceListItem.InvoiceItem -> {
                        InvoiceRowItemComposable(
                            date = item.date,
                            type = item.type,
                            status = item.statusText,
                            amount = item.amount,
                            modifier = Modifier.clickable { onHistoryItemClick(item) }
                        )
                    }
                }
            }
        }
    }
}

@Preview(name = "Invoice List - Light", showBackground = true)
@Preview(name = "Invoice List - Dark", uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
private fun PreviewInvoiceListComposeScreen() {
    MaterialTheme {
        InvoiceListComposeScreen(
            isLoading = false,
            latestInvoiceAmount = "20,00 €",
            latestInvoiceDateRange = "01 feb. 2024 - 04 mar. 2024",
            latestInvoiceType = "Factura Luz",
            latestInvoiceStatus = "Pendiente de Pago",
            latestInvoiceIconRes = R.drawable.ic_light,
            historyItems = emptyList(),
            onLatestInvoiceClick = {},
            onFilterClick = {},
            onHistoryItemClick = {}
        )
    }
}

@Preview(name = "Invoice List Skeleton - Light", showBackground = true)
@Preview(name = "Invoice List Skeleton - Dark", uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
private fun PreviewInvoiceListComposeScreenLoading() {
    MaterialTheme {
        InvoiceListComposeScreen(
            isLoading = true,
            latestInvoiceAmount = "",
            latestInvoiceDateRange = "",
            latestInvoiceType = "",
            latestInvoiceStatus = "",
            latestInvoiceIconRes = R.drawable.ic_light,
            historyItems = emptyList(),
            onLatestInvoiceClick = {},
            onFilterClick = {},
            onHistoryItemClick = {}
        )
    }
}