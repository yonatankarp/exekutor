package com.yonatankarp.exekutor.core.registry

import com.yonatankarp.exekutor.api.ExecutionContext
import com.yonatankarp.exekutor.api.Step
import io.github.oshai.kotlinlogging.KotlinLogging

/**
 * A global registry of all declared [Step]s.
 *
 * Steps are stored in a mutable list and can be resolved by plan builders.
 */
object StepRegistry {
    private val steps = mutableListOf<Step<out ExecutionContext>>()
    private val logger = KotlinLogging.logger {}

    /**
     * Registers a [Step] instance.
     */
    fun register(step: Step<out ExecutionContext>) {
        logger.info { "📦 Registering step: ${step.javaClass.simpleName}" }
        steps += step
    }

    /**
     * Returns all currently registered steps.
     */
    fun all(): List<Step<out ExecutionContext>> = steps.toList()
}

/**
 * Entry point for the step registration DSL.
 *
 * Use this function to declare all the steps your pipeline should expose.
 */
fun registerSteps(block: StepRegistryDsl.() -> Unit) {
    StepRegistryDsl().apply(block)
}
