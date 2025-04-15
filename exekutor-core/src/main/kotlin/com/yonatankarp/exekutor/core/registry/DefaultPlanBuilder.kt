package com.yonatankarp.exekutor.core.registry

import com.yonatankarp.exekutor.core.api.ExecutionContext
import com.yonatankarp.exekutor.core.api.Step

fun <C : ExecutionContext> defaultPlanBuilder(): (C) -> List<Step<C>> =
    {
        StepRegistry.all().filterIsInstance<Step<C>>()
    }
