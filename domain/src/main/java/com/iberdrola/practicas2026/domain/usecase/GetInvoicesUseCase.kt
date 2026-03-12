// Archivo: GetInvoicesUseCase.kt (Ubicación: domain/usecase)
package com.iberdrola.practicas2026.FranciscoPG.domain.usecase

import com.iberdrola.practicas2026.FranciscoPG.domain.model.Invoice
import com.iberdrola.practicas2026.FranciscoPG.domain.repository.InvoiceRepository
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import javax.inject.Inject

class GetInvoicesUseCase @Inject constructor(
    private val repository: InvoiceRepository
) {
    suspend operator fun invoke(supplyType: String): Result<List<Invoice>> {
        return repository.getInvoices(supplyType).map { invoices ->
            invoices
                .mapIndexed { index, invoice -> index to invoice }
                .sortedWith(
                    compareByDescending<Pair<Int, Invoice>> { (_, invoice) ->
                        parseChargeDate(invoice.chargeDate) ?: LocalDate.MIN
                    }.thenBy { (index, _) -> index }
                )
                .map { (_, invoice) -> invoice }
        }
    }

    private fun parseChargeDate(value: String): LocalDate? {
        return try {
            LocalDate.parse(value, DATE_FORMATTER)
        } catch (e: DateTimeParseException) {
            null
        }
    }

    private companion object {
        private val DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy")
    }
}
