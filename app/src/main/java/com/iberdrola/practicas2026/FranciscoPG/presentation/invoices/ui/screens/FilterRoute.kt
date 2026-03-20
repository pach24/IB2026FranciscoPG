package com.iberdrola.practicas2026.FranciscoPG.presentation.invoices.ui.screens

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.iberdrola.practicas2026.FranciscoPG.domain.model.InvoiceFilters
import com.iberdrola.practicas2026.FranciscoPG.presentation.invoices.viewmodel.FilterViewModel
import com.iberdrola.practicas2026.FranciscoPG.presentation.theme.IberdrolaTheme

@Composable
fun FilterRoute(
    onBack: () -> Unit,
    filterViewModel: FilterViewModel,
    onFiltersApplied: () -> Unit = {},
    onFiltersCleared: (previousFilters: InvoiceFilters) -> Unit = {}
) {
    val filterUIState by filterViewModel.filterState.collectAsStateWithLifecycle()

    Scaffold(
        containerColor = IberdrolaTheme.colors.background,
        modifier = Modifier.statusBarsPadding(),
        topBar = { FilterTopBar(onBack = onBack) }
    ) { padding ->
        FilterContent(
            modifier = Modifier.padding(padding),
            uiState = filterUIState,
            onApplyFilters = { filters ->
                filterViewModel.updateFilters(filters)
                filterViewModel.applyFilters()
                onFiltersApplied()
                onBack()
            },
            onClearFilters = {
                val previous = filterViewModel.appliedFilters.value
                filterViewModel.clearFilters()
                if (previous != InvoiceFilters()) {
                    onFiltersCleared(previous)
                }
            }
        )
    }
}
