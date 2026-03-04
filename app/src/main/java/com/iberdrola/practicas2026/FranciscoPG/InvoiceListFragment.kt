package com.iberdrola.practicas2026.FranciscoPG

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.iberdrola.practicas2026.FranciscoPG.databinding.FragmentInvoiceListBinding
import com.iberdrola.practicas2026.FranciscoPG.databinding.ViewLatestInvoiceCardBinding
import com.iberdrola.practicas2026.FranciscoPG.presentation.invoices.MyInvoicesViewModel
import com.iberdrola.practicas2026.FranciscoPG.presentation.invoices.adapter.InvoiceHistoryAdapter
import com.iberdrola.practicas2026.FranciscoPG.presentation.invoices.model.InvoiceListItem
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class InvoiceListFragment : Fragment() {

    private var binding: FragmentInvoiceListBinding? = null
    private var supplyType: String = SUPPLY_LIGHT
    private val viewModel: MyInvoicesViewModel by viewModels()

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
        setupObservers()
        simulateNetworkLoad()
    }

    private fun setupObservers() {
        viewModel.showDialogEvent.observe(viewLifecycleOwner) { shouldShow ->
            if (shouldShow == true) {
                showNotAvailableDialog()
                viewModel.onDialogHandled()
            }
        }
    }

    private fun simulateNetworkLoad() {
        viewLifecycleOwner.lifecycleScope.launch {
            delay(2000)
            showRealData()
        }
    }

    private fun showRealData() {
        binding?.let { b ->
            b.shimmerLatestInvoice.stopShimmer()
            b.shimmerLatestInvoice.visibility = View.GONE
            b.cardLatestInvoice.root.visibility = View.VISIBLE

            b.shimmerStickyHeader.stopShimmer()
            b.shimmerStickyHeader.visibility = View.GONE
            b.groupStickyHeaderReal.visibility = View.VISIBLE

            b.shimmerHistory.stopShimmer()
            b.shimmerHistory.visibility = View.GONE
            b.rvInvoiceHistory.visibility = View.VISIBLE

            val cardBinding = ViewLatestInvoiceCardBinding.bind(b.cardLatestInvoice.root)

            cardBinding.tvCardTitle.text = "Última factura"
            cardBinding.tvInvoiceType.text = "Factura $supplyType"
            cardBinding.tvInvoiceAmount.text = "20,00 €"
            cardBinding.tvInvoiceDateRange.text = "01 feb. 2024 - 04 mar. 2024"
            cardBinding.tvInvoiceStatus.text = "Pendiente de Pago"

            b.cardLatestInvoice.root.setOnClickListener {
                viewModel.onFeatureNotAvailable()
            }

            b.btnFilter.setOnClickListener {
                viewModel.onFeatureNotAvailable()
            }

            val mockList = listOf(
                InvoiceListItem.HeaderYear("2024"),
                InvoiceListItem.InvoiceItem("1", "8 de marzo", "Factura $supplyType", "20,00 €", "Pendiente de Pago", false),
                InvoiceListItem.InvoiceItem("2", "6 de febrero", "Factura $supplyType", "20,10 €", "Pendiente de Pago", false),
                InvoiceListItem.InvoiceItem("3", "10 de enero", "Factura $supplyType", "22,50 €", "Pagada", true)
            )

            val historyAdapter = InvoiceHistoryAdapter { invoiceId ->
                viewModel.onFeatureNotAvailable()
            }

            b.rvInvoiceHistory.apply {
                layoutManager = LinearLayoutManager(requireContext())
                adapter = historyAdapter
            }

            historyAdapter.submitList(mockList)
        }
    }

    fun scrollToTop() {
        binding?.let { b ->
            b.rvInvoiceHistory.smoothScrollToPosition(0)
            b.appBarLayout.setExpanded(true, true)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    // Usamos MaterialAlertDialogBuilder para crear un diálogo estándar
    private fun showNotAvailableDialog() {
        MaterialAlertDialogBuilder(requireContext()) // O usa el estilo por defecto si no tienes uno
            .setTitle("Información")
            .setMessage("Esta funcionalidad aún no está disponible.")
            .setPositiveButton("Aceptar") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }
}
