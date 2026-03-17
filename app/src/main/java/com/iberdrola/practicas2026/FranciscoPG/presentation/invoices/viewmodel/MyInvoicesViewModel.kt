// app/src/main/java/com/iberdrola/practicas2026/FranciscoPG/presentation/invoices/viewmodel/MyInvoicesViewModel.kt
package com.iberdrola.practicas2026.FranciscoPG.presentation.invoices.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iberdrola.practicas2026.FranciscoPG.R
import com.iberdrola.practicas2026.FranciscoPG.domain.model.Invoice
import com.iberdrola.practicas2026.FranciscoPG.domain.usecase.GetInvoicesUseCase
import com.iberdrola.practicas2026.FranciscoPG.presentation.invoices.model.InvoiceListItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject

// ── UI States ─────────────────────────────────────────────────────────────────

sealed class InvoiceUiState {
    object Loading : InvoiceUiState()
    data class Success(val invoices: List<Invoice>) : InvoiceUiState()
    data class Error(val message: String) : InvoiceUiState()
}

data class LatestInvoiceUiModel(
    val amount: String,
    val dateRange: String,
    val supplyTypeLabel: String,
    val status: String,
    val isPaid: Boolean,
    val iconRes: Int
)

sealed class InvoiceListUiState {
    object Loading : InvoiceListUiState()

    data class Success(
        val latestInvoice: LatestInvoiceUiModel?,
        val historyItems: List<InvoiceListItem>,
        val invoiceCount: Int
    ) : InvoiceListUiState()

    object Empty : InvoiceListUiState()

    data class ServerError(val message: String) : InvoiceListUiState()

    data class ConnectionError(val message: String) : InvoiceListUiState()
}

// ── ViewModel ─────────────────────────────────────────────────────────────────

@HiltViewModel
class MyInvoicesViewModel @Inject constructor(
    private val getInvoicesUseCase: GetInvoicesUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<InvoiceUiState>(InvoiceUiState.Loading)
    val uiState: StateFlow<InvoiceUiState> = _uiState.asStateFlow()

    private val _listUiState = MutableStateFlow<InvoiceListUiState>(InvoiceListUiState.Loading)
    val listUiState: StateFlow<InvoiceListUiState> = _listUiState.asStateFlow()

    private val _showDialogEvent = MutableStateFlow(false)
    val showDialogEvent: StateFlow<Boolean> = _showDialogEvent.asStateFlow()

    private var fetchGeneration = 0

    fun fetchInvoices(supplyType: String, useMock: Boolean = true, forceRefresh: Boolean = false) {
        Log.d("MyInvoicesVM", "fetchInvoices($supplyType, mock=$useMock, force=$forceRefresh)")
        val currentGeneration = ++fetchGeneration
        _uiState.value = InvoiceUiState.Loading
        _listUiState.value = InvoiceListUiState.Loading

        viewModelScope.launch {
            try {
                val result = getInvoicesUseCase(supplyType, forceRefresh = forceRefresh)
                if (currentGeneration != fetchGeneration) return@launch
                result.fold(
                    onSuccess = { invoices ->
                        Log.d("MyInvoicesVM", "SUCCESS: ${invoices.size} facturas")
                        _uiState.value = InvoiceUiState.Success(invoices)
                        _listUiState.value = buildListUiState(invoices, supplyType)
                    },
                    onFailure = { error ->
                        Log.e("MyInvoicesVM", "ERROR", error)
                        handleError(error)
                    }
                )
            } catch (e: Exception) {
                if (currentGeneration != fetchGeneration) return@launch
                Log.e("MyInvoicesVM", "EXCEPTION", e)
                handleError(e)
            }
        }
    }

    fun onFeatureNotAvailable() {
        _showDialogEvent.value = true
    }

    fun onDialogHandled() {
        _showDialogEvent.value = false
    }

    // ── Helpers privados ──────────────────────────────────────────────────────

    private fun handleError(error: Throwable) {
        val msg = error.message ?: "Error desconocido"
        _uiState.value = InvoiceUiState.Error(msg)
        _listUiState.value = classifyError(error)
    }

    private fun classifyError(error: Throwable): InvoiceListUiState {
        val msg = error.message ?: "Error desconocido"
        return when (error) {
            is UnknownHostException,
            is ConnectException,
            is SocketTimeoutException -> {
                InvoiceListUiState.ConnectionError(msg)
            }
            is HttpException -> {
                InvoiceListUiState.ServerError(msg)
            }
            is java.io.IOException -> {
                InvoiceListUiState.ConnectionError(msg)
            }
            else -> {
                InvoiceListUiState.ServerError(msg)
            }
        }
    }

    private fun buildListUiState(
        invoices: List<Invoice>,
        supplyType: String
    ): InvoiceListUiState {
        if (invoices.isEmpty()) {
            return InvoiceListUiState.Empty
        }

        val supplyTypeLabel = supplyTypeLabel(supplyType)
        val iconRes = supplyTypeIconRes(supplyType)
        val latestInvoice = invoices.firstOrNull()?.let { invoice ->
            LatestInvoiceUiModel(
                amount = "%.2f €".format(invoice.amount),
                dateRange = "${invoice.periodStart} - ${invoice.periodEnd}",
                supplyTypeLabel = "Factura $supplyTypeLabel",
                status = invoice.status,
                isPaid = invoice.status.contains("Pagada", ignoreCase = true),
                iconRes = iconRes
            )
        }
        return InvoiceListUiState.Success(
            latestInvoice = latestInvoice,
            historyItems = invoicesToListItems(invoices, supplyTypeLabel),
            invoiceCount = invoices.size
        )
    }

    private fun invoicesToListItems(
        invoices: List<Invoice>,
        supplyTypeLabel: String
    ): List<InvoiceListItem> {
        val result = mutableListOf<InvoiceListItem>()
        invoices
            .groupBy { it.chargeDate.takeLast(4) }
            .forEach { (year, yearInvoices) ->
                result.add(InvoiceListItem.HeaderYear(year))
                yearInvoices.forEach { invoice ->
                    result.add(
                        InvoiceListItem.InvoiceItem(
                            id = invoice.id,
                            date = formatDateToSpanish(invoice.chargeDate),
                            type = "Factura $supplyTypeLabel",
                            amount = "%.2f €".format(invoice.amount),
                            statusText = invoice.status,
                            isPaid = invoice.status.contains("Pagada", ignoreCase = true)
                        )
                    )
                }
            }
        return result
    }

    private fun formatDateToSpanish(dateStr: String): String {
        val parts = dateStr.split("/")
        if (parts.size != 3) return dateStr
        val day = parts[0].toIntOrNull() ?: return dateStr
        val month = parts[1].toIntOrNull() ?: return dateStr
        val months = arrayOf(
            "", "enero", "febrero", "marzo", "abril", "mayo", "junio",
            "julio", "agosto", "septiembre", "octubre", "noviembre", "diciembre"
        )
        if (month !in 1..12) return dateStr
        return "$day de ${months[month]}"
    }

    private fun supplyTypeLabel(supplyType: String): String =
        if (supplyType.equals("gas", ignoreCase = true)) "Gas" else "Luz"

    private fun supplyTypeIconRes(supplyType: String): Int =
        if (supplyType.equals("gas", ignoreCase = true)) R.drawable.ic_gas else R.drawable.ic_light
}
