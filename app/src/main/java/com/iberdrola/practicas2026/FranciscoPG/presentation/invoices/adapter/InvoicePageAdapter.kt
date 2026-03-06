package com.iberdrola.practicas2026.FranciscoPG.presentation.invoices.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.iberdrola.practicas2026.FranciscoPG.presentation.invoices.view.InvoiceListFragment


class InvoicePagerAdapter(activity: FragmentActivity) : FragmentStateAdapter(activity) {

    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> InvoiceListFragment.Companion.newInstance(InvoiceListFragment.Companion.SUPPLY_LIGHT)
            1 -> InvoiceListFragment.Companion.newInstance(InvoiceListFragment.Companion.SUPPLY_GAS)
            else -> throw IllegalArgumentException("Invalid position: $position")
        }
    }
}
