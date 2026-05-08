package com.iberdrola.practicas2026.FranciscoPG.di

import com.iberdrola.practicas2026.FranciscoPG.data.firebase.FirebaseRemoteConfigProvider
import com.iberdrola.practicas2026.FranciscoPG.domain.config.RemoteConfigProvider
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class FirebaseModule {

    @Binds
    @Singleton
    abstract fun bindRemoteConfigProvider(
        impl: FirebaseRemoteConfigProvider
    ): RemoteConfigProvider
}
