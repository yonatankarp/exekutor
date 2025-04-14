package com.yonatankarp.exekutor.core.steps

import com.yonatankarp.exekutor.core.ExecutionContext
import com.yonatankarp.exekutor.core.StepResult

interface Step<C : ExecutionContext> {
    val name: String

    suspend fun execute(context: C): StepResult
}
