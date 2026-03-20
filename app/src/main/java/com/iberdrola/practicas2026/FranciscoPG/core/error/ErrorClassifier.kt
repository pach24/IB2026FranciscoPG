package com.iberdrola.practicas2026.FranciscoPG.core.error

import com.iberdrola.practicas2026.FranciscoPG.presentation.invoices.model.InvoiceListUiState
import retrofit2.HttpException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject

class ErrorClassifier @Inject constructor() {

    fun classify(error: Throwable): InvoiceListUiState {
        val msg = error.message ?: "Error desconocido"
        return when (error) {
            is UnknownHostException,
            is ConnectException,
            is SocketTimeoutException,
            is java.io.IOException -> InvoiceListUiState.ConnectionError(msg)

            is HttpException -> InvoiceListUiState.ServerError(msg)

            else -> InvoiceListUiState.ServerError(msg)
        }
    }
}
