package com.vhontar.common

import java.lang.reflect.Proxy
import kotlin.properties.Delegates

class AnalyticsService private constructor(
    private val analyticsTracker: AnalyticsTracker
) {
    @Suppress("UNCHECKED_CAST")
    fun <T: Any> create(clazz: Class<T>): T {
        return Proxy.newProxyInstance(
            clazz.classLoader,
            arrayOf(clazz),
            AnalyticsProxyInvocationHandler(analyticsTracker)
        ) as T
    }

    class Builder {
        private var analyticsTracker: AnalyticsTracker by Delegates.notNull()

        fun setAnalyticsTracker(analyticsTracker: AnalyticsTracker): Builder {
            this.analyticsTracker = analyticsTracker
            return this
        }

        fun build(): AnalyticsService {
            return AnalyticsService(analyticsTracker)
        }
    }
}

inline fun <reified T: Any> AnalyticsService.create(): T = create(T::class.java)