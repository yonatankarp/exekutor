package com.yonatankarp.exekutor.core.steps

import com.yonatankarp.exekutor.core.ExecutionContext

abstract class AutoRegisteredStep<C : ExecutionContext> : Step<C> {
    init {
        @Suppress("LeakingThis")
        StepRegistry.register(this)
    }
}
