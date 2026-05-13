package com.iberdrola.practicas2026.FranciscoPG.domain.analytics

interface AnalyticsTracker {
    fun logScreenView(screenName: String)
    fun logEvent(name: String, params: Map<String, String> = emptyMap())
}
