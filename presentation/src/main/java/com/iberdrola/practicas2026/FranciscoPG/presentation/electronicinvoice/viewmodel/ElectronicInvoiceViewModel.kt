package com.iberdrola.practicas2026.FranciscoPG.presentation.electronicinvoice.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iberdrola.practicas2026.FranciscoPG.domain.analytics.AnalyticsEvent
import com.iberdrola.practicas2026.FranciscoPG.domain.analytics.AnalyticsTracker
import com.iberdrola.practicas2026.FranciscoPG.domain.config.RemoteConfigProvider
import com.iberdrola.practicas2026.FranciscoPG.domain.model.Contract
import com.iberdrola.practicas2026.FranciscoPG.domain.model.ContractStatus
import com.iberdrola.practicas2026.FranciscoPG.domain.model.SupplyType
import com.iberdrola.practicas2026.FranciscoPG.domain.usecase.CensorEmailUseCase
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
    data class GoToActivate(val supplyType: String) : ElectronicInvoiceNavigationEvent()
    data class GoToModify(val supplyType: String, val censoredEmail: String) : ElectronicInvoiceNavigationEvent()
}

@HiltViewModel
class ElectronicInvoiceViewModel @Inject constructor(
    private val getContractsUseCase: GetContractsUseCase,
    private val censorEmailUseCase: CensorEmailUseCase,
    private val remoteConfig: RemoteConfigProvider,
    private val analyticsTracker: AnalyticsTracker
) : ViewModel() {

    private val _contracts = MutableStateFlow<List<Contract>>(emptyList())
    val contracts: StateFlow<List<Contract>> = _contracts.asStateFlow()

    private val _navigationEvent = MutableSharedFlow<ElectronicInvoiceNavigationEvent>()
    val navigationEvent: SharedFlow<ElectronicInvoiceNavigationEvent> = _navigationEvent.asSharedFlow()

    init {
        loadContracts()
    }

    fun loadContracts() {
        viewModelScope.launch {
            getContractsUseCase().onSuccess { contracts ->
                _contracts.value = if (remoteConfig.isGasContractsEnabled) contracts
                    else contracts.filter { it.supplyType != SupplyType.GAS }
            }
        }
    }

    fun onContractClick(contract: Contract) {
        analyticsTracker.logEvent(
            AnalyticsEvent.TAP_CONTRACT,
            mapOf(AnalyticsEvent.PARAM_SUPPLY_TYPE to contract.supplyType.apiValue)
        )
        viewModelScope.launch {
            when (contract.status) {
                ContractStatus.ACTIVE -> _navigationEvent.emit(
                    ElectronicInvoiceNavigationEvent.GoToModify(
                        supplyType = contract.supplyType.apiValue,
                        censoredEmail = censorEmailUseCase(contract.email ?: "")
                    )
                )
                ContractStatus.INACTIVE -> _navigationEvent.emit(
                    ElectronicInvoiceNavigationEvent.GoToActivate(
                        supplyType = contract.supplyType.apiValue
                    )
                )
            }
        }
    }
}
