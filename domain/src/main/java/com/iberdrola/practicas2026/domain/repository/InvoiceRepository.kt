// Archivo: InvoiceRepository.kt (Ubicación: domain/repository)
package com.iberdrola.practicas2026.FranciscoPG.domain.repository

import com.iberdrola.practicas2026.FranciscoPG.domain.model.Invoice

interface InvoiceRepository {
    suspend fun getInvoices(supplyType: String, forceRefresh: Boolean = false): Result<List<Invoice>>
}
