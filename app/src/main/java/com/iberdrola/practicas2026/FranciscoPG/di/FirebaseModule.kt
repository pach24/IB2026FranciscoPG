package com.iberdrola.practicas2026.FranciscoPG.di

import android.content.Context
import com.google.firebase.analytics.FirebaseAnalytics
import com.iberdrola.practicas2026.FranciscoPG.data.firebase.FirebaseAnalyticsTracker
import com.iberdrola.practicas2026.FranciscoPG.data.firebase.FirebaseCrashReporter
import com.iberdrola.practicas2026.FranciscoPG.data.firebase.FirebaseRemoteConfigProvider
import com.iberdrola.practicas2026.FranciscoPG.domain.analytics.AnalyticsTracker
import com.iberdrola.practicas2026.FranciscoPG.domain.analytics.CrashReporter
import com.iberdrola.practicas2026.FranciscoPG.domain.config.RemoteConfigProvider
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
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

    @Binds
    @Singleton
    abstract fun bindAnalyticsTracker(
        impl: FirebaseAnalyticsTracker
    ): AnalyticsTracker

    @Binds
    @Singleton
    abstract fun bindCrashReporter(
        impl: FirebaseCrashReporter
    ): CrashReporter

    companion object {
        @Provides
        @Singleton
        fun provideFirebaseAnalytics(@ApplicationContext context: Context): FirebaseAnalytics =
            FirebaseAnalytics.getInstance(context)
    }
}
