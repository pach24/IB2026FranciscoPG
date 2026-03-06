package com.iberdrola.practicas2026.FranciscoPG.presentation.invoices.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MyInvoicesViewModel @Inject constructor() : ViewModel() {

    // Evento para mostrar un diálogo informativo
    private val _showDialogEvent = MutableLiveData<Boolean>()
    val showDialogEvent: LiveData<Boolean> get() = _showDialogEvent

    fun onFeatureNotAvailable() {
        _showDialogEvent.value = true
    }

    // Se llama cuando el diálogo se ha mostrado (o cerrado) para resetear el estado
    fun onDialogHandled() {
        _showDialogEvent.value = false
    }
}