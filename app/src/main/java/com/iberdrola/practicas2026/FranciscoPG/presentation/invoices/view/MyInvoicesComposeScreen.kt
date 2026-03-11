package com.iberdrola.practicas2026.FranciscoPG.presentation.invoices.view

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.*
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.iberdrola.practicas2026.FranciscoPG.R

private val InvoicesRegular = FontFamily(Font(R.font.iberpangea_regular, FontWeight.Normal))
private val InvoicesBold = FontFamily(Font(R.font.iberpangea_bold, FontWeight.Bold))

@Composable
fun MyInvoicesComposeScreen(
    address: String,
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit = {},
    lightTabContent: @Composable () -> Unit = {},
    gasTabContent: @Composable () -> Unit = {}
) {

    var selectedTab by rememberSaveable { mutableIntStateOf(0) }

    val tabs = listOf(
        stringResource(R.string.tab_light),
        stringResource(R.string.tab_gas)
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(colorResource(R.color.color_background))
    ) {

        /* BACK BUTTON */

        Row(
            modifier = Modifier
                .padding(
                    start = dimensionResource(R.dimen.m3_sys_spacing_3),
                    top = dimensionResource(R.dimen.m3_sys_spacing_3)
                )
                .clickable { onBackClick() },
            verticalAlignment = Alignment.CenterVertically
        ) {

            Icon(
                painter = painterResource(R.drawable.ic_arrow_back),
                contentDescription = null,
                tint = colorResource(R.color.iberdrola_dark_green),
                modifier = Modifier.size(dimensionResource(R.dimen.m3_comp_icon_size))
            )

            Text(
                text = stringResource(R.string.my_invoices_back),
                modifier = Modifier.padding(start = dimensionResource(R.dimen.m3_sys_spacing_1)),
                textDecoration = TextDecoration.Underline,
                fontFamily = InvoicesBold,
                fontWeight = FontWeight.Bold,
                fontSize = dimensionResource(R.dimen.m3_sys_typescale_headline).value.sp,
                color = colorResource(R.color.iberdrola_dark_green)
            )
        }

        /* TITLE */

        Text(
            text = stringResource(R.string.my_invoices_title),
            modifier = Modifier.padding(
                start = dimensionResource(R.dimen.m3_sys_spacing_3),
                top = dimensionResource(R.dimen.m3_sys_spacing_3),
                end = dimensionResource(R.dimen.m3_sys_spacing_3)
            ),
            fontFamily = InvoicesBold,
            fontWeight = FontWeight.Bold,
            fontSize = dimensionResource(R.dimen.m3_sys_typescale_title1).value.sp,
            color = colorResource(R.color.dark_grey_text)
        )

        /* ADDRESS */

        Text(
            text = address,
            modifier = Modifier.padding(
                start = dimensionResource(R.dimen.m3_sys_spacing_3),
                top = dimensionResource(R.dimen.m3_sys_spacing_1),
                end = dimensionResource(R.dimen.m3_sys_spacing_3)
            ),
            fontFamily = InvoicesBold,
            fontWeight = FontWeight.Bold,
            fontSize = dimensionResource(R.dimen.m3_sys_typescale_headline_small).value.sp,
            color = colorResource(R.color.dark_grey_text)
        )

        /* TAB SECTION */

        Box(
            modifier = Modifier
                .padding(top = dimensionResource(R.dimen.m3_sys_spacing_0))
        ) {

            ScrollableTabRow(
                selectedTabIndex = selectedTab,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = dimensionResource(R.dimen.m3_sys_spacing_3)),
                containerColor = Color.Transparent,
                edgePadding = 0.dp,
                divider = {},
                indicator = { tabPositions ->

                    TabRowDefaults.SecondaryIndicator(
                        modifier = Modifier
                            .tabIndicatorOffset(tabPositions[selectedTab]),
                        height = dimensionResource(R.dimen.tab_indicator_height),
                        color = colorResource(R.color.iberdrola_dark_green)
                    )
                }
            ) {

                tabs.forEachIndexed { index, title ->

                    Tab(
                        selected = selectedTab == index,
                        onClick = { selectedTab = index },
                        selectedContentColor = colorResource(R.color.color_text_high_emphasis),
                        unselectedContentColor = colorResource(R.color.light_grey),
                        text = {

                            Text(
                                text = title,
                                fontFamily = InvoicesRegular,
                                fontSize = dimensionResource(R.dimen.m3_sys_typescale_label).value.sp
                            )
                        }
                    )
                }
            }

            /* DIVIDER */

            Box(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(horizontal = dimensionResource(R.dimen.m3_sys_spacing_3))
                    .fillMaxWidth()
                    .height(dimensionResource(R.dimen.tab_divider_height))
                    .background(colorResource(R.color.tab_misfacturas))
            )
        }

        /* TAB CONTENT */

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = dimensionResource(R.dimen.m3_sys_spacing_1))
        ) {

            if (selectedTab == 0) {
                lightTabContent()
            } else {
                gasTabContent()
            }
        }
    }
}

/* PREVIEW */

@Preview(name = "MyInvoices Screen - Light", showBackground = true)
@Preview(
    name = "MyInvoices Screen - Dark",
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    showBackground = true
)
@Composable
private fun PreviewMyInvoicesComposeScreen() {

    MaterialTheme {

        MyInvoicesComposeScreen(
            address = stringResource(R.string.my_invoices_mock_address),

            lightTabContent = {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(dimensionResource(R.dimen.m3_sys_spacing_3))
                        .verticalScroll(rememberScrollState())
                ) {

                    Text(
                        text = "Contenido tab Luz",
                        color = colorResource(R.color.dark_grey_text)
                    )
                }
            },

            gasTabContent = {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(dimensionResource(R.dimen.m3_sys_spacing_3))
                ) {

                    Text(
                        text = "Contenido tab Gas",
                        color = colorResource(R.color.dark_grey_text)
                    )
                }
            }
        )
    }
}