package com.iberdrola.practicas2026.FranciscoPG.domain.config

interface RemoteConfigProvider {

    suspend fun fetchAndActivate()
    val isGasContractsEnabled: Boolean
}
