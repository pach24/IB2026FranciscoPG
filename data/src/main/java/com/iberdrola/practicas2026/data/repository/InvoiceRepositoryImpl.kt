// Archivo: InvoiceRepositoryImpl.kt (Ubicación: data/repository)
package com.iberdrola.practicas2026.FranciscoPG.data.repository

import com.iberdrola.practicas2026.FranciscoPG.domain.model.Invoice
import com.iberdrola.practicas2026.FranciscoPG.domain.repository.ConfigurationRepository
import com.iberdrola.practicas2026.FranciscoPG.domain.repository.InvoiceRepository
import com.iberdrola.practicas2026.data.local.InvoiceDao
import com.iberdrola.practicas2026.data.local.toDomain
import com.iberdrola.practicas2026.data.local.toEntity
import com.iberdrola.practicas2026.data.model.toDomain
import com.iberdrola.practicas2026.data.network.InvoiceApiService
import kotlinx.coroutines.delay
import javax.inject.Inject
import javax.inject.Named

class InvoiceRepositoryImpl @Inject constructor(
    @Named("RealApi") private val realApiService: InvoiceApiService,
    @Named("MockApi") private val mockApiService: InvoiceApiService,
    private val configRepository: ConfigurationRepository,
    private val invoiceDao: InvoiceDao
) : InvoiceRepository {

    override suspend fun getInvoices(supplyType: String): Result<List<Invoice>> {
        return try {
            if (configRepository.isMockEnabled()) {
                // Mock: solo devolver datos del JSON, sin tocar Room
                delay((1000..3000).random().toLong())
                val response = mockApiService.getInvoices(supplyType)
                val invoices = response.facturas
                    .filter { it.tipoSuministro == supplyType.uppercase() }
                    .map { it.toDomain() }
                Result.success(invoices)
            } else {
                // Real: usar Room como caché
                val cached = invoiceDao.getInvoicesBySupplyType(supplyType.uppercase())
                if (cached.isNotEmpty()) {
                    return Result.success(cached.map { it.toDomain() })
                }

                val response = realApiService.getInvoices(supplyType)
                val filteredInvoices = response.facturas
                    .filter { it.tipoSuministro == supplyType.uppercase() }
                    .map { it.toDomain() }

                invoiceDao.insertAll(
                    filteredInvoices.map { it.toEntity(supplyType.uppercase()) }
                )

                Result.success(filteredInvoices)
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}