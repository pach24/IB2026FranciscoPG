package com.iberdrola.practicas2026.FranciscoPG.presentation.home.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iberdrola.practicas2026.FranciscoPG.domain.usecase.GetMockModeUseCase
import com.iberdrola.practicas2026.FranciscoPG.domain.usecase.SetMockModeUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val getMockModeUseCase: GetMockModeUseCase,
    private val setMockModeUseCase: SetMockModeUseCase
) : ViewModel() {

    private val _userName = MutableStateFlow("")
    val userName: StateFlow<String> = _userName.asStateFlow()

    private val _useMock = MutableStateFlow(false)
    val useMock: StateFlow<Boolean> = _useMock.asStateFlow()

    // Evento one-shot para avisar a la UI del cambio de modo (null = no hay evento pendiente)
    private val _mockModeChanged = MutableStateFlow<Boolean?>(null)
    val mockModeChanged: StateFlow<Boolean?> = _mockModeChanged.asStateFlow()

    init {
        loadUserData()
        _useMock.value = getMockModeUseCase()
    }

    private fun loadUserData() {
        _userName.value = "FRANCISCO"
    }

    fun updateMockMode(isEnabled: Boolean) {
        viewModelScope.launch {
            setMockModeUseCase(isEnabled)
            _useMock.value = isEnabled
            _mockModeChanged.value = isEnabled
        }
    }

    fun onMockModeEventConsumed() {
        _mockModeChanged.value = null
    }
}