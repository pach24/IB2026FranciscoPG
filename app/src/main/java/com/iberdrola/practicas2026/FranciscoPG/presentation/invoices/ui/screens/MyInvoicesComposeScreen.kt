package com.iberdrola.practicas2026.FranciscoPG.presentation.invoices.ui.screens

import androidx.activity.compose.BackHandler
import com.iberdrola.practicas2026.FranciscoPG.presentation.invoices.ui.components.StretchTabIndicator
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
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
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import com.iberdrola.practicas2026.FranciscoPG.R
import com.iberdrola.practicas2026.FranciscoPG.presentation.invoices.ui.components.EmptyStateComposable
import com.iberdrola.practicas2026.FranciscoPG.presentation.invoices.ui.components.FeedbackBottomSheetComposable
import com.iberdrola.practicas2026.FranciscoPG.presentation.invoices.viewmodel.FeedbackSheetState
import kotlinx.coroutines.launch
import com.iberdrola.practicas2026.FranciscoPG.presentation.theme.Spacing
import com.iberdrola.practicas2026.FranciscoPG.presentation.theme.IconSize
import com.iberdrola.practicas2026.FranciscoPG.presentation.theme.Stroke
import com.iberdrola.practicas2026.FranciscoPG.presentation.theme.TextSize

private val InvoicesBold = FontFamily(Font(R.font.iberpangea_bold, FontWeight.Bold))
private val InvoicesRegular = FontFamily(Font(R.font.iberpangea_regular, FontWeight.Normal))

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun MyInvoicesComposeScreen(
    address: String,
    modifier: Modifier = Modifier,
    feedbackSheetState: FeedbackSheetState = FeedbackSheetState.Hidden,
    isGlobalEmpty: Boolean = false,
    defaultTabIndex: Int = 0,
    onBackClick: () -> Unit = {},
    onFeedbackFaceClick: () -> Unit = {},
    onFeedbackLaterClick: () -> Unit = {},
    onFeedbackDismiss: () -> Unit = {},
    onTabReselected: (Int) -> Unit = {},
    lightTabContent: @Composable () -> Unit = {},
    gasTabContent: @Composable () -> Unit = {}
) {
    val tabs = listOf(stringResource(R.string.tab_light), stringResource(R.string.tab_gas))
    val pagerState = rememberPagerState(
        initialPage = defaultTabIndex,
        pageCount = { tabs.size }
    )
    val scope = rememberCoroutineScope()

    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    // Auto-selección del tab con datos cuando cambia el defaultTabIndex
    var hasAutoSelected by remember { mutableStateOf(false) }
    LaunchedEffect(defaultTabIndex) {
        if (!hasAutoSelected && defaultTabIndex != pagerState.currentPage) {
            pagerState.scrollToPage(defaultTabIndex)
            hasAutoSelected = true
        }
    }

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

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(colorResource(R.color.color_background))
            .statusBarsPadding()
    ) {
        /* BOTON ATRAS */
        Row(
            modifier = Modifier
                .padding(start = Spacing.dp16, top = Spacing.dp16)
                .clickable { onBackClick() },
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_arrow_back),
                contentDescription = null,
                tint = colorResource(R.color.iberdrola_dark_green),
                modifier = Modifier.size(IconSize.dp24)
            )
            Text(
                text = stringResource(R.string.my_invoices_back),
                modifier = Modifier.padding(start = Spacing.dp8),
                textDecoration = TextDecoration.Underline,
                fontFamily = InvoicesBold,
                color = colorResource(R.color.iberdrola_dark_green),
                fontSize = TextSize.sp16            )
        }

        /* TITULOS */
        Text(
            text = stringResource(R.string.my_invoices_title),
            modifier = Modifier.padding(start = Spacing.dp16, top = Spacing.dp24, end = Spacing.dp16),
            fontFamily = InvoicesBold,
            fontSize = TextSize.sp32,
            color = colorResource(R.color.dark_grey_text)
        )

        Text(
            text = address,
            modifier = Modifier.padding(start = Spacing.dp16, top = Spacing.dp8, end = Spacing.dp16),
            fontFamily = InvoicesBold,
            fontSize = TextSize.sp18,
            color = colorResource(R.color.dark_grey_text)
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
                        .height(Stroke.dp1)
                        .background(colorResource(R.color.tab_misfacturas))
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
                            color = colorResource(R.color.iberdrola_green)
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
                                    fontFamily = if (isSelected) InvoicesBold else InvoicesRegular,
                                    fontSize = TextSize.sp14,
                                    color = if (isSelected) colorResource(R.color.color_text_high_emphasis) else Color.Gray
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
                    0 -> lightTabContent()
                    1 -> gasTabContent()
                }
            }
        }
    }

    if (feedbackSheetState != FeedbackSheetState.Hidden) {
        ModalBottomSheet(
            onDismissRequest = onFeedbackDismiss,
            sheetState = sheetState,
            containerColor = colorResource(R.color.color_surface),
            dragHandle = null,
        ) {
            when (feedbackSheetState) {
                is FeedbackSheetState.Asking -> {
                    FeedbackBottomSheetComposable(
                        modifier = Modifier.navigationBarsPadding(),
                        onFaceClick = onFeedbackFaceClick,
                        onLaterClick = onFeedbackLaterClick
                    )
                }
                is FeedbackSheetState.ThankYou -> {
                    ThankYouContent(modifier = Modifier.navigationBarsPadding())
                }
                else -> {}
            }
        }
    }
}

@Composable
private fun ThankYouContent(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(Spacing.dp32),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            painter = painterResource(R.drawable.ic_face_very_happy),
            contentDescription = null,
            tint = Color.Unspecified,
            modifier = Modifier.size(IconSize.dp48)
        )
        Spacer(modifier = Modifier.height(Spacing.dp16))
        Text(
            text = stringResource(R.string.feedback_thank_you),
            style = MaterialTheme.typography.titleMedium,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(Spacing.dp24))
    }
}
