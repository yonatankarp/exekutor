package com.yonatankarp.exekutor.core.registry

import com.yonatankarp.exekutor.api.ExecutionContext
import com.yonatankarp.exekutor.api.Step

/**
 * A DSL for registering [Step]s in a declarative way.
 *
 * Example usage:
 * ```
 * registerSteps {
 *     step { FraudCheck() }
 *     step { CreditCheck() }
 * }
 * ```
 */
class StepRegistryDsl {
    /**
     * Registers a step by providing a factory lambda.
     *
     * @param factory A lambda that produces a [Step] instance.
     */
    fun <C : ExecutionContext> step(factory: () -> Step<C>) {
        StepRegistry.register(factory())
    }
}
