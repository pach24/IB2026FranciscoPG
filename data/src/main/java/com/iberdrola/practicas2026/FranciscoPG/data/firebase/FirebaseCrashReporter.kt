package com.iberdrola.practicas2026.FranciscoPG.data.firebase

import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.iberdrola.practicas2026.FranciscoPG.domain.analytics.CrashReporter
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FirebaseCrashReporter @Inject constructor() : CrashReporter {

    private val crashlytics = FirebaseCrashlytics.getInstance()

    override fun log(message: String) {
        crashlytics.log(message)
    }

    override fun forceCrash(): Nothing {
        crashlytics.log("user forced crash from MainScreen")
        throw RuntimeException("Forced test crash — 4ª entrega")
    }
}
