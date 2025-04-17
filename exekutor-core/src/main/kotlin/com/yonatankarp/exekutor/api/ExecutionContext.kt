package com.yonatankarp.exekutor.api

/**
 * Defines the context shared across all steps during execution.
 *
 * Provides time budget tracking and a way to store intermediate [StepResult]s.
 */
interface ExecutionContext {
    /**
     * The total time budget allowed for this execution, in milliseconds.
     */
    val timeBudgetMs: Long

    /**
     * Returns the remaining time available before the execution should time out.
     *
     * Implementations should calculate this relative to the initial time budget.
     * If no time budget is remaining, this method should return `null`.
     */
    fun remainingTime(): Long?

    /**
     * A mutable map of step names to their results.
     *
     * Used by steps to store and optionally inspect previous results.
     */
    val results: MutableMap<String, StepResult>
}
