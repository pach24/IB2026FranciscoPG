package com.iberdrola.practicas2026.FranciscoPG.data.network

import co.infinum.retromock.meta.Mock
import co.infinum.retromock.meta.MockResponse
import com.iberdrola.practicas2026.FranciscoPG.data.model.ApiResponse
import com.iberdrola.practicas2026.FranciscoPG.data.model.InvoiceDto
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface InvoiceApiService {

    @Mock
    @MockResponse(body = "invoices_mock.json")
    @GET("invoices.json")
    suspend fun getInvoices(@Query("supplyType") supplyType: String): ApiResponse<List<InvoiceDto>>

    @Mock
    @MockResponse(body = "invoices_mock.json")
    @GET("invoices.json")
    fun getInvoicesCall(@Query("supplyType") supplyType: String): Call<ApiResponse<List<InvoiceDto>>>
}
