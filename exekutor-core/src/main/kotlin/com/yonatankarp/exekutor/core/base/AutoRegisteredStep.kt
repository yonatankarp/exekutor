package com.yonatankarp.exekutor.core.base

import com.yonatankarp.exekutor.core.api.ExecutionContext
import com.yonatankarp.exekutor.core.api.Step
import com.yonatankarp.exekutor.core.registry.StepRegistry

/**
 * A base class for [Step]s that automatically register themselves into the
 * global [StepRegistry].
 *
 * This is useful when you want to avoid manually invoking `registerSteps {}`
 * and instead rely on instantiation to handle registration.
 *
 * Example:
 * ```
 * class FraudCheck : AutoRegisteredStep<RiskContext>() {
 *     override val name = "FraudCheck"
 *     override suspend fun execute(context: RiskContext): StepResult = ...
 * }
 *
 * // Somewhere during initialization:
 * FraudCheck() // Automatically registers itself
 * ```
 *
 * ⚠️ Use this class only when instantiating steps early during setup.
 */
abstract class AutoRegisteredStep<C : ExecutionContext> : Step<C> {
    init {
        @Suppress("LeakingThis")
        StepRegistry.register(this)
    }
}
