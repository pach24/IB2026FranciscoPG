package com.iberdrola.practicas2026.FranciscoPG.data.repository

import com.iberdrola.practicas2026.FranciscoPG.domain.model.Invoice
import com.iberdrola.practicas2026.FranciscoPG.domain.model.SupplyType
import com.iberdrola.practicas2026.FranciscoPG.domain.repository.ConfigurationRepository
import com.iberdrola.practicas2026.FranciscoPG.domain.repository.InvoiceRepository
import com.iberdrola.practicas2026.FranciscoPG.data.local.InvoiceDao
import com.iberdrola.practicas2026.FranciscoPG.data.local.toDomain
import com.iberdrola.practicas2026.FranciscoPG.data.local.toEntity
import com.iberdrola.practicas2026.FranciscoPG.data.model.toDomain
import com.iberdrola.practicas2026.FranciscoPG.data.network.InvoiceApiService
import com.iberdrola.practicas2026.FranciscoPG.data.network.safeAwait
import kotlinx.coroutines.delay
import java.io.IOException
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
                delay((1000..3000).random().toLong())
                val response = mockApiService.getInvoicesCall(apiValue).safeAwait()
                if (response.code == 200 && response.data != null) {
                    val invoices = response.data
                        .filter { it.tipoSuministro.equals(apiValue, ignoreCase = true) }
                        .map { it.toDomain() }
                    Result.success(invoices)
                } else {
                    Result.failure(IOException("Error de API: ${response.error}"))
                }
            } else {
                if (forceRefresh) {
                    try {
                        val response = realApiService.getInvoices(apiValue)
                        if (response.code == 200 && response.data != null) {
                            val newInvoices = response.data
                                .filter { it.tipoSuministro.equals(apiValue, ignoreCase = true) }
                                .map { it.toDomain() }
                            invoiceDao.insertAll(newInvoices.map { it.toEntity() })
                        }
                    } catch (_: Exception) {
                        // API falló, no pasa nada: Room tiene los datos anteriores
                    }
                } else {
                    val cached = invoiceDao.getInvoicesBySupplyType(apiValue)
                    if (cached.isEmpty()) {
                        try {
                            val response = realApiService.getInvoices(apiValue)
                            if (response.code == 200 && response.data != null) {
                                val newInvoices = response.data
                                    .filter { it.tipoSuministro.equals(apiValue, ignoreCase = true) }
                                    .map { it.toDomain() }
                                invoiceDao.insertAll(newInvoices.map { it.toEntity() })
                            } else {
                                return Result.failure(IOException("Error de API: ${response.error}"))
                            }
                        } catch (e: Exception) {
                            return Result.failure(e)
                        }
                    }
                }

                val allCached = invoiceDao.getInvoicesBySupplyType(apiValue)
                Result.success(allCached.map { it.toDomain() })
            }
        } catch (e: Exception) {
            val fallback = invoiceDao.getInvoicesBySupplyType(apiValue)
            if (fallback.isNotEmpty()) {
                Result.success(fallback.map { it.toDomain() })
            } else {
                Result.failure(e)
            }
        }
    }
}
