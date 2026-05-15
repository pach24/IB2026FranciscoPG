package com.iberdrola.practicas2026.FranciscoPG.presentation.myinvoices.ui.components.list
import com.iberdrola.practicas2026.FranciscoPG.presentation.R

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.Dp
import com.iberdrola.practicas2026.FranciscoPG.presentation.theme.IberFontBold
import com.iberdrola.practicas2026.FranciscoPG.presentation.theme.IberFontRegular
import com.iberdrola.practicas2026.FranciscoPG.presentation.myinvoices.ui.preview.DevicePreview
import androidx.compose.ui.unit.sp
import com.iberdrola.practicas2026.FranciscoPG.domain.model.InvoiceStatus
import com.iberdrola.practicas2026.FranciscoPG.presentation.myinvoices.ui.components.ShimmerBox
import com.iberdrola.practicas2026.FranciscoPG.presentation.myinvoices.ui.components.ShimmerHost
import com.iberdrola.practicas2026.FranciscoPG.presentation.myinvoices.ui.components.StatusPillComposable
import com.iberdrola.practicas2026.FranciscoPG.presentation.theme.IberdrolaTheme
import com.iberdrola.practicas2026.FranciscoPG.presentation.theme.Spacing
import com.iberdrola.practicas2026.FranciscoPG.presentation.theme.IconSize
import com.iberdrola.practicas2026.FranciscoPG.presentation.theme.Radius
import com.iberdrola.practicas2026.FranciscoPG.presentation.theme.Stroke
import com.iberdrola.practicas2026.FranciscoPG.presentation.theme.Skeleton
import com.iberdrola.practicas2026.FranciscoPG.presentation.theme.TextSize

@Composable
fun LatestInvoiceCardComposable(
    amount: String,
    currencySymbol: String,
    dateRange: String,
    modifier: Modifier = Modifier,
    supplyType: String = "Factura Luz",
    status: String = "Pendiente de Pago",
    invoiceStatus: InvoiceStatus = InvoiceStatus.PENDING,
    iconRes: Int = R.drawable.ic_light,
    onClick: () -> Unit = {}
) {
    val colors = IberdrolaTheme.colors
    Card(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .border(
                width = Stroke.dp1,
                color = colors.iberdrolaDarkGreen,
                shape = RoundedCornerShape(Radius.dp16)
            ),
        shape = RoundedCornerShape(Radius.dp16),
        colors = CardDefaults.cardColors(containerColor = colors.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = Spacing.dp0)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(Spacing.dp15)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = stringResource(R.string.card_title_latest_invoice),
                        fontFamily = IberFontBold,
                        fontWeight = FontWeight.Bold,
                        fontSize = TextSize.sp14,
                        lineHeight = (14f * 1.4f).sp,
                        color = colors.darkGreyText
                    )
                    Spacer(modifier = Modifier.height(Spacing.dp4))
                    Text(
                        text = supplyType,
                        fontFamily = IberFontRegular,
                        fontSize = TextSize.sp12,
                        lineHeight = (12f * 1.4f).sp,
                        color = colors.lightGrey
                    )
                }

                Icon(
                    painter = painterResource(iconRes),
                    contentDescription = null,
                    tint = colors.iberdrolaDarkGreen,
                    modifier = Modifier
                        .size(IconSize.dp32)
                        .padding(end = Spacing.dp4)
                )
            }

            Spacer(modifier = Modifier.height(Spacing.dp14))

            Text(
                text = buildAnnotatedString {
                    append(amount)
                    append(" ")
                    withStyle(SpanStyle(fontSize = TextSize.sp16)) {
                        append(currencySymbol)
                    }
                },
                fontFamily = IberFontBold,
                fontWeight = FontWeight.Bold,
                fontSize = TextSize.sp24,
                lineHeight = (24f * 1.3f).sp,
                color = colors.darkGrey
            )

            Spacer(modifier = Modifier.height(Spacing.dp4))

            Text(
                text = dateRange,
                fontFamily = IberFontRegular,
                fontSize = TextSize.sp12,
                lineHeight = (12f * 1.4f).sp,
                color = colors.lightGrey
            )

            Spacer(modifier = Modifier.height(Spacing.dp10))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(Stroke.dp1)
                    .background(colors.divider)
            )

            Spacer(modifier = Modifier.height(Spacing.dp14))

            StatusPillComposable(
                text = status,
                status = invoiceStatus
            )
        }
    }
}

@Composable
fun SkeletonLatestInvoiceCardComposable(modifier: Modifier = Modifier) {
    val colors = IberdrolaTheme.colors
    Card(
        modifier = modifier
            .fillMaxWidth()
            .border(
                width = Stroke.dp1,
                color = colors.skeletonBackground,
                shape = RoundedCornerShape(Radius.dp16)
            ),
        shape = RoundedCornerShape(Radius.dp16),
        colors = CardDefaults.cardColors(containerColor = colors.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = Spacing.dp0)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(Spacing.dp15)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column {
                    SkeletonBox(
                        width = Skeleton.cardTitleW,
                        height = Skeleton.cardTitleH
                    )
                    Spacer(modifier = Modifier.height(Spacing.dp8))
                    SkeletonBox(
                        width = Skeleton.cardSubW,
                        height = Skeleton.cardSubH
                    )
                }

                SkeletonBox(
                    width = Skeleton.cardIconW,
                    height = Skeleton.cardIconH,
                    modifier = Modifier.padding(end = Spacing.dp5))
            }

            Spacer(modifier = Modifier.height(Spacing.dp21))
            SkeletonBox(
                width = Skeleton.cardAmountW,
                height = Skeleton.cardAmountH
            )

            Spacer(modifier = Modifier.height(Spacing.dp10))
            SkeletonBox(
                width = Skeleton.cardDatesW,
                height = Skeleton.cardDatesH
            )

            Spacer(modifier = Modifier.height(Spacing.dp14))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(Stroke.dp1)
                    .background(colors.strokeNeutral)
            )

            Spacer(modifier = Modifier.height(Spacing.dp12))
            ShimmerBox(
                width = Skeleton.cardStatusW,
                height = Skeleton.cardStatusH,
                shape = RoundedCornerShape(Radius.dp8)
            )
        }
    }
}

@Composable
private fun SkeletonBox(
    width: Dp,
    height: Dp,
    modifier: Modifier = Modifier
) {
    ShimmerBox(
        modifier = modifier,
        width = width,
        height = height,
        shape = RoundedCornerShape(Radius.dp4)
    )
}

@DevicePreview
@Composable
private fun PreviewLatestInvoiceCardComposable() {
    IberdrolaTheme {
        LatestInvoiceCardComposable(
            amount = "20,00",
            currencySymbol = "€",
            dateRange = "01 feb. 2024 - 04 mar. 2024",
            status = "Pagada",
            invoiceStatus = InvoiceStatus.PAID
        )
    }
}

@DevicePreview
@Composable
private fun PreviewSkeletonLatestInvoiceCardComposable() {
    IberdrolaTheme {
        SkeletonLatestInvoiceCardComposable()
    }
}

@DevicePreview
@Composable
private fun PreviewOverlaySkeletonOnCard() {
    IberdrolaTheme {
        ShimmerHost {
            Box {
                LatestInvoiceCardComposable(
                    amount = "20,00",
                    currencySymbol = "€",
                    dateRange = "01 feb. 2024 - 04 mar. 2024",
                    status = "Pagada",
                    invoiceStatus = InvoiceStatus.PAID
                )
                SkeletonLatestInvoiceCardComposable(
                    modifier = Modifier.alpha(0.8f)
                )
            }
        }
    }
}
