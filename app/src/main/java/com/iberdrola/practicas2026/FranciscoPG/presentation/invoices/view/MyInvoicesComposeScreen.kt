package com.iberdrola.practicas2026.FranciscoPG.presentation.invoices.view

import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.TabPosition
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
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.lerp
import androidx.compose.ui.unit.sp
import com.iberdrola.practicas2026.FranciscoPG.R
import com.iberdrola.practicas2026.FranciscoPG.presentation.feedback.FeedbackBottomSheetComposable
import kotlinx.coroutines.launch

private val InvoicesBold = FontFamily(Font(R.font.iberpangea_bold, FontWeight.Bold))
private val InvoicesRegular = FontFamily(Font(R.font.iberpangea_regular, FontWeight.Normal))

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun MyInvoicesComposeScreen(
    address: String,
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit = {},
    onTabReselected: (Int) -> Unit = {},
    lightTabContent: @Composable () -> Unit = {},
    gasTabContent: @Composable () -> Unit = {}
) {
    val tabs = listOf(stringResource(R.string.tab_light), stringResource(R.string.tab_gas))
    val pagerState = rememberPagerState(pageCount = { tabs.size })
    val scope = rememberCoroutineScope()

    // Estado del BottomSheet de feedback
    var showFeedbackSheet by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    // Intercepta el botón nativo de Android (gesture/hardware back)
    BackHandler {
        showFeedbackSheet = true
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
        /* BOTÓN ATRÁS */
        Row(
            modifier = Modifier
                .padding(start = 16.dp, top = 16.dp)
                .clickable { showFeedbackSheet = true }, // igual que el back nativo
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_arrow_back),
                contentDescription = null,
                tint = colorResource(R.color.iberdrola_dark_green),
                modifier = Modifier.size(24.dp)
            )
            Text(
                text = stringResource(R.string.my_invoices_back),
                modifier = Modifier.padding(start = 8.dp),
                textDecoration = TextDecoration.Underline,
                fontFamily = InvoicesBold,
                color = colorResource(R.color.iberdrola_dark_green),
                fontSize = 16.sp
            )
        }

        /* TÍTULOS */
        Text(
            text = stringResource(R.string.my_invoices_title),
            modifier = Modifier.padding(start = 16.dp, top = 24.dp, end = 16.dp),
            fontFamily = InvoicesBold,
            fontSize = 32.sp,
            color = colorResource(R.color.dark_grey_text)
        )

        Text(
            text = address,
            modifier = Modifier.padding(start = 16.dp, top = 8.dp, end = 16.dp),
            fontFamily = InvoicesBold,
            fontSize = 18.sp,
            color = colorResource(R.color.dark_grey_text)
        )

        /* TABS + INDICADOR */
        Box(modifier = Modifier.padding(top = 16.dp)) {
            Box(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(colorResource(R.color.tab_misfacturas))
            )

            ScrollableTabRow(
                selectedTabIndex = pagerState.currentPage,
                modifier = Modifier.fillMaxWidth(),
                containerColor = Color.Transparent,
                edgePadding = 16.dp,
                divider = {},
                indicator = { tabPositions ->
                    StretchTabIndicator(
                        tabPositions = tabPositions,
                        pagerState = pagerState,
                        isTabClick = isTabClick,
                        color = colorResource(R.color.iberdrola_dark_green)
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
                                fontSize = 14.sp,
                                color = if (isSelected) colorResource(R.color.color_text_high_emphasis) else Color.Gray
                            )
                        }
                    )
                }
            }
        }

        HorizontalPager(state = pagerState, modifier = Modifier.fillMaxSize()) { page ->
            when (page) {
                0 -> lightTabContent()
                1 -> gasTabContent()
            }
        }
    }

    // BottomSheet de feedback: se muestra tanto con el botón atrás personalizado
    // como con el gesto/botón nativo de Android gracias al BackHandler de arriba.
    if (showFeedbackSheet) {
        ModalBottomSheet(
            onDismissRequest = {
                showFeedbackSheet = false
                onBackClick()
            },
            sheetState = sheetState,
            // Transparent para que el Surface de FeedbackBottomSheetComposable
            // controle su propio color y bordes redondeados.
            containerColor = colorResource(R.color.color_surface),
            dragHandle = null,
        ) {
            FeedbackBottomSheetComposable(
                modifier = Modifier.navigationBarsPadding(),
                onFaceClick = {
                    scope.launch {
                        sheetState.hide()
                        showFeedbackSheet = false
                        onBackClick()
                    }
                },
                onLaterClick = {
                    scope.launch {
                        sheetState.hide()
                        showFeedbackSheet = false
                        onBackClick()
                    }
                }
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun StretchTabIndicator(
    tabPositions: List<TabPosition>,
    pagerState: PagerState,
    isTabClick: Boolean,
    color: Color,
    indicatorHeight: androidx.compose.ui.unit.Dp = 5.dp
) {
    val tabPadding = 20.dp

    val rawPosition = pagerState.currentPage + pagerState.currentPageOffsetFraction

    // ─── VELOCIDADES ──────────────────────────────────────────────────────────
    val clickDurationMs = 100  // click en tab: transición suave
    // ─────────────────────────────────────────────────────────────────────────

    val animatedPosition by animateFloatAsState(
        targetValue = rawPosition,
        animationSpec = if (isTabClick) {
            tween(durationMillis = clickDurationMs, easing = FastOutSlowInEasing)
        } else {
            tween(durationMillis = 0)
        },
        label = "tabIndicatorPosition"
    )

    val position = if (isTabClick) animatedPosition else rawPosition

    val fromIndex = position.toInt().coerceIn(0, tabPositions.lastIndex)
    val toIndex = (fromIndex + 1).coerceAtMost(tabPositions.lastIndex)
    val progress = (position - fromIndex).coerceIn(0f, 1f)

    val fromTab = tabPositions[fromIndex]
    val toTab = tabPositions[toIndex]

    val leadingProgress  = (progress * 2f).coerceAtMost(1f)
    val trailingProgress = ((progress - 0.5f) * 2f).coerceAtLeast(0f)

    val indicatorLeft  = lerp(fromTab.left  + tabPadding, toTab.left  + tabPadding, trailingProgress)
    val indicatorRight = lerp(fromTab.right - tabPadding, toTab.right - tabPadding, leadingProgress)

    Box(Modifier.fillMaxWidth().height(indicatorHeight)) {
        Box(
            Modifier
                .align(Alignment.BottomStart)
                .offset(x = indicatorLeft)
                .width(indicatorRight - indicatorLeft)
                .height(indicatorHeight)
                .background(color = color)
        )
    }
}