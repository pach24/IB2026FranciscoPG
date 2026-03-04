package com.iberdrola.practicas2026.FranciscoPG

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter



class InvoicePagerAdapter(activity: FragmentActivity) : FragmentStateAdapter(activity) {

    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> InvoiceListFragment.newInstance(InvoiceListFragment.SUPPLY_LIGHT)
            1 -> InvoiceListFragment.newInstance(InvoiceListFragment.SUPPLY_GAS)
            else -> throw IllegalArgumentException("Invalid position: $position")
        }
    }
}
