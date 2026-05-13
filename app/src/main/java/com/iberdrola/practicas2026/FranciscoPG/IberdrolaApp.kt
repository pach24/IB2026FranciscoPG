package com.iberdrola.practicas2026.FranciscoPG

import android.app.Application
import com.iberdrola.practicas2026.FranciscoPG.domain.config.RemoteConfigProvider
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltAndroidApp
class IberdrolaApp : Application() {

    @Inject lateinit var remoteConfig: RemoteConfigProvider

    private val appScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    override fun onCreate() {
        super.onCreate()
        // Refrescar Remote Config en background.
        appScope.launch { remoteConfig.fetchAndActivate() }
    }
}
