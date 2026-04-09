// Archivo: InvoiceRepository.kt (Ubicación: domain/repository)
package com.iberdrola.practicas2026.FranciscoPG.domain.repository

import com.iberdrola.practicas2026.FranciscoPG.domain.model.Invoice
import com.iberdrola.practicas2026.FranciscoPG.domain.model.SupplyType

interface InvoiceRepository {
    suspend fun getInvoices(supplyType: SupplyType, forceRefresh: Boolean = false): Result<List<Invoice>>
}
