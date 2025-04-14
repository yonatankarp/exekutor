package com.yonatankarp.exekutor.core

import com.yonatankarp.exekutor.core.steps.Step
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.withTimeout

class StepExecutionEngine<C : ExecutionContext>(
    private val planBuilder: (C) -> List<Step<C>>,
    private val timeBufferMs: Long = 200,
) {
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
                ExecutionDecision.Friction("Step ${step.name} requires friction", result.details)
        }
}
