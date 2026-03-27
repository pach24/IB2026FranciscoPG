package com.iberdrola.practicas2026.FranciscoPG.presentation.electronicinvoice.viewmodel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ConfirmElectronicInvoiceViewModel @Inject constructor() : ViewModel() {

    // Estado para mostrar el spinner semitransparente
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    // Estado para mostrar el banner
    private val _showBanner = MutableStateFlow(false)
    val showBanner: StateFlow<Boolean> = _showBanner.asStateFlow()

    fun onResendClicked() {
        viewModelScope.launch {
            // 1. Mostramos la pantalla de carga y ocultamos banner si estaba visible
            _isLoading.value = true
            _showBanner.value = false

            // 2. Simulamos la espera de 5 segundos
            delay(5000)

            // 3. Ocultamos la pantalla de carga y mostramos el banner
            _isLoading.value = false
            _showBanner.value = true
        }
    }
}