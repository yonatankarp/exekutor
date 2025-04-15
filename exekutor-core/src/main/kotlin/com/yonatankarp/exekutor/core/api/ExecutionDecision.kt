package com.yonatankarp.exekutor.core.api

/**
 * Represents the outcome of executing a step plan.
 *
 * Returned by [StepExecutionEngine.run] to indicate whether the pipeline:
 * - completed successfully
 * - failed due to a blocking condition
 * - encountered a case requiring user friction
 */
sealed class ExecutionDecision {
    /**
     * Indicates that all steps executed successfully.
     */
    data object Success : ExecutionDecision()

    /**
     * Indicates that execution failed due to an unrecoverable condition.
     *
     * @param reason Human-readable description of the failure.
     */
    data class Fail(
        val reason: String,
    ) : ExecutionDecision()

    /**
     * Indicates that execution requires additional user friction or verification.
     *
     * @param reason Explanation of why friction is needed.
     * @param data Optional structured metadata (e.g., challenge type, redirect link).
     */
    data class Friction(
        val reason: String,
        val data: Any? = null,
    ) : ExecutionDecision()
}
