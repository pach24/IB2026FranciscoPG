package com.iberdrola.practicas2026.FranciscoPG.presentation.electronicinvoice.ui

import android.content.res.Configuration
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import com.iberdrola.practicas2026.FranciscoPG.R
import com.iberdrola.practicas2026.FranciscoPG.presentation.common.CloseTopBar
import com.iberdrola.practicas2026.FranciscoPG.presentation.common.StepBottomButtonBar
import com.iberdrola.practicas2026.FranciscoPG.presentation.common.StepProgressBar
import com.iberdrola.practicas2026.FranciscoPG.presentation.theme.IberFontBold
import com.iberdrola.practicas2026.FranciscoPG.presentation.theme.IberdrolaTheme
import com.iberdrola.practicas2026.FranciscoPG.presentation.theme.Spacing
import com.iberdrola.practicas2026.FranciscoPG.presentation.theme.TextSize
import kotlinx.coroutines.launch

private const val WIZARD_PAGE_COUNT = 3

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ElectronicInvoiceWizardScreen(
    email: String,
    legalAccepted: Boolean,
    isEmailValid: Boolean,
    verificationCode: String,
    onEmailChanged: (String) -> Unit,
    onLegalAcceptedChanged: (Boolean) -> Unit,
    onVerificationCodeChanged: (String) -> Unit,
    onResendCode: () -> Unit,
    onNavigateBack: () -> Unit
) {
    val colors = IberdrolaTheme.colors
    val scope = rememberCoroutineScope()
    val pagerState = rememberPagerState(
        initialPage = 0,
        pageCount = { WIZARD_PAGE_COUNT }
    )

    val currentPage = pagerState.currentPage
    val showScaffold = currentPage < 2

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colors.background)
            .padding(top = Spacing.dp24)
    ) {
        // Top bar: solo visible en páginas 0 y 1
        AnimatedVisibility(
            visible = showScaffold,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            Column {
                CloseTopBar(onClose = onNavigateBack)

                Text(
                    text = stringResource(R.string.activate_einvoice_title),
                    color = colors.textPrimary,
                    fontFamily = IberFontBold,
                    fontWeight = FontWeight.Bold,
                    fontSize = TextSize.sp23,
                    modifier = Modifier.padding(horizontal = Spacing.dp24)
                )

                Spacer(modifier = Modifier.height(Spacing.dp12))

                StepProgressBar(
                    currentStep = currentPage + 2, // page 0 = step 2, page 1 = step 3
                    totalSteps = 4,
                    modifier = Modifier.padding(horizontal = Spacing.dp12)
                )
            }
        }

        // Contenido del pager
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.weight(1f),
            userScrollEnabled = false
        ) { page ->
            when (page) {
                0 -> ActivateElectronicInvoiceContent(
                    email = email,
                    legalAccepted = legalAccepted,
                    isEmailValid = isEmailValid,
                    onEmailChanged = onEmailChanged,
                    onLegalAcceptedChanged = onLegalAcceptedChanged
                )
                1 -> ConfirmElectronicInvoiceContent(
                    verificationCode = verificationCode,
                    onVerificationCodeChanged = onVerificationCodeChanged,
                    onResendCode = onResendCode
                )
                2 -> {
                    // TODO: Pantalla custom step 4
                }
            }
        }

        // Bottom bar: solo visible en páginas 0 y 1
        AnimatedVisibility(
            visible = showScaffold,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            StepBottomButtonBar(
                onBack = {
                    if (currentPage == 0) {
                        onNavigateBack()
                    } else {
                        scope.launch { pagerState.animateScrollToPage(currentPage - 1) }
                    }
                },
                onNext = {
                    scope.launch { pagerState.animateScrollToPage(currentPage + 1) }
                },
                isNextEnabled = when (currentPage) {
                    0 -> isEmailValid && legalAccepted
                    1 -> false // Sin contenido aún
                    else -> false
                }
            )
        }
    }
}

@Preview(name = "Wizard - Light", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_NO)
@Preview(name = "Wizard - Dark", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun ElectronicInvoiceWizardScreenPreview() {
    IberdrolaTheme {
        ElectronicInvoiceWizardScreen(
            email = "",
            legalAccepted = false,
            isEmailValid = true,
            verificationCode = "",
            onEmailChanged = {},
            onLegalAcceptedChanged = {},
            onVerificationCodeChanged = {},
            onResendCode = {},
            onNavigateBack = {}
        )
    }
}
