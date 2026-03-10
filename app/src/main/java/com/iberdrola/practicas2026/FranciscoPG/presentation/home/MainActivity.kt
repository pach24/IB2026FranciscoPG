package com.iberdrola.practicas2026.FranciscoPG.presentation.home
import android.widget.TextView
import android.content.Intent
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.RelativeSizeSpan
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.google.android.material.snackbar.Snackbar
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

        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupInsetsAndStatusBar()
        setupObservers()
        setupListeners()
        setFormattedAmount("20,00 €")
    }

    private fun setupInsetsAndStatusBar() {
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { _, insets ->

            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())

            binding.root.setPadding(
                systemBars.left,
                0,
                systemBars.right,
                systemBars.bottom
            )

            val baseMarginTop = resources.getDimensionPixelSize(R.dimen.m3_sys_spacing_4)

            val greetingParams =
                binding.tvGreeting.layoutParams as android.view.ViewGroup.MarginLayoutParams

            greetingParams.topMargin = baseMarginTop + systemBars.top
            binding.tvGreeting.layoutParams = greetingParams

            insets
        }

        WindowInsetsControllerCompat(window, window.decorView)
            .isAppearanceLightStatusBars = false
    }

    private fun setupObservers() {

        viewModel.userName.observe(this) { name ->
            binding.tvGreeting.text =
                getString(R.string.main_activity_greeting_user, name)
        }

        viewModel.useMock.observe(this) { isMockEnabled ->
            if (binding.switchMockMode.isChecked != isMockEnabled) {
                binding.switchMockMode.isChecked = isMockEnabled
            }
        }

        viewModel.mockModeChanged.observe(this) { isMockEnabled ->
            showMockSnackbar(isMockEnabled)
        }
    }

    private fun setupListeners() {

        binding.cardFacturas.root.setOnClickListener {
            startActivity(Intent(this, MyInvoicesActivity::class.java))
        }

        binding.switchMockMode.setOnCheckedChangeListener { _, isChecked ->
            viewModel.updateMockMode(isChecked)
        }
    }
    private fun showMockSnackbar(isMockEnabled: Boolean) {

        val message = if (isMockEnabled) {
            "Modo MOCK activado: los datos son simulados"
        } else {
            "Modo REAL activado"
        }

        val snackbar = Snackbar.make(binding.root, message, Snackbar.LENGTH_LONG)

        val textView = snackbar.view.findViewById<TextView>(
            com.google.android.material.R.id.snackbar_text
        )

        textView.typeface = resources.getFont(R.font.iberpangea_bold)

        if (isMockEnabled) {
            snackbar.setBackgroundTint(getColor(R.color.snackbar))
            textView.setTextColor(getColor(R.color.black))
        } else {
            snackbar.setBackgroundTint(getColor(R.color.iberdrola_green))
            textView.setTextColor(getColor(R.color.white))
        }

        snackbar.show()
    }

    private fun setFormattedAmount(amount: String) {
        val spannable = SpannableString(amount)
        val euroIndex = amount.indexOf("€")

        if (euroIndex != -1) {
            spannable.setSpan(
                RelativeSizeSpan(0.8f), // Reduce el símbolo al 50% del tamaño (24sp -> 12sp)
                euroIndex,
                amount.length,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        }

        // Accedemos a través del binding del include
        binding.cardFacturas.tvValue.text = spannable
    }
}