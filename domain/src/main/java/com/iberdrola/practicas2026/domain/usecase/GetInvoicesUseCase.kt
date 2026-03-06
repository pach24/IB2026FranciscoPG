package com.iberdrola.practicas2026.FranciscoPG.domain.usecase

import com.iberdrola.practicas2026.FranciscoPG.domain.model.Invoice
import com.iberdrola.practicas2026.FranciscoPG.domain.repository.InvoiceRepository
import javax.inject.Inject

class GetInvoicesUseCase @Inject constructor(
    private val repository: InvoiceRepository
) {
    // CAMBIO AQUI: Devuelve Result<List<Invoice>>
    suspend operator fun invoke(supplyType: String, useMock: Boolean): Result<List<Invoice>> {
        return repository.getInvoices(supplyType, useMock)
    }
}
