package com.iberdrola.practicas2026.FranciscoPG

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.iberdrola.practicas2026.FranciscoPG.databinding.ActivityMainBinding
import com.iberdrola.practicas2026.FranciscoPG.presentation.main.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 1. Habilitamos la visualización edge-to-edge
        enableEdgeToEdge()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupInsetsAndStatusBar()
        setupObservers()
        setupListeners()
    }

    // Configura los insets y asegura que los iconos de la barra de estado sean blancos
    private fun setupInsetsAndStatusBar() {
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { view, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())

            // 1. Aplicamos el padding de sistema solo a los laterales y abajo al contenedor general
            view.setPadding(systemBars.left, 0, systemBars.right, systemBars.bottom)

            // 2. Ajustamos la altura del fondo verde para que absorba el tamaño de la barra de estado
            // sin que el borde curvo inferior suba más de lo que debería.
            val headerParams = binding.headerBackground.layoutParams
            // Usamos el valor original definido en dimens (ej. 250dp) + la altura de la statusBar
            val originalHeaderHeight = resources.getDimensionPixelSize(R.dimen.main_activity_header_height)
            headerParams.height = originalHeaderHeight + systemBars.top
            binding.headerBackground.layoutParams = headerParams

            // 3. Bajamos los elementos (textos e icono) sumándole el espacio de la barra de estado al margen superior
            // Esto mantiene la fidelidad visual de Figma/Preview
            val greetingParams = binding.tvGreeting.layoutParams as android.view.ViewGroup.MarginLayoutParams
            val originalMarginTop = resources.getDimensionPixelSize(R.dimen.main_activity_greeting_margin_top)
            greetingParams.topMargin = originalMarginTop + systemBars.top
            binding.tvGreeting.layoutParams = greetingParams

            insets
        }

        // Forzamos los iconos de la barra a BLANCOS (isAppearanceLightStatusBars = false)
        WindowInsetsControllerCompat(window, window.decorView).isAppearanceLightStatusBars = false
    }


    private fun setupObservers() {
        viewModel.userName.observe(this) { name ->
            binding.tvGreeting.text = getString(R.string.main_activity_greeting_user, name)
        }
    }

    // Configura los eventos de click de la vista
    private fun setupListeners() {
        binding.cardFacturas.root.setOnClickListener {
            startActivity(Intent(this, MyInvoicesActivity::class.java))
        }
    }
}
