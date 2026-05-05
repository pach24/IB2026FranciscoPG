package com.iberdrola.practicas2026.FranciscoPG.presentation.myinvoices.mapper
import com.iberdrola.practicas2026.FranciscoPG.presentation.R

import com.iberdrola.practicas2026.FranciscoPG.domain.model.Invoice
import com.iberdrola.practicas2026.FranciscoPG.domain.model.SupplyType
import com.iberdrola.practicas2026.FranciscoPG.presentation.myinvoices.model.InvoiceListItem
import com.iberdrola.practicas2026.FranciscoPG.presentation.myinvoices.model.InvoiceUiModel
import com.iberdrola.practicas2026.FranciscoPG.presentation.myinvoices.model.LatestInvoiceUiModel
import javax.inject.Inject

class InvoiceUiMapper @Inject constructor() {

    fun mapLatest(invoices: List<Invoice>, supplyType: SupplyType): LatestInvoiceUiModel? {
        val typeLabel = "Factura ${supplyTypeLabel(supplyType)}"
        val iconRes = supplyTypeIconRes(supplyType)
        return buildLatestInvoice(invoices.firstOrNull(), typeLabel, iconRes)
    }

    fun map(invoices: List<Invoice>, supplyType: SupplyType): InvoiceUiModel {
        val typeLabel = "Factura ${supplyTypeLabel(supplyType)}"
        val iconRes = supplyTypeIconRes(supplyType)
        return InvoiceUiModel(
            latestInvoice = buildLatestInvoice(invoices.firstOrNull(), typeLabel, iconRes),
            historyItems = buildHistoryItems(invoices, typeLabel)
        )
    }

    private fun buildLatestInvoice(
        invoice: Invoice?,
        typeLabel: String,
        iconRes: Int
    ): LatestInvoiceUiModel? = invoice?.let {
        LatestInvoiceUiModel(
            amount = formatAmount(it.amount),
            currencySymbol = CURRENCY_SYMBOL,
            dateRange = "${formatPeriodDate(it.periodStart)} - ${formatPeriodDate(it.periodEnd)}",
            supplyTypeLabel = typeLabel,
            statusText = it.status.apiValue,
            status = it.status,
            iconRes = iconRes
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
                        currencySymbol = CURRENCY_SYMBOL,
                        statusText = invoice.status.apiValue,
                        status = invoice.status
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

    private fun formatPeriodDate(date: String): String {
        val parts = date.split("/")
        if (parts.size != 3) return date

        val day = parts[0].padStart(2, '0')
        val month = parts[1].toIntOrNull() ?: return date
        if (month !in 1..12) return date
        val year = parts[2]

        return "$day ${MONTHS_SHORT[month]} $year"
    }

    private fun formatAmount(amount: Double): String =
        String.format(java.util.Locale("es", "ES"), "%.2f", amount)

    private fun supplyTypeLabel(supplyType: SupplyType): String = when (supplyType) {
        SupplyType.ELECTRICITY -> "Luz"
        SupplyType.GAS -> "Gas"
    }

    private fun supplyTypeIconRes(supplyType: SupplyType): Int = when (supplyType) {
        SupplyType.ELECTRICITY -> R.drawable.ic_light
        SupplyType.GAS -> R.drawable.ic_gas
    }

    private companion object {
        private const val CURRENCY_SYMBOL = "€"
        private val MONTHS = arrayOf(
            "", "enero", "febrero", "marzo", "abril", "mayo", "junio",
            "julio", "agosto", "septiembre", "octubre", "noviembre", "diciembre"
        )
        private val MONTHS_SHORT = arrayOf(
            "", "ene.", "feb.", "mar.", "abr.", "may.", "jun.",
            "jul.", "ago.", "sep.", "oct.", "nov.", "dic."
        )
    }
}
