package com.iberdrola.practicas2026.FranciscoPG.presentation.invoices.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iberdrola.practicas2026.FranciscoPG.R
import com.iberdrola.practicas2026.FranciscoPG.domain.model.Invoice
import com.iberdrola.practicas2026.FranciscoPG.domain.usecase.GetInvoicesUseCase
import com.iberdrola.practicas2026.FranciscoPG.presentation.invoices.model.InvoiceListItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

// Estado de la UI para las facturas
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
    val iconRes: Int
)

sealed class InvoiceListUiState {
    object Loading : InvoiceListUiState()
    data class Success(
        val latestInvoice: LatestInvoiceUiModel?,
        val historyItems: List<InvoiceListItem>
    ) : InvoiceListUiState()

    data class Error(val message: String) : InvoiceListUiState()
}

@HiltViewModel
class MyInvoicesViewModel @Inject constructor(
    private val getInvoicesUseCase: GetInvoicesUseCase
) : ViewModel() {

    private val _uiState = MutableLiveData<InvoiceUiState>()
    val uiState: LiveData<InvoiceUiState> get() = _uiState

    private val _listUiState = MutableLiveData<InvoiceListUiState>(InvoiceListUiState.Loading)
    val listUiState: LiveData<InvoiceListUiState> get() = _listUiState

    private val _showDialogEvent = MutableLiveData<Boolean>()
    val showDialogEvent: LiveData<Boolean> get() = _showDialogEvent

    fun fetchInvoices(supplyType: String, useMock: Boolean = true) {
        Log.d("🔥DI", "VM: fetchInvoices($supplyType, mock=$useMock)")
        _uiState.value = InvoiceUiState.Loading
        _listUiState.value = InvoiceListUiState.Loading
        viewModelScope.launch {
            try {
                Log.d("DI", "VM: Llamando UseCase...")
                val result = getInvoicesUseCase(supplyType)
                Log.d("DI", "VM: Result = ${result.isSuccess}")
                result.fold(
                    onSuccess = { invoices ->
                        Log.d("DI", "VM: SUCCESS ${invoices.size} invoices")
                        _uiState.value = InvoiceUiState.Success(invoices)
                        _listUiState.value = buildListUiState(invoices, supplyType)
                    },
                    onFailure = { error ->
                        Log.e("DI", "VM: ERROR", error)
                        _uiState.value = InvoiceUiState.Error(error.message ?: "Error desconocido")
                        _listUiState.value = InvoiceListUiState.Error(
                            error.message ?: "Error desconocido"
                        )
                    }
                )
            } catch (e: Exception) {
                Log.e("🔥DI", "VM: EXCEPTION", e)
                _uiState.value = InvoiceUiState.Error(e.message ?: "Error desconocido")
                _listUiState.value = InvoiceListUiState.Error(e.message ?: "Error desconocido")
            }
        }
    }

    fun onFeatureNotAvailable() {
        _showDialogEvent.value = true
    }

    fun onDialogHandled() {
        _showDialogEvent.value = false
    }

    private fun buildListUiState(
        invoices: List<Invoice>,
        supplyType: String
    ): InvoiceListUiState {
        val supplyTypeLabel = supplyTypeLabel(supplyType)
        val iconRes = supplyTypeIconRes(supplyType)
        val latestInvoice = invoices.firstOrNull()?.let { invoice ->
            LatestInvoiceUiModel(
                amount = "%.2f \u20AC".format(invoice.amount),
                dateRange = "${invoice.periodStart} - ${invoice.periodEnd}",
                supplyTypeLabel = "Factura $supplyTypeLabel",
                status = invoice.status,
                iconRes = iconRes
            )
        }

        return InvoiceListUiState.Success(
            latestInvoice = latestInvoice,
            historyItems = invoicesToListItems(invoices, supplyTypeLabel)
        )
    }

    private fun invoicesToListItems(
        invoices: List<Invoice>,
        supplyTypeLabel: String
    ): List<InvoiceListItem> {
        val grouped = invoices
            .sortedByDescending { it.chargeDate }
            .groupBy { it.chargeDate.takeLast(4) }

        val result = mutableListOf<InvoiceListItem>()

        grouped.forEach { (year, yearInvoices) ->
            result.add(InvoiceListItem.HeaderYear(year))
            yearInvoices.forEach { invoice ->
                result.add(
                    InvoiceListItem.InvoiceItem(
                        id = invoice.id,
                        date = formatDateToSpanish(invoice.chargeDate),
                        type = "Factura $supplyTypeLabel",
                        amount = "%.2f \u20AC".format(invoice.amount),
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

    private fun supplyTypeLabel(supplyType: String): String {
        return if (supplyType.equals("gas", ignoreCase = true)) "Gas" else "Luz"
    }

    private fun supplyTypeIconRes(supplyType: String): Int {
        return if (supplyType.equals("gas", ignoreCase = true)) {
            R.drawable.ic_gas
        } else {
            R.drawable.ic_light
        }
    }
}
