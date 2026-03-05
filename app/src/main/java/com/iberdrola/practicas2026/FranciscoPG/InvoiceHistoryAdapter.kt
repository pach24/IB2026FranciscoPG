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

// Explicit constructor added to receive click events as lambda
class InvoiceHistoryAdapter(
    private val onInvoiceClicked: (String) -> Unit
) : ListAdapter<InvoiceListItem, RecyclerView.ViewHolder>(InvoiceDiffCallback()) {

    companion object {
        private const val VIEW_TYPE_HEADER = 0
        private const val VIEW_TYPE_INVOICE = 1
    }

    override fun getItemViewType(position: Int): Int {
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
            else -> throw IllegalArgumentException("Unknown ViewType")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = getItem(position)
        when (holder) {
            is HeaderViewHolder -> holder.bind(item as InvoiceListItem.HeaderYear)
            is InvoiceViewHolder -> holder.bind(item as InvoiceListItem.InvoiceItem)
        }
    }

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

            val context = binding.root.context

            // Trigger lambda from Presentation logic
            binding.root.setOnClickListener {
                onInvoiceClicked(item.id)
            }

            if (item.isPaid) {
                binding.tvInvoiceStatus.setBackgroundResource(R.drawable.bg_status_paid)
                binding.tvInvoiceStatus.setTextColor(ContextCompat.getColor(context, R.color.iberdrola_dark_green))
            } else {
                binding.tvInvoiceStatus.setBackgroundResource(R.drawable.bg_status_unpaid)
                binding.tvInvoiceStatus.setTextColor(android.graphics.Color.parseColor("#B72727"))
            }
        }
    }

    // Explicit type bounds applied to avoid compilation issues
    class InvoiceDiffCallback : DiffUtil.ItemCallback<InvoiceListItem>() {
        override fun areItemsTheSame(oldItem: InvoiceListItem, newItem: InvoiceListItem): Boolean {
            return when {
                oldItem is InvoiceListItem.HeaderYear && newItem is InvoiceListItem.HeaderYear ->
                    oldItem.year == newItem.year
                oldItem is InvoiceListItem.InvoiceItem && newItem is InvoiceListItem.InvoiceItem ->
                    oldItem.id == newItem.id
                else -> false
            }
        }

        override fun areContentsTheSame(oldItem: InvoiceListItem, newItem: InvoiceListItem): Boolean {
            return oldItem == newItem
        }
    }
}
