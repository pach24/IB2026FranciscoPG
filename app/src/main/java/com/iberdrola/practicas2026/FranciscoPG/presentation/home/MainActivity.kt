package com.iberdrola.practicas2026.FranciscoPG.presentation.home

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.iberdrola.practicas2026.FranciscoPG.R
import com.iberdrola.practicas2026.FranciscoPG.databinding.ActivityMainBinding
import com.iberdrola.practicas2026.FranciscoPG.presentation.invoices.view.MyInvoicesActivity
import com.iberdrola.practicas2026.FranciscoPG.presentation.main.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 1. Habilitamos la visualización edge-to-edge (el contenido dibuja debajo de las barras de sistema)
        enableEdgeToEdge()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupInsetsAndStatusBar()
        setupObservers()
        setupListeners()
    }

    private fun setupInsetsAndStatusBar() {
        // Escuchamos los insets en la raíz de la vista
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { _, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())

            // 1. El padding general (lateral y abajo) se aplica a todo el scroll
            // El padding top se lo damos SÓLO al TextView de saludo y la foto de perfil.
            // Al hacer esto, el fondo verde (ImageView) sigue dibujándose hasta el tope de la pantalla
            // cubriendo el espacio detrás del StatusBar, pero el texto y los iconos bajan lo necesario.

            // Padding lateral y bottom al ScrollView principal para no ocultar contenido
            binding.root.setPadding(systemBars.left, 0, systemBars.right, systemBars.bottom)

            // 2. Padding Top seguro: Se lo sumamos dinámicamente al margen superior del saludo.
            // Usamos la dimensión M3 que definiste en tu XML (m3_sys_spacing_4 que equivale a 32dp)
            val baseMarginTop = resources.getDimensionPixelSize(R.dimen.m3_sys_spacing_4)

            val greetingParams = binding.tvGreeting.layoutParams as android.view.ViewGroup.MarginLayoutParams
            greetingParams.topMargin = baseMarginTop + systemBars.top
            binding.tvGreeting.layoutParams = greetingParams

            insets
        }

        // Aseguramos que los iconos de la barra de estado se vean blancos sobre el fondo verde
        WindowInsetsControllerCompat(window, window.decorView).isAppearanceLightStatusBars = false
    }

    private fun setupObservers() {
        viewModel.userName.observe(this) { name ->
            binding.tvGreeting.text = getString(R.string.main_activity_greeting_user, name)
        }
    }

    private fun setupListeners() {
        // Usar include binding requiere acceder a la vista raíz (root)
        binding.cardFacturas.root.setOnClickListener {
            startActivity(Intent(this, MyInvoicesActivity::class.java))
        }
    }
}
