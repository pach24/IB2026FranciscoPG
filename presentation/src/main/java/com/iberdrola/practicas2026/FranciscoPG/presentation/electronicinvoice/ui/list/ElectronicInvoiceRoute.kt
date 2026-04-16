package com.iberdrola.practicas2026.FranciscoPG.presentation.electronicinvoice.ui.list

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.iberdrola.practicas2026.FranciscoPG.presentation.electronicinvoice.viewmodel.ElectronicInvoiceNavigationEvent
import com.iberdrola.practicas2026.FranciscoPG.presentation.electronicinvoice.viewmodel.ElectronicInvoiceViewModel

@Composable
fun ElectronicInvoiceRoute(
    onNavigateBack: () -> Unit,
    onNavigateToActivate: (supplyType: String) -> Unit,
    onNavigateToModify: (supplyType: String, censoredEmail: String) -> Unit
) {
    val viewModel: ElectronicInvoiceViewModel = hiltViewModel()
    val contracts by viewModel.contracts.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.loadContracts()
        viewModel.navigationEvent.collect { event ->
            when (event) {
                is ElectronicInvoiceNavigationEvent.GoToActivate ->
                    onNavigateToActivate(event.supplyType)
                is ElectronicInvoiceNavigationEvent.GoToModify ->
                    onNavigateToModify(event.supplyType, event.censoredEmail)
            }
        }
    }

    ElectronicInvoiceScreen(
        contracts = contracts,
        onContractClick = viewModel::onContractClick,
        onNavigateBack = onNavigateBack
    )
}
