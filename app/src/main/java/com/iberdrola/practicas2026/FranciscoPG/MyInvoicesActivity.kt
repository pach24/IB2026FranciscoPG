package com.iberdrola.practicas2026.FranciscoPG

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.tabs.TabLayout

import com.iberdrola.practicas2026.FranciscoPG.InvoicePagerAdapter
import com.google.android.material.tabs.TabLayoutMediator
import com.iberdrola.practicas2026.FranciscoPG.R
import com.iberdrola.practicas2026.FranciscoPG.databinding.ActivityMyInvoicesBinding

class MyInvoicesActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMyInvoicesBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 1. Inicializar ViewBinding
        binding = ActivityMyInvoicesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 2. Configurar botones (Atrás)
        setupListeners()

        // 3. Configurar Paginador y Pestañas
        setupViewPager()
    }

    private fun setupListeners() {
        binding.btnBack.setOnClickListener {
            // Manejo correcto de retroceso en Android 13+
            onBackPressedDispatcher.onBackPressed()
        }
    }

    private fun setupViewPager() {
        // Inicializar el Adapter pasándole la Activity
        val pagerAdapter = InvoicePagerAdapter(this)

        // Asignar el Adapter al ViewPager2
        binding.viewPager.adapter = pagerAdapter

        // Vincular el TabLayout con el ViewPager2 usando TabLayoutMediator
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            when (position) {
                0 -> tab.text = getString(R.string.tab_light)
                1 -> tab.text = getString(R.string.tab_gas)
            }
        }.attach() // ¡Importante! Si olvidas .attach(), las pestañas no se conectarán

        // === AÑADE ESTO ===
        // Escuchar los toques en las pestañas
        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                // Ya lo gestiona ViewPager2 automáticamente, no hacemos nada extra.
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {}

            override fun onTabReselected(tab: TabLayout.Tab?) {
                // El usuario ha tocado la pestaña en la que YA estaba.
                // Buscamos el Fragmento activo actual y le pedimos que suba.

                // ViewPager2 con FragmentStateAdapter crea tags internos para los fragmentos
                val currentPosition = binding.viewPager.currentItem
                val fragmentManager = supportFragmentManager

                // Buscar el fragmento activo usando el tag predeterminado del ViewPager2
                val fragment = fragmentManager.findFragmentByTag("f$currentPosition")

                if (fragment is InvoiceListFragment) {
                    fragment.scrollToTop()
                }
            }
        })
    }

}





