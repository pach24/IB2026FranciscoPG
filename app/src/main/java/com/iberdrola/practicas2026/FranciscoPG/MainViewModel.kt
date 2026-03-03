package com.iberdrola.practicas2026.FranciscoPG.presentation.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

// Anotación necesaria para que Hilt pueda inyectar este ViewModel en la Activity
@HiltViewModel
class MainViewModel @Inject constructor() : ViewModel() {


    private val _userName = MutableLiveData<String>()
    val userName: LiveData<String> get() = _userName

    init {
        loadUserData()
    }

    // Simula la carga de datos del usuario (más adelante vendrá de un caso de uso / Room)
    private fun loadUserData() {
        _userName.value = "FRANCISCO"
    }
}
