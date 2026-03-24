package com.iberdrola.practicas2026.FranciscoPG.presentation.invoices.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
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
    onFiltersCleared: (previousDraft: InvoiceFilters, previousApplied: InvoiceFilters) -> Unit = { _, _ -> },
    onFilterInteraction: () -> Unit = {}
) {
    val filterUIState by filterViewModel.filterState.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(IberdrolaTheme.colors.background)
            .statusBarsPadding()
    ) {
        Scaffold(
            containerColor = IberdrolaTheme.colors.background,
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
                onClearFilters = { previousDraft ->
                    val previousApplied = filterViewModel.appliedFilters.value
                    filterViewModel.clearFilters()
                    onFiltersCleared(previousDraft, previousApplied)
                },
                onFilterInteraction = onFilterInteraction
            )
        }
    }
}
