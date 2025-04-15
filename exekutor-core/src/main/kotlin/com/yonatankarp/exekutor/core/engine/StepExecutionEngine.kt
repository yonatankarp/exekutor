package com.yonatankarp.exekutor.core.engine

import com.yonatankarp.exekutor.core.api.ExecutionContext
import com.yonatankarp.exekutor.core.api.ExecutionDecision
import com.yonatankarp.exekutor.core.api.Outcome
import com.yonatankarp.exekutor.core.api.Step
import com.yonatankarp.exekutor.core.api.StepResult
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.withTimeout

/**
 * Orchestrates the execution of a list of [Step]s based on a given plan.
 *
 * Each step is executed in order.
 * Execution stops on:
 * - failure (`Outcome.FAIL`)
 * - friction required (`Outcome.FRICTION_REQUIRED`)
 * - time budget exceeded
 *
 * @param C The context type shared across all steps.
 * @param planBuilder A lambda that builds the list of steps to execute, based on the context.
 * @param timeBufferMs Minimum buffer (ms) required before starting the next step.
 */
class StepExecutionEngine<C : ExecutionContext>(
    private val planBuilder: (C) -> List<Step<C>>,
    private val timeBufferMs: Long = 200,
) {
    /**
     * Runs the execution plan for the given [context].
     *
     * @return A final [ExecutionDecision] representing the outcome.
     */
    suspend fun run(context: C): ExecutionDecision {
        val plan = planBuilder(context)

        for (step in plan) {
            if (shouldFailDueToTime(context)) {
                return ExecutionDecision.Fail("Time budget exceeded before step ${step.name}")
            }

            val result = runStepSafely(step, context)
            context.results[step.name] = result

            val decision = resolveDecision(step, result)
            if (decision != null) return decision
        }

        return ExecutionDecision.Success
    }

    private fun shouldFailDueToTime(context: C): Boolean = context.remainingTime() <= timeBufferMs

    private suspend fun runStepSafely(
        step: Step<C>,
        context: C,
    ): StepResult =
        try {
            withTimeout(context.remainingTime()) {
                step.execute(context)
            }
        } catch (e: TimeoutCancellationException) {
            StepResult(Outcome.FAIL, "Step ${step.name} timed out")
        } catch (e: Exception) {
            StepResult(Outcome.FAIL, "Step ${step.name} threw exception: ${e.message}")
        }

    private fun resolveDecision(
        step: Step<C>,
        result: StepResult,
    ): ExecutionDecision? =
        when (result.outcome) {
            Outcome.PASS -> null
            Outcome.FAIL -> ExecutionDecision.Fail("Step ${step.name} failed")
            Outcome.FRICTION_REQUIRED ->
                ExecutionDecision.Friction(
                    "Step ${step.name} requires friction",
                    result.details,
                )
        }
}
