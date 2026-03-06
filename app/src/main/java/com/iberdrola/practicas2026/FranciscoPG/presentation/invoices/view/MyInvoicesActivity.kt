package com.iberdrola.practicas2026.FranciscoPG.presentation.invoices.view

import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import com.iberdrola.practicas2026.FranciscoPG.databinding.ActivityMyInvoicesBinding
import com.iberdrola.practicas2026.FranciscoPG.R
import com.iberdrola.practicas2026.FranciscoPG.presentation.feedback.FeedbackBottomSheetFragment
import com.iberdrola.practicas2026.FranciscoPG.presentation.invoices.adapter.InvoicePagerAdapter

@AndroidEntryPoint
class MyInvoicesActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMyInvoicesBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setupSystemBars()

        binding = ActivityMyInvoicesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        applyWindowInsets()
        setupListeners()
        setupViewPager()
        setupBackNavigation()
    }

    // Configura la barra de estado respetando el modo oscuro/claro del sistema
    private fun setupSystemBars() {
        enableEdgeToEdge()
    }

    // Aplica los insets de la ventana para que la vista no quede oculta por la barra de estado
    private fun applyWindowInsets() {
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { view, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun setupListeners() {
        binding.btnBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
    }

    // Intercepta el evento de retroceso para mostrar el BottomSheet temporalmente
    private fun setupBackNavigation() {
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                val bottomSheet = FeedbackBottomSheetFragment.newInstance()
                bottomSheet.show(supportFragmentManager, FeedbackBottomSheetFragment.TAG)
            }
        })
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
