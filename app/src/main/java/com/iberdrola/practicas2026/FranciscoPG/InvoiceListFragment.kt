package com.iberdrola.practicas2026.FranciscoPG

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.iberdrola.practicas2026.FranciscoPG.databinding.FragmentInvoiceListBinding
import com.iberdrola.practicas2026.FranciscoPG.databinding.ViewLatestInvoiceCardBinding
import com.iberdrola.practicas2026.FranciscoPG.presentation.invoices.adapter.InvoiceHistoryAdapter
import com.iberdrola.practicas2026.FranciscoPG.presentation.invoices.model.InvoiceListItem
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class InvoiceListFragment : Fragment() {

    // Variable nullable para evitar el uso del operador !!
    private var binding: FragmentInvoiceListBinding? = null

    // Propiedad para saber si estamos en la pestaña "Luz" o "Gas"
    private var supplyType: String = SUPPLY_LIGHT

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
        // Inicializamos el binding. Retornamos binding?.root (que devuelve null de forma segura si fallara)
        binding = FragmentInvoiceListBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Al iniciar, el ShimmerFrameLayout se está mostrando por defecto en el XML.
        // Simulamos una carga de red de 2 segundos (Mock local).
        simulateNetworkLoad()
    }

    private fun simulateNetworkLoad() {
        viewLifecycleOwner.lifecycleScope.launch {
            // Simulamos 2 segundos de retardo
            delay(2000)

            // Cuando "llegan" los datos, ocultamos el skeleton y mostramos la card real
            showRealData()
        }
    }

    private fun showRealData() {
        binding?.let { b ->
            // 1. Tarjeta principal
            b.shimmerLatestInvoice.stopShimmer()
            b.shimmerLatestInvoice.visibility = View.GONE
            b.cardLatestInvoice.root.visibility = View.VISIBLE

            // 2. Sticky Header (Ocultar shimmer, mostrar grupo real)
            b.shimmerStickyHeader.stopShimmer()
            b.shimmerStickyHeader.visibility = View.GONE
            b.groupStickyHeaderReal.visibility = View.VISIBLE

            // 3. Lista inferior
            b.shimmerHistory.stopShimmer()
            b.shimmerHistory.visibility = View.GONE
            b.rvInvoiceHistory.visibility = View.VISIBLE

            // Rellenar la tarjeta maquetada con datos reales/mockeados
            val cardBinding = ViewLatestInvoiceCardBinding.bind(b.cardLatestInvoice.root)

            cardBinding.tvCardTitle.text = "Última factura"
            cardBinding.tvInvoiceType.text = "Factura $supplyType" // Cambia "Luz" o "Gas" según el tab
            cardBinding.tvInvoiceAmount.text = "20,00 €"
            cardBinding.tvInvoiceDateRange.text = "01 feb. 2024 - 04 mar. 2024"
            cardBinding.tvInvoiceStatus.text = "Pendiente de Pago"

            // --- HISTORIAL (LISTA) ---
            b.shimmerHistory.stopShimmer()
            b.shimmerHistory.visibility = View.GONE
            b.rvInvoiceHistory.visibility = View.VISIBLE

            val mockList = listOf(
                InvoiceListItem.HeaderYear("2024"),
                InvoiceListItem.InvoiceItem("1", "8 de marzo", "Factura $supplyType", "20,00 €", "Pendiente de Pago", false),
                InvoiceListItem.InvoiceItem("2", "6 de febrero", "Factura $supplyType", "20,10 €", "Pendiente de Pago", false),
                InvoiceListItem.InvoiceItem("3", "10 de enero", "Factura $supplyType", "22,50 €", "Pagada", true),

                InvoiceListItem.HeaderYear("2023"),
                InvoiceListItem.InvoiceItem("4", "12 de diciembre", "Factura $supplyType", "18,50 €", "Pagada", true),
                InvoiceListItem.InvoiceItem("5", "10 de noviembre", "Factura $supplyType", "25,30 €", "Pagada", true),
                InvoiceListItem.InvoiceItem("6", "8 de octubre", "Factura $supplyType", "21,00 €", "Pagada", true),
                InvoiceListItem.InvoiceItem("7", "9 de septiembre", "Factura $supplyType", "19,80 €", "Pagada", true),
                InvoiceListItem.InvoiceItem("8", "5 de agosto", "Factura $supplyType", "24,20 €", "Pagada", true),
                InvoiceListItem.InvoiceItem("9", "7 de julio", "Factura $supplyType", "26,10 €", "Pagada", true),

                InvoiceListItem.HeaderYear("2022"),
                InvoiceListItem.InvoiceItem("10", "15 de diciembre", "Factura $supplyType", "20,00 €", "Pagada", true),
                InvoiceListItem.InvoiceItem("11", "12 de noviembre", "Factura $supplyType", "22,10 €", "Pagada", true),
                InvoiceListItem.InvoiceItem("12", "10 de octubre", "Factura $supplyType", "18,90 €", "Pagada", true),
                InvoiceListItem.InvoiceItem("13", "14 de septiembre", "Factura $supplyType", "21,50 €", "Pagada", true),
                InvoiceListItem.InvoiceItem("14", "11 de agosto", "Factura $supplyType", "23,40 €", "Pagada", true),
                InvoiceListItem.InvoiceItem("15", "8 de julio", "Factura $supplyType", "27,20 €", "Pagada", true)
            )

            // 4. Configurar el RecyclerView y el Adapter
            val historyAdapter = InvoiceHistoryAdapter()

            b.rvInvoiceHistory.apply {
                layoutManager = LinearLayoutManager(requireContext())
                adapter = historyAdapter
            }

            // 5. Enviar la lista al Adapter
            historyAdapter.submitList(mockList)


        }
    }
    fun scrollToTop() {
        binding?.let { b ->
            // 1. Desplazar el RecyclerView al inicio
            b.rvInvoiceHistory.smoothScrollToPosition(0)

            // 2. Expandir el AppBarLayout para que se vea la tarjeta (True = animado)
            b.appBarLayout.setExpanded(true, true)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // Liberamos la referencia al destruir la vista para evitar memory leaks
        binding = null
    }
}
