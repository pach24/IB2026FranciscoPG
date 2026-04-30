package com.iberdrola.practicas2026.FranciscoPG.data.network

import co.infinum.retromock.meta.Mock
import co.infinum.retromock.meta.MockResponse
import com.iberdrola.practicas2026.FranciscoPG.data.model.ApiResponse
import com.iberdrola.practicas2026.FranciscoPG.data.model.ContractDto
import retrofit2.Call
import retrofit2.http.GET

interface ContractApiService {

    @Mock
    @MockResponse(body = "contracts_mock.json")
    @GET("contracts.json")
    fun getContractsCall(): Call<ApiResponse<List<ContractDto>>>
}
