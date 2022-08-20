package com.vhontar.common

import java.lang.reflect.Proxy
import kotlin.properties.Delegates

class AnalyticsService private constructor(
    private val analyticsTracker: AnalyticsTracker,
    private val withCache: Boolean = false
) {
    @Suppress("UNCHECKED_CAST")
    fun <T: Any> create(clazz: Class<T>): T {
        return Proxy.newProxyInstance(
            clazz.classLoader,
            arrayOf(clazz),
            analyticsProxyInvocationHandler(analyticsTracker, withCache)
        ) as T
    }

    class Builder {
        private var analyticsTracker: AnalyticsTracker by Delegates.notNull()
        private var withCache: Boolean = false

        fun setAnalyticsTracker(analyticsTracker: AnalyticsTracker): Builder {
            this.analyticsTracker = analyticsTracker
            return this
        }

        fun useCaching(): Builder {
            withCache = true
            return this
        }

        fun build(): AnalyticsService {
            return AnalyticsService(analyticsTracker)
        }
    }
}

inline fun <reified T: Any> AnalyticsService.create(): T = create(T::class.java)