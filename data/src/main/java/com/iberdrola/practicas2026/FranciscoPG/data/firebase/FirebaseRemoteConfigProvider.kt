package com.iberdrola.practicas2026.FranciscoPG.data.firebase

import android.util.Log
import com.google.firebase.Firebase
import com.google.firebase.remoteconfig.ConfigUpdate
import com.google.firebase.remoteconfig.ConfigUpdateListener
import com.google.firebase.remoteconfig.FirebaseRemoteConfigException
import com.google.firebase.remoteconfig.remoteConfig
import com.google.firebase.remoteconfig.remoteConfigSettings

import com.iberdrola.practicas2026.FranciscoPG.domain.config.RemoteConfigProvider
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FirebaseRemoteConfigProvider @Inject constructor() : RemoteConfigProvider {

    private val remoteConfig = Firebase.remoteConfig

    private val _gasContractsEnabled = MutableStateFlow(true)
    override val gasContractsEnabledFlow: StateFlow<Boolean> = _gasContractsEnabled.asStateFlow()

    init {
        val settings = remoteConfigSettings {
            minimumFetchIntervalInSeconds = 0
        }
        remoteConfig.setConfigSettingsAsync(settings)
        remoteConfig.setDefaultsAsync(mapOf(KEY_GAS_CONTRACTS_ENABLED to true))

        remoteConfig.addOnConfigUpdateListener(object : ConfigUpdateListener {
            override fun onUpdate(configUpdate: ConfigUpdate) {
                Log.d(TAG, "onUpdate keys=${configUpdate.updatedKeys}")
                remoteConfig.activate().addOnSuccessListener {
                    val newValue = remoteConfig.getBoolean(KEY_GAS_CONTRACTS_ENABLED)
                    Log.d(TAG, "activated → $KEY_GAS_CONTRACTS_ENABLED=$newValue")
                    _gasContractsEnabled.value = newValue
                }
            }
            override fun onError(error: FirebaseRemoteConfigException) {
                Log.e(TAG, "realtime config error: ${error.message}")
            }
        })
    }

    override suspend fun fetchAndActivate() {
        runCatching { remoteConfig.fetchAndActivate().await() }
        _gasContractsEnabled.value = remoteConfig.getBoolean(KEY_GAS_CONTRACTS_ENABLED)
    }

    override val isGasContractsEnabled: Boolean
        get() = remoteConfig.getBoolean(KEY_GAS_CONTRACTS_ENABLED)

    companion object {
        private const val KEY_GAS_CONTRACTS_ENABLED = "gas_contracts_enabled"
        private const val TAG = "RemoteConfig"
    }
}
