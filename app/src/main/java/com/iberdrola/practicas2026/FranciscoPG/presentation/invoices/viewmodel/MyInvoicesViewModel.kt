package com.iberdrola.practicas2026.FranciscoPG.presentation.invoices.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iberdrola.practicas2026.FranciscoPG.domain.usecase.GetInvoicesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

// Estado de la UI para las facturas
sealed class InvoiceUiState {
    object Loading : InvoiceUiState()
    data class Success(val invoices: List<com.iberdrola.practicas2026.FranciscoPG.domain.model.Invoice>) : InvoiceUiState()
    data class Error(val message: String) : InvoiceUiState()
}

@HiltViewModel
class MyInvoicesViewModel @Inject constructor(
    private val getInvoicesUseCase: GetInvoicesUseCase
) : ViewModel() {

    private val _uiState = MutableLiveData<InvoiceUiState>()
    val uiState: LiveData<InvoiceUiState> get() = _uiState

    private val _showDialogEvent = MutableLiveData<Boolean>()
    val showDialogEvent: LiveData<Boolean> get() = _showDialogEvent

    fun fetchInvoices(supplyType: String, useMock: Boolean = true) {
        Log.d("🔥DI", "VM: fetchInvoices($supplyType, mock=$useMock)")
        _uiState.value = InvoiceUiState.Loading
        viewModelScope.launch {
            try {
                Log.d("DI", "VM: Llamando UseCase...")
                val result = getInvoicesUseCase(supplyType)
                Log.d("DI", "VM: Result = ${result.isSuccess}")
                result.fold(
                    onSuccess = { invoices ->
                        Log.d("DI", "VM: SUCCESS ${invoices.size} invoices")
                        _uiState.value = InvoiceUiState.Success(invoices)
                    },
                    onFailure = { error ->
                        Log.e("DI", "VM: ERROR", error)
                        _uiState.value = InvoiceUiState.Error(error.message ?: "Error desconocido")
                    }
                )
            } catch (e: Exception) {
                Log.e("🔥DI", "VM: EXCEPTION", e)
                _uiState.value = InvoiceUiState.Error(e.message ?: "Error desconocido")
            }
        }
    }


    fun onFeatureNotAvailable() {
        _showDialogEvent.value = true
    }

    fun onDialogHandled() {
        _showDialogEvent.value = false
    }
}
