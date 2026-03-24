// Archivo: InvoiceRepositoryImpl.kt (Ubicación: data/repository)
package com.iberdrola.practicas2026.FranciscoPG.data.repository

import com.iberdrola.practicas2026.FranciscoPG.domain.model.Invoice
import com.iberdrola.practicas2026.FranciscoPG.domain.model.SupplyType
import com.iberdrola.practicas2026.FranciscoPG.domain.repository.ConfigurationRepository
import com.iberdrola.practicas2026.FranciscoPG.domain.repository.InvoiceRepository
import com.iberdrola.practicas2026.data.local.InvoiceDao
import com.iberdrola.practicas2026.data.local.toDomain
import com.iberdrola.practicas2026.data.local.toEntity
import com.iberdrola.practicas2026.data.model.toDomain
import com.iberdrola.practicas2026.data.network.InvoiceApiService
import com.iberdrola.practicas2026.data.network.safeAwait
import kotlinx.coroutines.delay
import javax.inject.Inject
import javax.inject.Named

class InvoiceRepositoryImpl @Inject constructor(
    @Named("RealApi") private val realApiService: InvoiceApiService,
    @Named("MockApi") private val mockApiService: InvoiceApiService,
    private val configRepository: ConfigurationRepository,
    private val invoiceDao: InvoiceDao
) : InvoiceRepository {

    override suspend fun getInvoices(supplyType: SupplyType, forceRefresh: Boolean): Result<List<Invoice>> {
        val apiValue = supplyType.apiValue
        return try {
            if (configRepository.isMockEnabled()) {
                // Mock: solo devolver datos del JSON, sin tocar Room
                delay((1000..3000).random().toLong())
                val response = mockApiService.getInvoicesCall(apiValue).safeAwait()
                val invoices = response.facturas
                    .filter { it.tipoSuministro.equals(apiValue, ignoreCase = true) }
                    .map { it.toDomain() }
                Result.success(invoices)
            } else {
                // Room es SSOT: intentar sincronizar con la API
                // y siempre devolver lo que haya en Room.
                if (forceRefresh) {
                    // Intentar traer datos nuevos de la API
                    try {
                        val response = realApiService.getInvoices(apiValue)
                        val newInvoices = response.facturas
                            .filter { it.tipoSuministro.equals(apiValue, ignoreCase = true) }
                            .map { it.toDomain() }
                        invoiceDao.insertAll(newInvoices.map { it.toEntity() })
                    } catch (_: Exception) {
                        // API falló, no pasa nada: Room tiene los datos anteriores
                    }
                } else {
                    // Primera carga: intentar sincronizar si Room está vacío
                    val cached = invoiceDao.getInvoicesBySupplyType(apiValue)
                    if (cached.isEmpty()) {
                        try {
                            val response = realApiService.getInvoices(apiValue)
                            val newInvoices = response.facturas
                                .filter { it.tipoSuministro.equals(apiValue, ignoreCase = true) }
                                .map { it.toDomain() }
                            invoiceDao.insertAll(newInvoices.map { it.toEntity() })
                        } catch (e: Exception) {
                            // API falló y Room vacío → error real
                            return Result.failure(e)
                        }
                    }
                }

                // Siempre devolver Room como fuente de verdad
                val allCached = invoiceDao.getInvoicesBySupplyType(apiValue)
                if (allCached.isNotEmpty()) {
                    Result.success(allCached.map { it.toDomain() })
                } else {
                    Result.success(emptyList())
                }
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
