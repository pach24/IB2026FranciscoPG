package com.iberdrola.practicas2026.FranciscoPG.domain.usecase

import com.iberdrola.practicas2026.FranciscoPG.domain.model.Invoice
import com.iberdrola.practicas2026.FranciscoPG.domain.model.InvoiceSortCriteria
import javax.inject.Inject

class SortInvoicesUseCase @Inject constructor() {

    operator fun invoke(
        invoices: List<Invoice>,
        criteria: InvoiceSortCriteria = InvoiceSortCriteria.DATE_DESC
    ): List<Invoice> {
        return when (criteria) {
            InvoiceSortCriteria.DATE_DESC -> sortByDateDescending(invoices)
            InvoiceSortCriteria.DATE_ASC -> sortByDateAscending(invoices)
            InvoiceSortCriteria.AMOUNT_DESC -> sortByAmountDescending(invoices)
            InvoiceSortCriteria.AMOUNT_ASC -> sortByAmountAscending(invoices)
        }
    }

    private fun sortByDateDescending(invoices: List<Invoice>): List<Invoice> {
        return invoices
            .mapIndexed { index, invoice -> index to invoice }
            .sortedWith(
                compareByDescending<Pair<Int, Invoice>> { (_, invoice) ->
                    invoice.chargeDate
                }.thenBy { (index, _) -> index }
            )
            .map { (_, invoice) -> invoice }
    }

    private fun sortByDateAscending(invoices: List<Invoice>): List<Invoice> {
        return invoices
            .mapIndexed { index, invoice -> index to invoice }
            .sortedWith(
                compareBy<Pair<Int, Invoice>> { (_, invoice) ->
                    invoice.chargeDate
                }.thenBy { (index, _) -> index }
            )
            .map { (_, invoice) -> invoice }
    }

    private fun sortByAmountDescending(invoices: List<Invoice>): List<Invoice> {
        return invoices.sortedByDescending { it.amount }
    }

    private fun sortByAmountAscending(invoices: List<Invoice>): List<Invoice> {
        return invoices.sortedBy { it.amount }
    }
}
