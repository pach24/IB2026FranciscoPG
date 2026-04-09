package com.iberdrola.practicas2026.FranciscoPG.presentation.myinvoices.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iberdrola.practicas2026.FranciscoPG.domain.model.FeedbackInteraction
import com.iberdrola.practicas2026.FranciscoPG.domain.usecase.IncrementExitCounterUseCase
import com.iberdrola.practicas2026.FranciscoPG.domain.usecase.ShouldShowFeedbackPromptUseCase
import com.iberdrola.practicas2026.FranciscoPG.domain.usecase.UpdateFeedbackInteractionUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class FeedbackSheetState {
    object Hidden : FeedbackSheetState()
    object Asking : FeedbackSheetState()
    object ThankYou : FeedbackSheetState()
}

@HiltViewModel
class FeedbackViewModel @Inject constructor(
    private val incrementExitCounterUseCase: IncrementExitCounterUseCase,
    private val shouldShowFeedbackPromptUseCase: ShouldShowFeedbackPromptUseCase,
    private val updateFeedbackInteractionUseCase: UpdateFeedbackInteractionUseCase
) : ViewModel() {

    private val _sheetState = MutableStateFlow<FeedbackSheetState>(FeedbackSheetState.Hidden)
    val sheetState: StateFlow<FeedbackSheetState> = _sheetState.asStateFlow()

    private val _navigateBack = MutableSharedFlow<Unit>(replay = 0, extraBufferCapacity = 1)
    val navigateBack: SharedFlow<Unit> = _navigateBack.asSharedFlow()

    private var explicitActionTaken = false

    fun onExitInvoices() {
        if (_sheetState.value != FeedbackSheetState.Hidden) return
        viewModelScope.launch {
            incrementExitCounterUseCase()
            val shouldShow = shouldShowFeedbackPromptUseCase()
            Log.d(TAG, "onExitInvoices -> shouldShowFeedback=$shouldShow")
            if (shouldShow) {
                explicitActionTaken = false
                _sheetState.value = FeedbackSheetState.Asking
            } else {
                _navigateBack.emit(Unit)
            }
        }
    }

    fun onFeedbackRated() {
        explicitActionTaken = true
        viewModelScope.launch {
            updateFeedbackInteractionUseCase(FeedbackInteraction.RATED)
            _sheetState.value = FeedbackSheetState.ThankYou
            delay(THANK_YOU_DELAY_MS)
            _sheetState.value = FeedbackSheetState.Hidden
            _navigateBack.emit(Unit)
        }
    }

    fun onFeedbackLater() {
        explicitActionTaken = true
        viewModelScope.launch {
            updateFeedbackInteractionUseCase(FeedbackInteraction.LATER)
            _sheetState.value = FeedbackSheetState.Hidden
            _navigateBack.emit(Unit)
        }
    }

    fun onSheetDismissed() {
        if (explicitActionTaken) return

        viewModelScope.launch {
            updateFeedbackInteractionUseCase(FeedbackInteraction.DISMISSED)
            _sheetState.value = FeedbackSheetState.Hidden
            _navigateBack.emit(Unit)
        }
    }


    companion object {
        private const val TAG = "FeedbackVM"
        private const val THANK_YOU_DELAY_MS = 1500L
    }
}