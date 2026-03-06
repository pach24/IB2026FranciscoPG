package com.iberdrola.practicas2026.FranciscoPG.presentation.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.iberdrola.practicas2026.FranciscoPG.domain.usecase.GetMockModeUseCase
import com.iberdrola.practicas2026.FranciscoPG.domain.usecase.SetMockModeUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val getMockModeUseCase: GetMockModeUseCase,
    private val setMockModeUseCase: SetMockModeUseCase
) : ViewModel() {

    private val _userName = MutableLiveData<String>()
    val userName: LiveData<String> get() = _userName

    private val _useMock = MutableLiveData<Boolean>()
    val useMock: LiveData<Boolean> get() = _useMock

    // Evento para avisar a la UI del cambio de modo
    private val _mockModeChanged = MutableLiveData<Boolean>()
    val mockModeChanged: LiveData<Boolean> get() = _mockModeChanged

    init {
        loadUserData()
        _useMock.value = getMockModeUseCase()
    }

    private fun loadUserData() {
        _userName.value = "FRANCISCO"
    }

    fun updateMockMode(isEnabled: Boolean) {
        setMockModeUseCase(isEnabled)
        _useMock.value = isEnabled
        _mockModeChanged.value = isEnabled
    }
}