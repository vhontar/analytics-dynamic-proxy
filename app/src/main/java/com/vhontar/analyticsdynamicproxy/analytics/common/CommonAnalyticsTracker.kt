package com.vhontar.analyticsdynamicproxy.analytics.common

import com.vhontar.analyticsdynamicproxy.analytics.console.ConsoleAnalyticsTracker
import com.vhontar.analyticsdynamicproxy.analytics.firebase.FirebaseAnalyticsTracker
import com.vhontar.common.AnalyticsTracker

class CommonAnalyticsTracker : AnalyticsTracker {
    private val consoleAnalyticsTracker = ConsoleAnalyticsTracker()
    private val firebaseAnalyticsTracker = FirebaseAnalyticsTracker()

    override fun trackEvent(name: String, params: Map<String, Any>?) {
        consoleAnalyticsTracker.trackEvent(name, params)
        firebaseAnalyticsTracker.trackEvent(name, params)
    }
}