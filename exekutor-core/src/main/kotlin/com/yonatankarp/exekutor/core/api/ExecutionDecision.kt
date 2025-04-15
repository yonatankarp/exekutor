package com.yonatankarp.exekutor.core.api

sealed class ExecutionDecision {
    data object Success : ExecutionDecision()

    data class Fail(
        val reason: String,
    ) : ExecutionDecision()

    data class Friction(
        val reason: String,
        val data: Any? = null,
    ) : ExecutionDecision()
}
