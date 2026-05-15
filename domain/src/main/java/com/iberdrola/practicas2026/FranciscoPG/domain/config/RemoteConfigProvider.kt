package com.iberdrola.practicas2026.FranciscoPG.domain.config

import kotlinx.coroutines.flow.Flow

interface RemoteConfigProvider {

    suspend fun fetchAndActivate()
    val isGasContractsEnabled: Boolean
    val gasContractsEnabledFlow: Flow<Boolean>
}
