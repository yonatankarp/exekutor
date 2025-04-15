package com.yonatankarp.exekutor.core.base

import com.yonatankarp.exekutor.core.api.ExecutionContext
import com.yonatankarp.exekutor.core.api.Step
import com.yonatankarp.exekutor.core.registry.StepRegistry

abstract class AutoRegisteredStep<C : ExecutionContext> : Step<C> {
    init {
        @Suppress("LeakingThis")
        StepRegistry.register(this)
    }
}
