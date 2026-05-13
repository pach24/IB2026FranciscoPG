package com.iberdrola.practicas2026.FranciscoPG.presentation.myinvoices.ui.components.feedback
import com.iberdrola.practicas2026.FranciscoPG.presentation.R

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.waitForUpOrCancellation
import androidx.compose.foundation.indication
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.iberdrola.practicas2026.FranciscoPG.presentation.myinvoices.viewmodel.FeedbackSheetState
import com.iberdrola.practicas2026.FranciscoPG.presentation.theme.IberdrolaTheme
import com.iberdrola.practicas2026.FranciscoPG.presentation.theme.IconSize
import com.iberdrola.practicas2026.FranciscoPG.presentation.theme.Radius
import com.iberdrola.practicas2026.FranciscoPG.presentation.theme.Spacing
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FeedbackSheet(
    feedbackSheetState: FeedbackSheetState,
    onFaceClick: () -> Unit,
    onLaterClick: () -> Unit,
    onDismiss: () -> Unit
) {
    if (feedbackSheetState == FeedbackSheetState.Hidden) return

    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val colors = IberdrolaTheme.colors

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = colors.surface,
        dragHandle = {
            val rippleSource = remember { MutableInteractionSource() }
            val scope = rememberCoroutineScope()

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = Spacing.dp12),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(Radius.dp50))
                        .indication(rippleSource, ripple(bounded = true))
                        .pointerInput(Unit) {
                            awaitEachGesture {
                                val down = awaitFirstDown()
                                val press = PressInteraction.Press(down.position)
                                scope.launch { rippleSource.emit(press) }
                                val up = waitForUpOrCancellation()
                                scope.launch {
                                    if (up != null) rippleSource.emit(PressInteraction.Release(press))
                                    else rippleSource.emit(PressInteraction.Cancel(press))
                                }
                            }
                        }
                        .padding(horizontal = Spacing.dp12, vertical = Spacing.dp8),
                    contentAlignment = Alignment.Center
                ) {
                    Box(
                        modifier = Modifier
                            .size(width = 32.dp, height = 4.dp)
                            .background(
                                color = colors.textPrimary.copy(alpha = 0.4f),
                                shape = RoundedCornerShape(Radius.dp50)
                            )
                    )
                }
            }
        },
    ) {
        when (feedbackSheetState) {
            is FeedbackSheetState.Asking -> {
                FeedbackBottomSheetComposable(
                    modifier = Modifier.navigationBarsPadding(),
                    onFaceClick = onFaceClick,
                    onLaterClick = onLaterClick
                )
            }
            is FeedbackSheetState.ThankYou -> {
                ThankYouContent(modifier = Modifier.navigationBarsPadding())
            }
            else -> {}
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
