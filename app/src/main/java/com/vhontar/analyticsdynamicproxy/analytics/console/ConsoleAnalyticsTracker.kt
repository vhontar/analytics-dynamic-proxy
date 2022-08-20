package com.vhontar.analyticsdynamicproxy.analytics.console

import android.util.Log
import com.vhontar.common.AnalyticsTracker

class ConsoleAnalyticsTracker: AnalyticsTracker {
    override fun trackEvent(name: String, params: Map<String, Any>?) {
        if (params.isNullOrEmpty()) {
            Log.d("ConsoleAnalyticsTracker", name)
        } else {
            Log.d("ConsoleAnalyticsTracker", "$name($params)")
        }
    }
}