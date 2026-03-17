package com.iberdrola.practicas2026.FranciscoPG.presentation.invoices.ui.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import com.iberdrola.practicas2026.FranciscoPG.presentation.invoices.ui.preview.DevicePreview
import androidx.compose.ui.unit.sp
import com.iberdrola.practicas2026.FranciscoPG.R
import com.iberdrola.practicas2026.FranciscoPG.presentation.theme.Spacing
import com.iberdrola.practicas2026.FranciscoPG.presentation.theme.IconSize
import com.iberdrola.practicas2026.FranciscoPG.presentation.theme.Radius
import com.iberdrola.practicas2026.FranciscoPG.presentation.theme.Stroke
import com.iberdrola.practicas2026.FranciscoPG.presentation.theme.Component
import com.iberdrola.practicas2026.FranciscoPG.presentation.theme.TextSize

private val FeedbackFontRegular = FontFamily(Font(R.font.iberpangea_regular, FontWeight.Normal))
private val FeedbackFontBold = FontFamily(Font(R.font.iberpangea_bold, FontWeight.Bold))

private data class FaceItem(@DrawableRes val icon: Int)

@Composable
fun FeedbackBottomSheetComposable(
    modifier: Modifier = Modifier,
    onFaceClick: () -> Unit = {},
    onLaterClick: () -> Unit = {}
) {
    val faces = listOf(
        FaceItem(R.drawable.ic_face_very_sad),
        FaceItem(R.drawable.ic_face_sad),
        FaceItem(R.drawable.ic_face_neutral),
        FaceItem(R.drawable.ic_face_happy),
        FaceItem(R.drawable.ic_face_very_happy)
    )

    Surface(
        modifier = modifier.fillMaxWidth(),
        color = colorResource(R.color.color_surface),
        shape = RoundedCornerShape(
            topStart = Radius.dp24,
            topEnd = Radius.dp24
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(Spacing.dp24),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .padding(top = Spacing.dp8)
                    .width(Component.dragW)
                    .height(Component.dragH)
                    .background(
                        color = colorResource(R.color.handler_color),
                        shape = RoundedCornerShape(Radius.dp8)
                    )
            )

            Text(
                text = stringResource(R.string.feedback_title),
                modifier = Modifier.padding(top = Spacing.dp24),
                fontFamily = FeedbackFontBold,
                fontWeight = FontWeight.Bold,
                fontSize = TextSize.sp20,
                color = colorResource(R.color.color_text_primary)
            )

            Text(
                text = stringResource(R.string.feedback_subtitle),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = Spacing.dp16),
                textAlign = TextAlign.Center,
                fontFamily = FeedbackFontRegular,
                fontSize = TextSize.sp16,
                lineHeight = (16f + 4).sp,
                color = colorResource(R.color.light_grey)
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = Spacing.dp24)
                    .height(Stroke.dp1)
                    .background(colorResource(R.color.color_stroke_neutral))
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = Spacing.dp32),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                faces.forEach { face ->
                    Box(
                        modifier = Modifier
                            .size(IconSize.dp48)
                            .clickable(onClick = onFaceClick),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            painter = painterResource(face.icon),
                            contentDescription = null,
                            tint = androidx.compose.ui.graphics.Color.Unspecified,
                            modifier = Modifier.size(IconSize.dp48)
                        )
                    }
                }
            }

            Text(
                text = stringResource(R.string.feedback_later),
                modifier = Modifier
                    .padding(
                        top = Spacing.dp32,
                        bottom = Spacing.dp8
                    )
                    .clickable(onClick = onLaterClick)
                    .padding(Spacing.dp8),
                textDecoration = TextDecoration.Underline,
                fontFamily = FeedbackFontBold,
                fontWeight = FontWeight.Bold,
                fontSize = TextSize.sp16,
                color = colorResource(R.color.iberdrola_dark_green)
            )

        }
    }
}

@DevicePreview
@Composable
private fun PreviewFeedbackBottomSheetComposable() {
    MaterialTheme {
        FeedbackBottomSheetComposable()
    }
}

