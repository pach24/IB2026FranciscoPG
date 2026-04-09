package com.iberdrola.practicas2026.FranciscoPG.presentation.electronicinvoice.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iberdrola.practicas2026.FranciscoPG.domain.model.Contract
import com.iberdrola.practicas2026.FranciscoPG.domain.model.ContractStatus
import com.iberdrola.practicas2026.FranciscoPG.domain.model.SupplyType
import com.iberdrola.practicas2026.FranciscoPG.domain.usecase.GetContractsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class ElectronicInvoiceNavigationEvent {
    data class GoToActivate(val contract: Contract) : ElectronicInvoiceNavigationEvent()
    data class GoToModify(val contract: Contract) : ElectronicInvoiceNavigationEvent()
}

@HiltViewModel
class ElectronicInvoiceViewModel @Inject constructor(
    private val getContractsUseCase: GetContractsUseCase
) : ViewModel() {

    private val _contracts = MutableStateFlow<List<Contract>>(emptyList())
    val contracts: StateFlow<List<Contract>> = _contracts.asStateFlow()

    private val _navigationEvent = MutableSharedFlow<ElectronicInvoiceNavigationEvent>()
    val navigationEvent: SharedFlow<ElectronicInvoiceNavigationEvent> = _navigationEvent.asSharedFlow()

    init {
        loadContracts()
    }

    private fun loadContracts() {
        viewModelScope.launch {
            getContractsUseCase().onSuccess { contracts ->
                _contracts.value = contracts
            }.onFailure {
                // Fallback: contratos por defecto
                _contracts.value = listOf(
                    Contract(
                        supplyType = SupplyType.ELECTRICITY,
                        status = ContractStatus.ACTIVE,
                        email = "usuario@email.com"
                    ),
                    Contract(
                        supplyType = SupplyType.GAS,
                        status = ContractStatus.INACTIVE,
                        email = null
                    )
                )
            }
        }
    }

    fun onContractClick(contract: Contract) {
        viewModelScope.launch {
            when (contract.status) {
                ContractStatus.ACTIVE -> _navigationEvent.emit(
                    ElectronicInvoiceNavigationEvent.GoToModify(contract)
                )
                ContractStatus.INACTIVE -> _navigationEvent.emit(
                    ElectronicInvoiceNavigationEvent.GoToActivate(contract)
                )
            }
        }
    }
}
