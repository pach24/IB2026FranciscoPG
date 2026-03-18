package com.iberdrola.practicas2026.FranciscoPG.domain.usecase

import com.iberdrola.practicas2026.FranciscoPG.domain.model.Invoice
import com.iberdrola.practicas2026.FranciscoPG.domain.model.InvoiceSortCriteria
import com.iberdrola.practicas2026.FranciscoPG.domain.model.SupplyType
import com.iberdrola.practicas2026.FranciscoPG.domain.repository.InvoiceRepository
import javax.inject.Inject

class GetInvoicesUseCase @Inject constructor(
    private val repository: InvoiceRepository,
    private val sortInvoicesUseCase: SortInvoicesUseCase
) {
    suspend operator fun invoke(
        supplyType: SupplyType,
        forceRefresh: Boolean = false,
        sortCriteria: InvoiceSortCriteria = InvoiceSortCriteria.DATE_DESC
    ): Result<List<Invoice>> {
        return repository.getInvoices(supplyType, forceRefresh).map { invoices ->
            sortInvoicesUseCase(invoices, sortCriteria)
        }
    }
}
