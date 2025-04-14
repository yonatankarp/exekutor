package com.yonatankarp.exekutor.core.steps

import com.yonatankarp.exekutor.core.ExecutionContext
import io.github.oshai.kotlinlogging.KotlinLogging

internal object StepRegistry {
    private val steps = mutableListOf<Step<out ExecutionContext>>()
    private val logger = KotlinLogging.logger {}

    fun register(step: Step<out ExecutionContext>) {
        logger.info { "📦 Registering step: ${step.javaClass.simpleName}" }
        steps += step
    }

    fun all(): List<Step<out ExecutionContext>> = steps.toList()
}
