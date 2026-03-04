package com.iberdrola.practicas2026.FranciscoPG

import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import com.iberdrola.practicas2026.FranciscoPG.databinding.ActivityMyInvoicesBinding

@AndroidEntryPoint
class MyInvoicesActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMyInvoicesBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Modifica la barra de estado solo para esta Activity antes de inflar la vista
        setupStatusBar()

        binding = ActivityMyInvoicesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupListeners()
        setupViewPager()
    }

    private fun setupStatusBar() {
        // Obtenemos el controlador de la ventana (WindowInsetsController)
        val windowInsetsController = WindowCompat.getInsetsController(window, window.decorView)

        // Indicamos que los iconos de la barra de estado deben ser oscuros (true)
        // porque el fondo será blanco/claro
        windowInsetsController.isAppearanceLightStatusBars = true

        // Cambiamos el color de fondo de la barra de estado a blanco
        window.statusBarColor = Color.WHITE
    }

    private fun setupListeners() {
        binding.btnBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
    }

    private fun setupViewPager() {
        val pagerAdapter = InvoicePagerAdapter(this)
        binding.viewPager.adapter = pagerAdapter

        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            when (position) {
                0 -> tab.text = getString(R.string.tab_light)
                1 -> tab.text = getString(R.string.tab_gas)
            }
        }.attach()

        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {}
            override fun onTabUnselected(tab: TabLayout.Tab?) {}

            override fun onTabReselected(tab: TabLayout.Tab?) {
                val currentPosition = binding.viewPager.currentItem
                val fragmentManager = supportFragmentManager
                val fragment = fragmentManager.findFragmentByTag("f$currentPosition")

                if (fragment is InvoiceListFragment) {
                    fragment.scrollToTop()
                }
            }
        })
    }
}
