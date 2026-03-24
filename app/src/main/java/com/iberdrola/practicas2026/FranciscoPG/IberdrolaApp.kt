package com.iberdrola.practicas2026.FranciscoPG

import android.app.Application
import android.util.Log
import dagger.hilt.android.HiltAndroidApp

// Punto de entrada para el grafo de inyección de dependencias a nivel de aplicación
@HiltAndroidApp
class IberdrolaApp : Application() {

    override fun onCreate() {
        super.onCreate()
        installRetromockCrashGuard()
    }

    /**
     * Retromock no maneja bien la cancelación de coroutines:
     * su callback `onFailure` intenta resumir una continuación ya cancelada,
     * lanzando "Already resumed" en el main thread.
     * Este handler atrapa solo ese crash específico y lo ignora.
     */
    private fun installRetromockCrashGuard() {
        val defaultHandler = Thread.getDefaultUncaughtExceptionHandler()
        Thread.setDefaultUncaughtExceptionHandler { thread, throwable ->
            if (throwable is IllegalStateException
                && throwable.message?.startsWith("Already resumed") == true
                && throwable.stackTrace.any { it.className.contains("retromock", ignoreCase = true) }
            ) {
                Log.w("IberdrolaApp", "Retromock cancellation crash suppressed", throwable)
            } else {
                defaultHandler?.uncaughtException(thread, throwable)
            }
        }
    }
}
