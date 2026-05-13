package com.iberdrola.practicas2026.FranciscoPG.data.firebase

import android.os.Bundle
import com.google.firebase.analytics.FirebaseAnalytics
import com.iberdrola.practicas2026.FranciscoPG.domain.analytics.AnalyticsTracker
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FirebaseAnalyticsTracker @Inject constructor(
    private val analytics: FirebaseAnalytics
) : AnalyticsTracker {

    override fun logScreenView(screenName: String) {
        val bundle = Bundle().apply {
            putString(FirebaseAnalytics.Param.SCREEN_NAME, screenName)
        }
        analytics.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW, bundle)
    }

    override fun logEvent(name: String, params: Map<String, String>) {
        val bundle = Bundle().apply {
            params.forEach { (k, v) -> putString(k, v) }
        }
        analytics.logEvent(name, bundle)
    }
}
