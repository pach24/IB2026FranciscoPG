package com.iberdrola.practicas2026.FranciscoPG.presentation.electronicinvoice.ui

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
    onNavigateToActivate: () -> Unit,
    onNavigateToModify: () -> Unit
) {
    val viewModel: ElectronicInvoiceViewModel = hiltViewModel()
    val contracts by viewModel.contracts.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.navigationEvent.collect { event ->
            when (event) {
                is ElectronicInvoiceNavigationEvent.GoToActivate -> onNavigateToActivate()
                is ElectronicInvoiceNavigationEvent.GoToModify -> onNavigateToModify()
            }
        }
    }

    ElectronicInvoiceScreen(
        contracts = contracts,
        onContractClick = viewModel::onContractClick,
        onNavigateBack = onNavigateBack
    )
}
