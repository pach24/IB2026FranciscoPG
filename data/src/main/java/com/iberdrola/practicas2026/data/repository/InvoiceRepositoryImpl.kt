// Archivo: InvoiceRepositoryImpl.kt (Ubicación: data/repository)
package com.iberdrola.practicas2026.FranciscoPG.data.repository

import com.iberdrola.practicas2026.FranciscoPG.domain.model.Invoice
import com.iberdrola.practicas2026.FranciscoPG.domain.repository.ConfigurationRepository
import com.iberdrola.practicas2026.FranciscoPG.domain.repository.InvoiceRepository
import com.iberdrola.practicas2026.data.model.InvoiceListResponseDto
import com.iberdrola.practicas2026.data.model.toDomain
import com.iberdrola.practicas2026.data.network.InvoiceApiService
import kotlinx.coroutines.delay
import javax.inject.Inject
import javax.inject.Named

class InvoiceRepositoryImpl @Inject constructor(
    @Named("RealApi") private val realApiService: InvoiceApiService,
    @Named("MockApi") private val mockApiService: InvoiceApiService,
    private val configRepository: ConfigurationRepository
) : InvoiceRepository {

    override suspend fun getInvoices(supplyType: String): Result<List<Invoice>> {
        return try {
            // Evaluamos la configuración en tiempo de ejecución
            val response: InvoiceListResponseDto = if (configRepository.isMockEnabled()) {
                delay((1000..3000).random().toLong())
                mockApiService.getInvoices(supplyType)
            } else {
                realApiService.getInvoices(supplyType)
            }

            val filteredInvoices = response.facturas
                .filter { it.tipoSuministro == supplyType.uppercase() }
                .map { it.toDomain() }

            Result.success(filteredInvoices)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
