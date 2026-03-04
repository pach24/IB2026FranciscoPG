package com.iberdrola.practicas2026.FranciscoPG.presentation.invoices.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.iberdrola.practicas2026.FranciscoPG.R
import com.iberdrola.practicas2026.FranciscoPG.databinding.ItemInvoiceHeaderBinding
import com.iberdrola.practicas2026.FranciscoPG.databinding.ItemInvoiceRowBinding
import com.iberdrola.practicas2026.FranciscoPG.presentation.invoices.model.InvoiceListItem

class InvoiceHistoryAdapter : ListAdapter<InvoiceListItem, RecyclerView.ViewHolder>(InvoiceDiffCallback()) {

    // Definimos identificadores constantes para nuestros ViewTypes
    companion object {
        private const val VIEW_TYPE_HEADER = 0
        private const val VIEW_TYPE_INVOICE = 1
    }

    override fun getItemViewType(position: Int): Int {
        // Devuelve el ViewType en función de la clase del elemento actual
        return when (getItem(position)) {
            is InvoiceListItem.HeaderYear -> VIEW_TYPE_HEADER
            is InvoiceListItem.InvoiceItem -> VIEW_TYPE_INVOICE
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            VIEW_TYPE_HEADER -> {
                val binding = ItemInvoiceHeaderBinding.inflate(inflater, parent, false)
                HeaderViewHolder(binding)
            }
            VIEW_TYPE_INVOICE -> {
                val binding = ItemInvoiceRowBinding.inflate(inflater, parent, false)
                InvoiceViewHolder(binding)
            }
            else -> throw IllegalArgumentException("ViewType desconocido")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = getItem(position)
        when (holder) {
            is HeaderViewHolder -> holder.bind(item as InvoiceListItem.HeaderYear)
            is InvoiceViewHolder -> holder.bind(item as InvoiceListItem.InvoiceItem)
        }
    }

    // --- ViewHolders ---

    inner class HeaderViewHolder(private val binding: ItemInvoiceHeaderBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: InvoiceListItem.HeaderYear) {
            binding.tvYearHeader.text = item.year
        }
    }

    inner class InvoiceViewHolder(private val binding: ItemInvoiceRowBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: InvoiceListItem.InvoiceItem) {
            binding.tvInvoiceDate.text = item.date
            binding.tvInvoiceType.text = item.type
            binding.tvInvoiceAmount.text = item.amount
            binding.tvInvoiceStatus.text = item.statusText

            // Lógica para cambiar el color del Pill (Chip) de estado
            val context = binding.root.context

            if (item.isPaid) {
                // Estado: PAGADA (Verde)
                binding.tvInvoiceStatus.setBackgroundResource(R.drawable.bg_status_paid)
                // Color corporativo iberdrola_dark_green o un verde oscuro específico
                binding.tvInvoiceStatus.setTextColor(ContextCompat.getColor(context, R.color.iberdrola_dark_green))
            } else {
                // Estado: PENDIENTE DE PAGO (Rojo)
                binding.tvInvoiceStatus.setBackgroundResource(R.drawable.bg_status_unpaid)
                // El rojo oscuro que usaste en tu XML original
                binding.tvInvoiceStatus.setTextColor(android.graphics.Color.parseColor("#B72727"))
            }
        }
    }

    // --- DiffUtil para optimización ---
    class InvoiceDiffCallback : DiffUtil.ItemCallback<InvoiceListItem>() {
        override fun areItemsTheSame(oldItem: InvoiceListItem, newItem: InvoiceListItem): Boolean {
            // Son el mismo elemento si ambos son Headers del mismo año, o Facturas con mismo ID
            return when {
                oldItem is InvoiceListItem.HeaderYear && newItem is InvoiceListItem.HeaderYear ->
                    oldItem.year == newItem.year
                oldItem is InvoiceListItem.InvoiceItem && newItem is InvoiceListItem.InvoiceItem ->
                    oldItem.id == newItem.id
                else -> false
            }
        }

        override fun areContentsTheSame(oldItem: InvoiceListItem, newItem: InvoiceListItem): Boolean {
            // Verifica si el contenido interno ha cambiado
            return oldItem == newItem
        }
    }
}
