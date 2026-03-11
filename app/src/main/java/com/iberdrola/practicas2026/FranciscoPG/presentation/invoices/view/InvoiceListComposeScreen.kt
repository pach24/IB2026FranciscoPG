package com.iberdrola.practicas2026.FranciscoPG.presentation.invoices.view

import android.content.res.Configuration
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.tooling.preview.Preview
import com.iberdrola.practicas2026.FranciscoPG.R
import com.iberdrola.practicas2026.FranciscoPG.presentation.invoices.model.InvoiceListItem

@Composable
fun InvoiceListComposeScreen(
    isLoading: Boolean,
    latestInvoiceAmount: String,
    latestInvoiceDateRange: String,
    latestInvoiceType: String,
    latestInvoiceStatus: String,
    historyItems: List<InvoiceListItem>,
    onLatestInvoiceClick: () -> Unit,
    onFilterClick: () -> Unit,
    onHistoryItemClick: (InvoiceListItem.InvoiceItem) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = dimensionResource(R.dimen.m3_sys_spacing_5)),
        verticalArrangement = Arrangement.Top
    ) {
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = dimensionResource(R.dimen.m3_sys_spacing_4))
            ) {
                if (isLoading) {
                    SkeletonLatestInvoiceCardComposable()
                } else {
                    LatestInvoiceCardComposable(
                        amount = latestInvoiceAmount,
                        dateRange = latestInvoiceDateRange,
                        supplyType = latestInvoiceType,
                        status = latestInvoiceStatus,
                        modifier = Modifier.clickable(onClick = onLatestInvoiceClick)
                    )
                }
            }
        }

        item {
            Column(modifier = Modifier.fillMaxWidth()) {
                if (isLoading) {
                    SkeletonStickyInvoiceHeaderComposable()
                } else {
                    StickyInvoiceHeaderComposable(onFilterClick = onFilterClick)
                }
            }
        }

        if (isLoading) {
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = dimensionResource(R.dimen.m3_sys_spacing_3))
                ) {
                    SkeletonInvoiceHeaderItemComposable()
                    repeat(4) {
                        SkeletonInvoiceRowItemComposable()
                    }
                }
            }
        } else {
            items(
                items = historyItems,
                key = { item ->
                    when (item) {
                        is InvoiceListItem.HeaderYear -> "year_${item.year}"
                        is InvoiceListItem.InvoiceItem -> item.id
                    }
                }
            ) { item ->
                when (item) {
                    is InvoiceListItem.HeaderYear -> {
                        InvoiceHeaderItemComposable(
                            year = item.year,
                            modifier = Modifier.padding(horizontal = dimensionResource(R.dimen.m3_sys_spacing_3))
                        )
                    }

                    is InvoiceListItem.InvoiceItem -> {
                        InvoiceRowItemComposable(
                            date = item.date,
                            type = item.type,
                            status = item.statusText,
                            amount = item.amount,
                            onClick = { onHistoryItemClick(item) },
                            modifier = Modifier.padding(horizontal = dimensionResource(R.dimen.m3_sys_spacing_3))
                        )
                    }
                }
            }
        }
    }
}

@Preview(name = "Invoice List Screen - Light", showBackground = true)
@Preview(name = "Invoice List Screen - Dark", uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
private fun PreviewInvoiceListComposeScreenLoaded() {
    MaterialTheme {
        InvoiceListComposeScreen(
            isLoading = false,
            latestInvoiceAmount = "20,00 €",
            latestInvoiceDateRange = "01 feb. 2024 - 04 mar. 2024",
            latestInvoiceType = "Factura Luz",
            latestInvoiceStatus = "Pendiente de Pago",
            historyItems = listOf(
                InvoiceListItem.HeaderYear("2024"),
                InvoiceListItem.InvoiceItem("1", "8 de marzo", "Factura Luz", "20,00 €", "Pendiente de Pago", false),
                InvoiceListItem.InvoiceItem("2", "1 de febrero", "Factura Luz", "54,21 €", "Pagada", true)
            ),
            onLatestInvoiceClick = {},
            onFilterClick = {},
            onHistoryItemClick = {}
        )
    }
}

@Preview(name = "Invoice List Screen Loading - Light", showBackground = true)
@Preview(name = "Invoice List Screen Loading - Dark", uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
private fun PreviewInvoiceListComposeScreenLoading() {
    MaterialTheme {
        InvoiceListComposeScreen(
            isLoading = true,
            latestInvoiceAmount = "",
            latestInvoiceDateRange = "",
            latestInvoiceType = "",
            latestInvoiceStatus = "",
            historyItems = emptyList(),
            onLatestInvoiceClick = {},
            onFilterClick = {},
            onHistoryItemClick = {}
        )
    }
}
