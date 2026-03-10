// Archivo: GetInvoicesUseCase.kt (Ubicación: domain/usecase)
package com.iberdrola.practicas2026.FranciscoPG.domain.usecase

import com.iberdrola.practicas2026.FranciscoPG.domain.model.Invoice
import com.iberdrola.practicas2026.FranciscoPG.domain.repository.InvoiceRepository
import javax.inject.Inject

class GetInvoicesUseCase @Inject constructor(
    private val repository: InvoiceRepository
) {
    suspend operator fun invoke(supplyType: String): Result<List<Invoice>> {
        return repository.getInvoices(supplyType)
    }
}
