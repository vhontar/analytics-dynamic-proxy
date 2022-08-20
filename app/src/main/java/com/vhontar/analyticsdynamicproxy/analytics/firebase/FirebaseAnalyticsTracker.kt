package com.vhontar.analyticsdynamicproxy.analytics.firebase

import com.vhontar.common.AnalyticsTracker

class FirebaseAnalyticsTracker: AnalyticsTracker {
    override fun trackEvent(name: String, params: Map<String, Any>?) {
        // TODO use firebase api to send analytics there
    }
}