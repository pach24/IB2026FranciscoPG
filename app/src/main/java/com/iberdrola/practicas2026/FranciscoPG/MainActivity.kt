package com.iberdrola.practicas2026.FranciscoPG

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import com.iberdrola.practicas2026.FranciscoPG.databinding.ActivityMainBinding
import com.iberdrola.practicas2026.FranciscoPG.presentation.main.MainViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding


    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupObservers()
        setupListeners()
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
