package com.yonatankarp.exekutor.core.api

/**
 * Represents a single step in an execution plan.
 *
 * @param C The type of [ExecutionContext] used to share state and time tracking.
 */
interface Step<C : ExecutionContext> {
    /**
     * The unique name of the step.
     * Used for debugging, logging, and result tracking.
     */
    val name: String

    /**
     * Executes the step logic using the given [context].
     *
     * @param context The current execution context.
     * @return The result of the step execution.
     */
    suspend fun execute(context: C): StepResult
}
