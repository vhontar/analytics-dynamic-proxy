package com.vhontar.analyticsdynamicproxy

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        (application as AppApplication).consoleAnalytics.trackAppStartedEvent()
        (application as AppApplication).consoleAnalytics.trackAppAndroidVersions(
            "1.0.0",
            "9.0"
        )
    }
}