package com.yonatankarp.exekutor.core.registry

import com.yonatankarp.exekutor.core.api.ExecutionContext
import com.yonatankarp.exekutor.core.api.Step

class StepRegistryDsl {
    fun <C : ExecutionContext> step(factory: () -> Step<C>) {
        StepRegistry.register(factory())
    }
}
