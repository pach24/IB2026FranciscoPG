package com.iberdrola.practicas2026.FranciscoPG.presentation.invoices.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.iberdrola.practicas2026.FranciscoPG.R
import com.iberdrola.practicas2026.FranciscoPG.databinding.FragmentInvoiceListBinding
import com.iberdrola.practicas2026.FranciscoPG.databinding.ViewLatestInvoiceCardBinding
import com.iberdrola.practicas2026.FranciscoPG.presentation.invoices.adapter.InvoiceHistoryAdapter
import com.iberdrola.practicas2026.FranciscoPG.presentation.invoices.model.InvoiceListItem
import com.iberdrola.practicas2026.FranciscoPG.presentation.invoices.viewmodel.InvoiceUiState
import com.iberdrola.practicas2026.FranciscoPG.presentation.invoices.viewmodel.MyInvoicesViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class InvoiceListFragment : Fragment() {

    private var binding: FragmentInvoiceListBinding? = null
    private var supplyType: String = SUPPLY_LIGHT
    private val viewModel: MyInvoicesViewModel by viewModels()
    private lateinit var historyAdapter: InvoiceHistoryAdapter

    companion object {
        private const val ARG_SUPPLY_TYPE = "arg_supply_type"
        const val SUPPLY_LIGHT = "Luz"
        const val SUPPLY_GAS = "Gas"

        fun newInstance(supplyType: String) = InvoiceListFragment().apply {
            arguments = Bundle().apply {
                putString(ARG_SUPPLY_TYPE, supplyType)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supplyType = arguments?.getString(ARG_SUPPLY_TYPE) ?: SUPPLY_LIGHT
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentInvoiceListBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        setupObservers()
        // Llamar al repositorio real (Retromock por defecto)
        viewModel.fetchInvoices(supplyType)
    }

    private fun setupRecyclerView() {
        historyAdapter = InvoiceHistoryAdapter { _ ->
            viewModel.onFeatureNotAvailable()
        }
        binding?.rvInvoiceHistory?.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = historyAdapter
        }

        binding?.btnFilter?.setOnClickListener { viewModel.onFeatureNotAvailable() }
        binding?.cardLatestInvoice?.root?.setOnClickListener { viewModel.onFeatureNotAvailable() }
    }

    private fun setupObservers() {
        viewModel.uiState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is InvoiceUiState.Loading -> showLoading()
                is InvoiceUiState.Success -> showData(state.invoices)
                is InvoiceUiState.Error -> showError(state.message)
            }
        }

        viewModel.showDialogEvent.observe(viewLifecycleOwner) { shouldShow ->
            if (shouldShow == true) {
                showNotAvailableDialog()
                viewModel.onDialogHandled()
            }
        }
    }

    private fun showLoading() {
        binding?.let { b ->
            b.shimmerLatestInvoice?.visibility = View.VISIBLE
            b.shimmerLatestInvoice?.startShimmer()
            b.cardLatestInvoice.root.visibility = View.GONE

            b.shimmerStickyHeader?.visibility = View.VISIBLE
            b.shimmerStickyHeader?.startShimmer()
            b.groupStickyHeaderReal?.visibility = View.GONE

            b.shimmerHistory?.visibility = View.VISIBLE
            b.shimmerHistory?.startShimmer()
            b.rvInvoiceHistory.visibility = View.GONE
        }
    }

    private fun showData(invoices: List<com.iberdrola.practicas2026.FranciscoPG.domain.model.Invoice>) {
        binding?.let { b ->
            b.shimmerLatestInvoice?.stopShimmer()
            b.shimmerLatestInvoice?.visibility = View.GONE
            b.cardLatestInvoice.root.visibility = View.VISIBLE

            b.shimmerStickyHeader?.stopShimmer()
            b.shimmerStickyHeader?.visibility = View.GONE
            b.groupStickyHeaderReal?.visibility = View.VISIBLE

            b.shimmerHistory?.stopShimmer()
            b.shimmerHistory?.visibility = View.GONE
            b.rvInvoiceHistory.visibility = View.VISIBLE

            if (invoices.isNotEmpty()) {
                val latestInvoice = invoices.first()
                val cardBinding = ViewLatestInvoiceCardBinding.bind(b.cardLatestInvoice.root)

                cardBinding.tvCardTitle.text = "Última factura"
                cardBinding.tvInvoiceType.text = "Factura $supplyType"
                cardBinding.tvInvoiceAmount.text = "%.2f €".format(latestInvoice.amount)
                cardBinding.tvInvoiceDateRange.text = "${latestInvoice.periodStart} - ${latestInvoice.periodEnd}"
                cardBinding.tvInvoiceStatus.text = latestInvoice.status

                val iconRes = if (supplyType == SUPPLY_GAS) R.drawable.ic_gas else R.drawable.ic_light
                cardBinding.ivSupplyIcon.setImageResource(iconRes)
            }

            // Convertir Domain Models a InvoiceListItem para RecyclerView
            val listItems = invoicesToListItems(invoices)
            historyAdapter.submitList(listItems)
        }
    }

    // Dentro de InvoiceListFragment.kt
    // Añadir este método helper dentro de InvoiceListFragment
    private fun formatDateToSpanish(dateStr: String): String {
        val parts = dateStr.split("/")
        if (parts.size != 3) return dateStr

        val day = parts[0].toIntOrNull() ?: 0
        val month = parts[1].toIntOrNull() ?: 0
        val year = parts[2]

        val months = arrayOf(
            "", "enero", "febrero", "marzo", "abril", "mayo", "junio",
            "julio", "agosto", "septiembre", "octubre", "noviembre", "diciembre"
        )

        return "${day} de ${months[month]}"
    }

    // Método invoicesToListItems corregido
    private fun invoicesToListItems(invoices: List<com.iberdrola.practicas2026.FranciscoPG.domain.model.Invoice>): List<InvoiceListItem> {
        val grouped = invoices.groupBy { it.chargeDate.takeLast(4) }

        val result = mutableListOf<InvoiceListItem>()
        grouped.keys.sortedDescending().forEach { year ->
            result.add(InvoiceListItem.HeaderYear(year))
            grouped[year]?.forEach { invoice ->
                result.add(
                    InvoiceListItem.InvoiceItem(
                        id = invoice.id,
                        date = formatDateToSpanish(invoice.chargeDate), // CAMBIO: Usa el formateador
                        type = "Factura $supplyType",
                        amount = "%.2f €".format(invoice.amount),
                        statusText = invoice.status,
                        isPaid = invoice.status.contains("Pagada", ignoreCase = true)
                    )
                )
            }
        }
        return result
    }



    private fun showError(message: String) {
        binding?.let { b ->
            b.shimmerLatestInvoice?.stopShimmer()
            b.shimmerStickyHeader?.stopShimmer()
            b.shimmerHistory?.stopShimmer()
        }
        Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
    }

    private fun showNotAvailableDialog() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Información")
            .setMessage("Esta funcionalidad aún no está disponible.")
            .setPositiveButton("Aceptar", null)
            .show()
    }

    fun scrollToTop() {
        binding?.let { b ->
            b.rvInvoiceHistory?.smoothScrollToPosition(0)
            b.appBarLayout?.setExpanded(true, true)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}
