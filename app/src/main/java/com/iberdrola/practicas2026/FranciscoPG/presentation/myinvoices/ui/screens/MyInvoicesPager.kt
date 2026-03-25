package com.iberdrola.practicas2026.FranciscoPG.presentation.myinvoices.ui.screens

import androidx.activity.compose.BackHandler
import com.iberdrola.practicas2026.FranciscoPG.presentation.myinvoices.ui.components.StretchTabIndicator
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import com.iberdrola.practicas2026.FranciscoPG.presentation.theme.IberFontBold
import com.iberdrola.practicas2026.FranciscoPG.presentation.theme.IberFontRegular
import com.iberdrola.practicas2026.FranciscoPG.R
import com.iberdrola.practicas2026.FranciscoPG.presentation.myinvoices.ui.components.BackButton
import com.iberdrola.practicas2026.FranciscoPG.presentation.myinvoices.ui.components.list.EmptyStateComposable
import com.iberdrola.practicas2026.FranciscoPG.presentation.myinvoices.ui.components.feedback.FeedbackSheet
import com.iberdrola.practicas2026.FranciscoPG.presentation.myinvoices.viewmodel.FeedbackSheetState
import com.iberdrola.practicas2026.FranciscoPG.presentation.theme.IberdrolaTheme
import kotlinx.coroutines.launch
import com.iberdrola.practicas2026.FranciscoPG.presentation.theme.Spacing
import com.iberdrola.practicas2026.FranciscoPG.presentation.theme.Stroke
import com.iberdrola.practicas2026.FranciscoPG.presentation.theme.TextSize

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MyInvoicesComposeScreen(
    address: String,
    modifier: Modifier = Modifier,
    feedbackSheetState: FeedbackSheetState = FeedbackSheetState.Hidden,
    isGlobalEmpty: Boolean = false,
    preferredTabIndex: Int = 0,
    onTabChanged: (Int) -> Unit = {},
    onBackClick: () -> Unit = {},
    onFeedbackFaceClick: () -> Unit = {},
    onFeedbackLaterClick: () -> Unit = {},
    onFeedbackDismiss: () -> Unit = {},
    onTabReselected: (Int) -> Unit = {},
    electricityTabContent: @Composable () -> Unit = {},
    gasTabContent: @Composable () -> Unit = {}
) {
    val tabs = listOf(stringResource(R.string.tab_light), stringResource(R.string.tab_gas))
    val pagerState = rememberPagerState(
        initialPage = 0,
        pageCount = { tabs.size }
    )
    val scope = rememberCoroutineScope()

    val colors = IberdrolaTheme.colors

    // Solo intercepta back cuando NO hay sheet visible;
    // si el sheet está abierto, su propio handler gestiona el back.
    BackHandler(enabled = feedbackSheetState == FeedbackSheetState.Hidden) {
        onBackClick()
    }

    // Detección swipe vs click para el indicador de tabs
    var isTabClick by remember { mutableStateOf(false) }
    LaunchedEffect(pagerState) {
        snapshotFlow {
            Triple(
                pagerState.settledPage,
                pagerState.targetPage,
                pagerState.currentPageOffsetFraction
            )
        }.collect { (settled, target, fraction) ->
            // Si la fracción se mueve y target==settled → es swipe manual → reset flag
            if (target == settled && fraction != 0f) {
                isTabClick = false
            }
        }
    }

    // Auto-seleccionar tab preferido (Luz vacía → Gas, o tras filtros)
    LaunchedEffect(preferredTabIndex) {
        if (pagerState.currentPage != preferredTabIndex) {
            pagerState.scrollToPage(preferredTabIndex)
        }
    }

    // Sincronizar tab activo con el padre
    LaunchedEffect(pagerState.settledPage) {
        onTabChanged(pagerState.settledPage)
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(colors.background)
            .statusBarsPadding()
    ) {
        /* BOTON ATRAS */
        BackButton(
            text = stringResource(R.string.my_invoices_back),
            onClick = onBackClick,
            color = colors.iberdrolaDarkGreen,
            fontSize = TextSize.sp16,
            modifier = Modifier.padding(start = Spacing.dp16, top = Spacing.dp16)
        )

        /* Mis facturas */
        Text(
            text = stringResource(R.string.my_invoices_title),
            modifier = Modifier.padding(start = Spacing.dp24, top = Spacing.dp24, end = Spacing.dp24),
            fontFamily = IberFontBold,
            fontSize = TextSize.sp32,
            color = colors.darkGreyText
        )
        //Dirección
        Text(
            text = address,
            modifier = Modifier.padding(start = Spacing.dp24, top = Spacing.dp8, end = Spacing.dp24),
            fontFamily = IberFontBold,
            fontSize = TextSize.sp18,
            color = colors.darkGreyText
        )

        if (isGlobalEmpty) {
            /* EMPTY STATE GLOBAL: sin facturas en ningun tab */
            EmptyStateComposable(
                title = stringResource(R.string.empty_state_global_title),
                subtitle = stringResource(R.string.empty_state_global_subtitle),
                modifier = Modifier.fillMaxSize()
            )
        } else {
            /* TABS + INDICADOR */
            Box(modifier = Modifier.padding(top = Spacing.dp16)) {
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .fillMaxWidth()
                        .height(Stroke.dp2)
                        .background(colors.tabMisFacturas)
                )

                ScrollableTabRow(
                    selectedTabIndex = pagerState.currentPage,
                    modifier = Modifier.fillMaxWidth(),
                    containerColor = Color.Transparent,
                    edgePadding = Spacing.dp16,
                    divider = {},
                    indicator = { tabPositions ->
                        StretchTabIndicator(
                            tabPositions = tabPositions,
                            pagerState = pagerState,
                            isTabClick = isTabClick,
                            color = colors.iberdrolaGreen
                        )
                    }
                ) {
                    tabs.forEachIndexed { index, title ->
                        val isSelected = pagerState.currentPage == index
                        Tab(
                            selected = isSelected,
                            onClick = {
                                if (pagerState.currentPage == index) {
                                    onTabReselected(index)
                                } else {
                                    isTabClick = true
                                    scope.launch { pagerState.animateScrollToPage(index) }
                                }
                            },
                            text = {
                                Text(
                                    text = title,
                                    fontFamily = if (isSelected) IberFontBold else IberFontRegular,
                                    fontSize = TextSize.sp14,
                                    color = if (isSelected) colors.textHighEmphasis else Color.Gray
                                )
                            }
                        )
                    }
                }
            }

            HorizontalPager(
                state = pagerState,
                modifier = Modifier.fillMaxSize(),
                beyondViewportPageCount = 1
            ) { page ->
                when (page) {
                    0 -> electricityTabContent()
                    1 -> gasTabContent()
                }
            }
        }
    }

    FeedbackSheet(
        feedbackSheetState = feedbackSheetState,
        onFaceClick = onFeedbackFaceClick,
        onLaterClick = onFeedbackLaterClick,
        onDismiss = onFeedbackDismiss
    )
}
