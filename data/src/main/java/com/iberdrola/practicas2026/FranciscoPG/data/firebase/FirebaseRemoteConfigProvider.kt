package com.iberdrola.practicas2026.FranciscoPG.data.firebase

import com.google.firebase.Firebase
import com.google.firebase.remoteconfig.remoteConfig
import com.google.firebase.remoteconfig.remoteConfigSettings
import com.iberdrola.practicas2026.FranciscoPG.domain.config.RemoteConfigProvider
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FirebaseRemoteConfigProvider @Inject constructor() : RemoteConfigProvider {

    private val remoteConfig = Firebase.remoteConfig

    init {

        val settings = remoteConfigSettings {
            minimumFetchIntervalInSeconds = 0
        }
        remoteConfig.setConfigSettingsAsync(settings)
        remoteConfig.setDefaultsAsync(
            mapOf(KEY_GAS_CONTRACTS_ENABLED to true)
        )
    }

    override suspend fun fetchAndActivate() {
        runCatching { remoteConfig.fetchAndActivate().await() }
    }

    override val isGasContractsEnabled: Boolean
        get() = remoteConfig.getBoolean(KEY_GAS_CONTRACTS_ENABLED)

    companion object {
        private const val KEY_GAS_CONTRACTS_ENABLED = "gas_contracts_enabled"
    }
}
