package com.vhontar.analyticsdynamicproxy

import android.app.Application
import com.vhontar.analyticsdynamicproxy.analytics.AnalyticsHelper

class AppApplication: Application() {
    // must be used through DI, but for simplicity...
    val consoleAnalytics = AnalyticsHelper.createConsoleAnalytics()
}