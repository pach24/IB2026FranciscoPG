package com.iberdrola.practicas2026.FranciscoPG.presentation.home.ui

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.iberdrola.practicas2026.FranciscoPG.R

private val IberFontRegular = FontFamily(Font(R.font.iberpangea_regular, FontWeight.Normal))
private val IberFontBold = FontFamily(Font(R.font.iberpangea_bold, FontWeight.Bold))

@Composable
fun MainScreen(
    userName: String,
    isMockEnabled: Boolean,
    onMockModeChanged: (Boolean) -> Unit,
    onInvoicesCardClick: () -> Unit,
    snackbarHostState: SnackbarHostState,
    snackbarContainerColor: Color,
    snackbarContentColor: Color
) {
    val scrollState = rememberScrollState()
    // Definimos cuánto queremos que el fondo verde sobresalga por debajo de la tarjeta
    val extraBackgroundHeight = 48.dp

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = colorResource(R.color.color_background),
        contentWindowInsets = WindowInsets(0.dp),
        snackbarHost = {
            SnackbarHost(
                hostState = snackbarHostState,
                modifier = Modifier.padding(bottom = 64.dp) // Ajusta este valor según necesites
            ) { data ->
                Snackbar(
                    snackbarData = data,
                    containerColor = snackbarContainerColor,
                    contentColor = snackbarContentColor
                )
            }
        }
    ) { paddingValues ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState)
                    .padding(bottom = dimensionResource(R.dimen.m3_sys_spacing_3))
            ) {
                // SECCIÓN SUPERIOR: Contiene el fondo dinámico y la cabecera
                Box(modifier = Modifier.fillMaxWidth()) {

                    // 1. Imagen de fondo (Z-Index inferior)
                    // matchParentSize hace que la imagen mida lo mismo que el Column de abajo
                    Box(modifier = Modifier.matchParentSize()) {
                        Image(
                            painter = painterResource(R.drawable.bg_header_curved),
                            contentDescription = null,
                            contentScale = ContentScale.FillBounds,
                            modifier = Modifier.fillMaxSize()
                        )
                    }

                    // 2. Contenido que define el tamaño del fondo
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .statusBarsPadding()
                    ) {
                        HeaderContent(userName)
                        PromoCard()

                        // Este Spacer es el "margen hacia abajo" que estira el fondo verde
                        Spacer(modifier = Modifier.height(extraBackgroundHeight))
                    }
                }

                // SECCIÓN INFERIOR: Contenido que "flota" sobre el fondo o va debajo
                // Usamos offset negativo para anular el empuje del Spacer anterior
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .offset(y = -extraBackgroundHeight)
                ) {
                    Text(
                        text = stringResource(R.string.main_activity_my_energy_title),
                        color = colorResource(R.color.color_text_primary),
                        fontFamily = IberFontBold,
                        fontWeight = FontWeight.Bold,
                        fontSize = dimensionResource(R.dimen.m3_sys_typescale_title2).value.sp,
                        modifier = Modifier.padding(
                            top = dimensionResource(R.dimen.m3_sys_spacing_4),
                            start = dimensionResource(R.dimen.m3_sys_spacing_3)
                        )
                    )

                    Row(
                        modifier = Modifier
                            .horizontalScroll(rememberScrollState())
                            .padding(
                                horizontal = dimensionResource(R.dimen.m3_sys_spacing_3),
                                vertical = dimensionResource(R.dimen.m3_sys_spacing_2)
                            )
                    ) {
                        ItemInvoiceCard(modifier = Modifier.clickable { onInvoicesCardClick() })
                    }

                    Spacer(modifier = Modifier.height(dimensionResource(R.dimen.m3_sys_spacing_2)))

                    // Ajuste opcional: Si el offset deja muy poco espacio al final del scroll,
                    // puedes añadir un Spacer positivo aquí para compensar.
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(start = dimensionResource(R.dimen.m3_sys_spacing_3))
                    ) {
                        Text(
                            text = stringResource(R.string.switch_mock_mode),
                            color = colorResource(R.color.color_text_primary),
                            fontFamily = IberFontRegular
                        )
                        Spacer(modifier = Modifier.width(dimensionResource(R.dimen.m3_sys_spacing_1)))
                        Switch(
                            checked = isMockEnabled,
                            onCheckedChange = onMockModeChanged,
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = colorResource(R.color.color_surface),
                                checkedTrackColor = colorResource(R.color.iberdrola_green),
                                checkedBorderColor = colorResource(R.color.iberdrola_green),
                                uncheckedThumbColor = colorResource(R.color.light_grey),
                                uncheckedTrackColor = colorResource(R.color.color_surface),
                                uncheckedBorderColor = colorResource(R.color.light_grey)
                            )
                        )
                    }

                    // Compensación del offset para que el scroll termine correctamente
                    Spacer(modifier = Modifier.height(extraBackgroundHeight))
                }
            }
        }
    }
}

