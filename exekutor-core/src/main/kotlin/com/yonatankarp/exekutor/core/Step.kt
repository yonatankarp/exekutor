package com.yonatankarp.exekutor.core

interface Step<C : ExecutionContext> {
    val name: String

    suspend fun execute(context: C): StepResult
}
