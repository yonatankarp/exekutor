package com.yonatankarp.exekutor.core.api

interface ExecutionContext {
    val timeBudgetMs: Long

    fun remainingTime(): Long

    val results: MutableMap<String, StepResult>
}