@Composable
private fun HeaderContent(userName: String) {
    Column(modifier = Modifier.padding(horizontal = dimensionResource(R.dimen.m3_sys_spacing_3))) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = dimensionResource(R.dimen.m3_sys_spacing_4)),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(R.string.main_activity_greeting_user, userName),
                color = Color.White,
                fontFamily = IberFontBold,
                fontWeight = FontWeight.Bold,
                fontSize = dimensionResource(R.dimen.m3_sys_typescale_title1).value.sp
            )
            Box(
                modifier = Modifier
                    .size(dimensionResource(R.dimen.m3_comp_avatar_size))
                    .background(
                        color = colorResource(R.color.color_surface),
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_user_outline),
                    contentDescription = stringResource(R.string.main_activity_cd_profile),
                    tint = colorResource(R.color.color_brand_primary),
                    modifier = Modifier.size(dimensionResource(R.dimen.m3_comp_icon_size_custom_28))
                )
            }
        }

        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.m3_sys_spacing_1)))
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = stringResource(R.string.main_activity_address_placeholder),
                color = Color.White,
                fontFamily = IberFontBold,
                fontWeight = FontWeight.Bold,
                fontSize = dimensionResource(R.dimen.m3_sys_typescale_subhead).value.sp
            )
            Spacer(modifier = Modifier.width(dimensionResource(R.dimen.m3_sys_spacing_1)))
            Icon(
                painter = painterResource(R.drawable.ic_edit),
                contentDescription = stringResource(R.string.main_activity_cd_edit_address),
                tint = Color.White,
                modifier = Modifier.size(dimensionResource(R.dimen.m3_comp_icon_size_custom_20))
            )
        }
    }
}


@Composable
private fun PromoCard() {
    Card(
        colors = CardDefaults.cardColors(containerColor = colorResource(R.color.color_promo_background)),
        shape = RoundedCornerShape(dimensionResource(R.dimen.m3_comp_card_corner_radius)),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = dimensionResource(R.dimen.m3_sys_spacing_3))
            .padding(top = dimensionResource(R.dimen.m3_sys_spacing_4))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Min)
                .heightIn(min = dimensionResource(R.dimen.m3_comp_promo_card_min_height)),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(R.drawable.img_iberdrola_card),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .width(dimensionResource(R.dimen.m3_comp_promo_image_width))
                    .fillMaxHeight()
                    .padding(start = dimensionResource(R.dimen.m3_sys_spacing_2))
            )

            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(dimensionResource(R.dimen.m3_sys_spacing_3))
            ) {
                Text(
                    text = stringResource(R.string.main_activity_promo_title),
                    color = colorResource(R.color.color_text_primary),
                    fontFamily = IberFontBold,
                    fontWeight = FontWeight.Bold,
                    fontSize = dimensionResource(R.dimen.m3_sys_typescale_headline).value.sp,
                    lineHeight = 24.sp
                )

                Spacer(modifier = Modifier.height(dimensionResource(R.dimen.m3_sys_spacing_1)))

                Text(
                    text = stringResource(R.string.main_activity_promo_desc),
                    color = colorResource(R.color.color_text_primary),
                    fontFamily = IberFontRegular,
                    fontSize = dimensionResource(R.dimen.m3_sys_typescale_subhead).value.sp,
                    lineHeight = 20.sp
                )
            }
        }
    }
}

@Composable
fun ItemInvoiceCard(modifier: Modifier = Modifier) {
    val cardShape = RoundedCornerShape(dimensionResource(R.dimen.m3_comp_card_corner_radius))

    Box(
        modifier = modifier
            .padding(end = 16.dp)
            .background(color = colorResource(R.color.color_surface), shape = cardShape)
            .border(
                width = dimensionResource(R.dimen.m3_comp_stroke_width_thick),
                color = colorResource(R.color.color_divider),
                shape = cardShape
            )
            .padding(horizontal = 24.dp, vertical = 18.dp)
    ) {
        Column {
            Icon(
                painter = painterResource(R.drawable.file_chart_column),
                contentDescription = null,
                tint = colorResource(R.color.iberdrola_green),
                modifier = Modifier.size(28.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = buildAnnotatedString {
                    append("20,00")
                    withStyle(style = SpanStyle(fontSize = 19.2.sp)) { append("€") }
                },
                color = colorResource(R.color.dark_grey_text),
                fontFamily = IberFontBold,
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp,
                modifier = Modifier.padding(end = dimensionResource(R.dimen.m3_sys_spacing_1))
            )

            Text(
                text = stringResource(R.string.main_my_invoices_title),
                color = colorResource(R.color.dark_grey_text),
                fontFamily = IberFontRegular,
                fontSize = dimensionResource(R.dimen.m3_sys_typescale_headline_small).value.sp,
                minLines = 2,
                maxLines = 2,
                modifier = Modifier.padding(top = dimensionResource(R.dimen.m3_sys_spacing_1))
            )
        }
    }
}

@Preview(name = "Main Light", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_NO)
@Preview(name = "Main Dark", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun MainScreenPreview() {
    MaterialTheme {
        MainScreen(
            userName = "FRANCISCO",
            isMockEnabled = true,
            onMockModeChanged = {},
            onInvoicesCardClick = {},
            snackbarHostState = SnackbarHostState(),
            snackbarContainerColor = colorResource(R.color.snackbar),
            snackbarContentColor = colorResource(R.color.black)
        )
    }
}
