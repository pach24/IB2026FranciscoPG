package com.iberdrola.practicas2026.FranciscoPG.data.repository

import com.iberdrola.practicas2026.FranciscoPG.domain.model.Invoice
import com.iberdrola.practicas2026.FranciscoPG.domain.repository.InvoiceRepository
import com.iberdrola.practicas2026.data.model.InvoiceListResponseDto
import com.iberdrola.practicas2026.data.model.toDomain
import com.iberdrola.practicas2026.data.network.InvoiceApiService
import kotlinx.coroutines.delay
import javax.inject.Inject
import javax.inject.Named

class InvoiceRepositoryImpl @Inject constructor(
    @Named("RealApi") private val realApiService: InvoiceApiService,
    @Named("MockApi") private val mockApiService: InvoiceApiService
) : InvoiceRepository {

    override suspend fun getInvoices(supplyType: String, useMock: Boolean): Result<List<Invoice>> {
        return try {
            val response: InvoiceListResponseDto = if (useMock) {
                delay((1000..3000).random().toLong())
                mockApiService.getInvoices(supplyType)
            } else {
                realApiService.getInvoices(supplyType)
            }

            // CAMBIO: Filtrar por tipoSuministro antes de mapear
            val filteredInvoices = response.facturas
                .filter { it.tipoSuministro == supplyType.uppercase() }
                .map { it.toDomain() }

            Result.success(filteredInvoices)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
