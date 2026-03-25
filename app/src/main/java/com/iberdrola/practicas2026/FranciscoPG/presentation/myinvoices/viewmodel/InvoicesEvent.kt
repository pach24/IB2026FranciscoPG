package com.iberdrola.practicas2026.FranciscoPG.presentation.myinvoices.viewmodel

sealed interface InvoicesEvent {
    data class OnMockModeChanged(val useMock: Boolean) : InvoicesEvent
    object OnRefresh : InvoicesEvent
    data class OnTabChanged(val index: Int) : InvoicesEvent
    object OnFeatureNotAvailable : InvoicesEvent
    object OnBannerDismissed : InvoicesEvent
}
