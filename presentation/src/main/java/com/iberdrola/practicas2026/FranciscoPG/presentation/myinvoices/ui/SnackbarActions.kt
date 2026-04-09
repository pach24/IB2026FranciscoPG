package com.iberdrola.practicas2026.FranciscoPG.presentation.myinvoices.ui

import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeoutOrNull

fun CoroutineScope.showFilterResultSnackbar(
    snackbarHostState: SnackbarHostState,
    totalCount: Int
) {
    launch {
        snackbarHostState.currentSnackbarData?.dismiss()
        delay(300)
        val message = if (totalCount == 0) {
            "No hay facturas que coincidan con tu búsqueda"
        } else {
            "$totalCount facturas coinciden con los filtros"
        }
        withTimeoutOrNull(2000) {
            snackbarHostState.showSnackbar(
                message = message,
                duration = SnackbarDuration.Indefinite
            )
        }
        snackbarHostState.currentSnackbarData?.dismiss()
    }
}

fun CoroutineScope.showFiltersClearedSnackbar(
    snackbarHostState: SnackbarHostState,
    message: String,
    undoLabel: String,
    onUndo: () -> Unit
) {
    launch {
        snackbarHostState.currentSnackbarData?.dismiss()
        val result = withTimeoutOrNull(3000) {
            snackbarHostState.showSnackbar(
                message = message,
                actionLabel = undoLabel,
                duration = SnackbarDuration.Indefinite
            )
        }
        if (result == SnackbarResult.ActionPerformed) {
            onUndo()
        }
        snackbarHostState.currentSnackbarData?.dismiss()
    }
}
