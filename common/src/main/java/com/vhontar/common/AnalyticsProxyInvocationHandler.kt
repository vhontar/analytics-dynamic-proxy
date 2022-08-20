package com.vhontar.common
import java.lang.reflect.InvocationHandler
import java.lang.reflect.Method

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