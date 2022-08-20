package com.vhontar.common

interface AnalyticsTracker {
    fun trackEvent(name: String, params: Map<String, Any>? = null)
}