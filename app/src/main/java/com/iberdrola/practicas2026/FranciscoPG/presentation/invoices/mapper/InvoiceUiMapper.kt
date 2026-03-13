package com.iberdrola.practicas2026.FranciscoPG.presentation.invoices.mapper

import com.iberdrola.practicas2026.FranciscoPG.R
import com.iberdrola.practicas2026.FranciscoPG.domain.model.Invoice
import com.iberdrola.practicas2026.FranciscoPG.presentation.invoices.model.InvoiceListItem
import com.iberdrola.practicas2026.FranciscoPG.presentation.invoices.viewmodel.InvoiceUiModel
import com.iberdrola.practicas2026.FranciscoPG.presentation.invoices.viewmodel.LatestInvoiceUiModel
import javax.inject.Inject

class InvoiceUiMapper @Inject constructor() {

    fun map(invoices: List<Invoice>, supplyType: String): InvoiceUiModel {
        val supplyTypeLabel = supplyTypeLabel(supplyType)
        val typeLabel = "Factura $supplyTypeLabel"
        val iconRes = supplyTypeIconRes(supplyType)

        val latestInvoice = invoices.firstOrNull()?.let { invoice ->
            LatestInvoiceUiModel(
                amount = formatAmount(invoice.amount),
                dateRange = "${invoice.periodStart} - ${invoice.periodEnd}",
                supplyTypeLabel = typeLabel,
                status = invoice.status,
                isPaid = invoice.status.contains("Pagada", ignoreCase = true),
                iconRes = iconRes
            )
        }

        return InvoiceUiModel(
            latestInvoice = latestInvoice,
            historyItems = buildHistoryItems(invoices, typeLabel)
        )
    }

    private fun buildHistoryItems(
        invoices: List<Invoice>,
        typeLabel: String
    ): List<InvoiceListItem> {
        if (invoices.isEmpty()) return emptyList()

        return buildList {
            var currentYear: String? = null
            for (invoice in invoices) {
                val yearLabel = invoice.chargeDate.takeLast(4)

                if (yearLabel != currentYear) {
                    add(InvoiceListItem.HeaderYear(yearLabel))
                    currentYear = yearLabel
                }

                add(
                    InvoiceListItem.InvoiceItem(
                        id = invoice.id,
                        date = formatDateToSpanish(invoice.chargeDate),
                        type = typeLabel,
                        amount = formatAmount(invoice.amount),
                        statusText = invoice.status,
                        isPaid = invoice.status.contains("Pagada", ignoreCase = true)
                    )
                )
            }
        }
    }

    private fun formatDateToSpanish(chargeDate: String): String {
        val parts = chargeDate.split("/")
        if (parts.size != 3) return chargeDate

        val day = parts[0].toIntOrNull() ?: return chargeDate
        val month = parts[1].toIntOrNull() ?: return chargeDate
        if (month !in 1..12) return chargeDate

        return "$day de ${MONTHS[month]}"
    }

    private fun formatAmount(amount: Double): String = "%.2f €".format(amount)

    private fun supplyTypeLabel(supplyType: String): String {
        return if (supplyType.equals("gas", ignoreCase = true)) "Gas" else "Luz"
    }

    private fun supplyTypeIconRes(supplyType: String): Int {
        return if (supplyType.equals("gas", ignoreCase = true)) {
            R.drawable.ic_gas
        } else {
            R.drawable.ic_light
        }
    }

    private companion object {
        private val MONTHS = arrayOf(
            "", "enero", "febrero", "marzo", "abril", "mayo", "junio",
            "julio", "agosto", "septiembre", "octubre", "noviembre", "diciembre"
        )
    }
}