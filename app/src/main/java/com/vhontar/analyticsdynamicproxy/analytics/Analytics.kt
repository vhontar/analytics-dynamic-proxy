package com.vhontar.analyticsdynamicproxy.analytics

import com.vhontar.common.EventName
import com.vhontar.common.EventParam

interface Analytics {
    @EventName("app_started")
    fun trackAppStartedEvent()

    @EventName("versions")
    fun trackAppAndroidVersions(
        @EventParam("app_version") appVersion: String,
        @EventParam("android_version") androidVersion: String
    )
}