package com.iberdrola.practicas2026.FranciscoPG

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

// Punto de entrada para el grafo de inyección de dependencias a nivel de aplicación
@HiltAndroidApp
class IberdrolaApp : Application() {

    override fun onCreate() {
        super.onCreate()
    }
}
