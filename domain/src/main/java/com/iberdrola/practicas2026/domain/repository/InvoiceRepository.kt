package com.iberdrola.practicas2026.FranciscoPG.domain.repository

import com.iberdrola.practicas2026.FranciscoPG.domain.model.Invoice

interface InvoiceRepository {
    suspend fun getInvoices(supplyType: String, useMock: Boolean): Result<List<Invoice>>
}
