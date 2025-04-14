package com.yonatankarp.exekutor.core.steps

import com.yonatankarp.exekutor.core.ExecutionContext

fun <C : ExecutionContext> defaultPlanBuilder(): (C) -> List<Step<C>> =
    {
        StepRegistry.all().filterIsInstance<Step<C>>()
    }
