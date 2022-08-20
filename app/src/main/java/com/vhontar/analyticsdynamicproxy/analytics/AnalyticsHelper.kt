package com.vhontar.analyticsdynamicproxy.analytics

import com.vhontar.analyticsdynamicproxy.analytics.console.ConsoleAnalyticsTracker
import com.vhontar.analyticsdynamicproxy.analytics.firebase.FirebaseAnalyticsTracker
import com.vhontar.common.AnalyticsService

// It's a helper class which is using temporarily instead of DI implementation
object AnalyticsHelper {
    fun createConsoleAnalytics(): Analytics {
        val service = AnalyticsService.Builder()
            .setAnalyticsTracker(ConsoleAnalyticsTracker())
            .useCaching()
            .build()

        return service.create(Analytics::class.java)
    }

    fun createFirebaseAnalytics(): Analytics {
        val service = AnalyticsService.Builder()
            .setAnalyticsTracker(FirebaseAnalyticsTracker())
            .build()

        return service.create(Analytics::class.java)
    }
}