package com.vhontar.common
import java.lang.reflect.InvocationHandler
import java.lang.reflect.Method

internal fun analyticsProxyInvocationHandler(
    analyticsTracker: AnalyticsTracker,
    withCache: Boolean
): InvocationHandler = when {
    withCache -> AnalyticsProxyInvocationHandlerWithCache(analyticsTracker)
    else -> AnalyticsProxyInvocationHandler(analyticsTracker)
}

// fail-fast: API should be written properly with all needed annotations
internal class AnalyticsProxyInvocationHandlerWithCache(
    private val analyticsTracker: AnalyticsTracker
): InvocationHandler {

    private val eventFactories : MutableMap<Method, EventFactory> = mutableMapOf()

    override fun invoke(proxy: Any, method: Method, args: Array<out Any>?): Any {
        val eventFactory = eventFactories.getOrPut(method) { newEventFactory(method, args?.size ?: 0) }
        val (name, params) = eventFactory.create(args)
        analyticsTracker.trackEvent(name, params)
        return Unit
    }

    private fun newEventFactory(method: Method, paramsSize: Int): EventFactory {
        val annotations = method.declaredAnnotations
        checkMethodAnnotations(annotations)
        val eventName = annotations.firstNotNullOf { it as? EventName }.value

        val argNames = Array(paramsSize) { index ->
            val paramAnnotations: Array<Annotation> = method.parameterAnnotations[index]
            checkParamAnnotations(paramAnnotations)
            paramAnnotations.firstNotNullOf { it as? EventParam }.name
        }
        return EventFactory(eventName, argNames)
    }

    private data class Event(val name: String, val params: Map<String, Any>?)

    private class EventFactory(
        private val eventName: String,
        private val argNames: Array<String>
    ) {
        fun create(args: Array<out Any>?): Event {
            if (args.isNullOrEmpty()) {
                check(argNames.isEmpty())
                return Event(eventName, emptyMap())
            }

            check(args.size == argNames.size)
            return Event(eventName, argNames.associateWithIndexed { i, _ -> args[i] })
        }
    }
}

// fail-fast: API should be written properly with all needed annotations
internal class AnalyticsProxyInvocationHandler(
    private val analyticsTracker: AnalyticsTracker
): InvocationHandler {
    override fun invoke(proxy: Any, method: Method, args: Array<out Any>?): Any {
        checkMethodAnnotations(method.annotations)
        val eventName: EventName = method.annotations.firstNotNullOf { it as? EventName }
        if (args.isNullOrEmpty()) {
            analyticsTracker.trackEvent(eventName.value)
        } else {
            val params = buildMap {
                repeat(args.size) { i ->
                    val eventParam = checkParamAnnotations(method.parameterAnnotations[i])
                        .firstNotNullOf { it as? EventParam }
                    put(eventParam.name, args[i])
                }
            }
            analyticsTracker.trackEvent(eventName.value, params)
        }

        return Unit
    }
}

private fun checkMethodAnnotations(annotations: Array<Annotation>?) {
    if (annotations.isNullOrEmpty()) error("No method annotations")
    if (annotations.none { it is EventName }) error("No EventName annotation")
}

private fun checkParamAnnotations(annotations: Array<Annotation>?): Array<Annotation> {
    if (annotations.isNullOrEmpty()) error("No param annotations")
    if (annotations.none { it is EventParam }) error("No EventParam annotation")
    return annotations
}