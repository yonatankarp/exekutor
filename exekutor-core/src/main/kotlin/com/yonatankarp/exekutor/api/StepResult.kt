package com.yonatankarp.exekutor.api

/**
 * The outcome of a single step execution.
 *
 * @param outcome The result type (pass, fail, friction).
 * @param details Optional metadata or reason for failure/friction.
 */
data class StepResult(
    val outcome: Outcome,
    val details: Any? = null,
)
