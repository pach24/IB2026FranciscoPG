package com.iberdrola.practicas2026.FranciscoPG

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.iberdrola.practicas2026.FranciscoPG.databinding.ActivityMyInvoicesBinding
import com.iberdrola.practicas2026.FranciscoPG.presentation.invoices.MyInvoicesViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MyInvoicesActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMyInvoicesBinding

    private val viewModel: MyInvoicesViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMyInvoicesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupListeners()
    }

    private fun setupListeners() {
        binding.btnBack.setOnClickListener {
            finish()
        }
    }
}
