package com.iberdrola.practicas2026.FranciscoPG

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.iberdrola.practicas2026.FranciscoPG.databinding.ActivityMyInvoicesBinding
import com.iberdrola.practicas2026.FranciscoPG.presentation.invoices.MyInvoicesViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MyInvoicesActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMyInvoicesBinding
    private val viewModel: MyInvoicesViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 1. Habilitar la visualización "edge-to-edge" recomendada por Android 15+
        enableEdgeToEdge()

        binding = ActivityMyInvoicesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupInsetsAndStatusBar()
        setupListeners()
    }

    // Configura los márgenes seguros y el color de los iconos de la barra
    private fun setupInsetsAndStatusBar() {
        // Aseguramos que la vista respete el notch y la barra de navegación del móvil
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { view, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // 2. Forzamos los iconos de la barra de estado a oscuros (batería, hora, etc)
        // ya que nuestro fondo del layout es blanco.
        WindowInsetsControllerCompat(window, window.decorView).isAppearanceLightStatusBars = true
    }

    private fun setupListeners() {
        binding.btnBack.setOnClickListener {
            finish()
        }
    }
}
