package com.iberdrola.practicas2026.FranciscoPG

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.iberdrola.practicas2026.FranciscoPG.databinding.FragmentInvoiceListBinding
import com.iberdrola.practicas2026.FranciscoPG.databinding.ViewLatestInvoiceCardBinding
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
        // Usamos ?.let para asegurar que 'binding' no es nulo antes de acceder a sus vistas.
        // La variable 'b' dentro de las llaves representa al binding seguro.
        binding?.let { b ->

            // Detener animación y ocultar contenedor Skeleton
            b.shimmerLatestInvoice.stopShimmer()
            b.shimmerLatestInvoice.visibility = View.GONE

            // Mostrar el contenedor de la tarjeta real
            b.cardLatestInvoice.root.visibility = View.VISIBLE

            // Rellenar la tarjeta maquetada con datos reales/mockeados
            val cardBinding = ViewLatestInvoiceCardBinding.bind(b.cardLatestInvoice.root)

            cardBinding.tvCardTitle.text = "Última factura"
            cardBinding.tvInvoiceType.text = "Factura $supplyType" // Cambia "Luz" o "Gas" según el tab
            cardBinding.tvInvoiceAmount.text = "20,00 €"
            cardBinding.tvInvoiceDateRange.text = "01 feb. 2024 - 04 mar. 2024"
            cardBinding.tvInvoiceStatus.text = "Pendiente de Pago"

            // Aquí podrías cambiar el icono o los colores en función de 'supplyType' o estado de la factura
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // Liberamos la referencia al destruir la vista para evitar memory leaks
        binding = null
    }
}
