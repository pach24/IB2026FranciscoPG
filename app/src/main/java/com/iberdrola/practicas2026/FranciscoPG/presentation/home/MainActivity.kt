package com.iberdrola.practicas2026.FranciscoPG.presentation.home

import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.core.view.WindowInsetsControllerCompat
import com.iberdrola.practicas2026.FranciscoPG.R
import com.iberdrola.practicas2026.FranciscoPG.presentation.invoices.view.MyInvoicesActivity
import com.iberdrola.practicas2026.FranciscoPG.presentation.main.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        WindowInsetsControllerCompat(window, window.decorView)
            .isAppearanceLightStatusBars = false

        setContent {
            val userName by viewModel.userName.observeAsState("FRANCISCO")
            val useMock by viewModel.useMock.observeAsState(true)
            val mockModeChanged by viewModel.mockModeChanged.observeAsState()
            val snackbarHostState = remember { SnackbarHostState() }

            val mockModeMessage = if (useMock) {
                stringResource(R.string.main_mock_activated)
            } else {
                stringResource(R.string.main_mock_disabled)
            }
            val snackbarContainer = if (useMock) {
                colorResource(R.color.snackbar)
            } else {
                colorResource(R.color.iberdrola_green)
            }
            val snackbarContent = if (useMock) {
                colorResource(R.color.black)
            } else {
                colorResource(R.color.white)
            }

            LaunchedEffect(mockModeChanged) {
                if (mockModeChanged != null) {
                    snackbarHostState.showSnackbar(
                        message = mockModeMessage,
                        duration = SnackbarDuration.Long
                    )
                }
            }

            MainScreen(
                userName = userName,
                isMockEnabled = useMock,
                onMockModeChanged = viewModel::updateMockMode,
                // Make sure to use this@MainActivity so the Intent gets the right Context!
                onInvoicesCardClick = { startActivity(Intent(this@MainActivity, MyInvoicesActivity::class.java)) },
                snackbarHostState = snackbarHostState,
                snackbarContainerColor = snackbarContainer,
                snackbarContentColor = snackbarContent
            )
        } // <--- Notice that setContent is now properly closed down here!
    }
}