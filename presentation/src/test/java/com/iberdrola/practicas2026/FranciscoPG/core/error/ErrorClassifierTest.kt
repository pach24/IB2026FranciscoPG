package com.iberdrola.practicas2026.FranciscoPG.core.error

import com.iberdrola.practicas2026.FranciscoPG.presentation.myinvoices.model.InvoiceListUiState
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

class ErrorClassifierTest {

    private lateinit var classifier: ErrorClassifier

    @Before
    fun setUp() {
        classifier = ErrorClassifier()
    }

    // DNS no resuelto -> error de conexion
    @Test
    fun `UnknownHostException returns ConnectionError`() {
        val result = classifier.classify(UnknownHostException("no host"))
        assertTrue(result is InvoiceListUiState.ConnectionError)
    }

    // Conexion rechazada -> error de conexion
    @Test
    fun `ConnectException returns ConnectionError`() {
        val result = classifier.classify(ConnectException("refused"))
        assertTrue(result is InvoiceListUiState.ConnectionError)
    }

    // Timeout de socket -> error de conexion
    @Test
    fun `SocketTimeoutException returns ConnectionError`() {
        val result = classifier.classify(SocketTimeoutException("timeout"))
        assertTrue(result is InvoiceListUiState.ConnectionError)
    }

    // Error generico de I/O -> error de conexion
    @Test
    fun `IOException returns ConnectionError`() {
        val result = classifier.classify(IOException("io error"))
        assertTrue(result is InvoiceListUiState.ConnectionError)
    }

    // HTTP 500 -> error de servidor
    @Test
    fun `HttpException returns ServerError`() {
        val response = Response.error<Any>(500, okhttp3.ResponseBody.create(null, ""))
        val result = classifier.classify(HttpException(response))
        assertTrue(result is InvoiceListUiState.ServerError)
    }

    // Excepcion no catalogada -> error de servidor como fallback
    @Test
    fun `unknown exception returns ServerError`() {
        val result = classifier.classify(RuntimeException("unexpected"))
        assertTrue(result is InvoiceListUiState.ServerError)
    }

    // Verifica que el mensaje del error se propaga al estado
    @Test
    fun `ConnectionError contains error message`() {
        val result = classifier.classify(UnknownHostException("test message"))
        assertEquals("test message", (result as InvoiceListUiState.ConnectionError).message)
    }

    // Verifica que el mensaje del error se propaga al estado de servidor
    @Test
    fun `ServerError contains error message`() {
        val result = classifier.classify(RuntimeException("server down"))
        assertEquals("server down", (result as InvoiceListUiState.ServerError).message)
    }

    // Verifica el mensaje por defecto cuando el error no tiene mensaje
    @Test
    fun `null message defaults to Error desconocido`() {
        val result = classifier.classify(RuntimeException())
        assertEquals("Error desconocido", (result as InvoiceListUiState.ServerError).message)
    }
}
