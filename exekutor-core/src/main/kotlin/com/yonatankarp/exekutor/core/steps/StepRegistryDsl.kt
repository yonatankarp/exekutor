package com.yonatankarp.exekutor.core.steps

import com.yonatankarp.exekutor.core.ExecutionContext

class StepRegistryDsl {
    fun <C : ExecutionContext> step(factory: () -> Step<C>) {
        StepRegistry.register(factory())
    }
}
