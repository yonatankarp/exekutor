package com.yonatankarp.exekutor.core.api

interface Step<C : ExecutionContext> {
    val name: String

    suspend fun execute(context: C): StepResult
}
