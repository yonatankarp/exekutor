package com.yonatankarp.exekutor.core.registry

import com.yonatankarp.exekutor.api.ExecutionContext
import com.yonatankarp.exekutor.api.Step

/**
 * Returns a default plan builder function that pulls all registered steps
 * from [StepRegistry] and casts them to the correct type.
 *
 * This is a convenience for projects that want to define steps declaratively
 * without manually building execution plans.
 *
 * @return A plan builder: (context) -> list of steps
 */
fun <C : ExecutionContext> defaultPlanBuilder(): (C) -> List<Step<C>> =
    {
        @Suppress("UNCHECKED_CAST")
        StepRegistry.all() as List<Step<C>>
    }
