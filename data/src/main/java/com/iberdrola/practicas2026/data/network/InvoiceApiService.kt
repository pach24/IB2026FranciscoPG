package com.iberdrola.practicas2026.data.network

import co.infinum.retromock.meta.Mock
import co.infinum.retromock.meta.MockResponse
import com.iberdrola.practicas2026.data.model.InvoiceListResponseDto
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface InvoiceApiService {

    @Mock
    @MockResponse(body = "invoices_mock.json")
    @GET("invoices.json")
    suspend fun getInvoices(@Query("supplyType") supplyType: String): InvoiceListResponseDto

    @Mock
    @MockResponse(body = "invoices_mock.json")
    @GET("invoices.json")
    fun getInvoicesCall(@Query("supplyType") supplyType: String): Call<InvoiceListResponseDto>
}
