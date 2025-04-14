package com.yonatankarp.exekutor.core

import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.withTimeout

class StepExecutionEngine<C : ExecutionContext>(
    private val planBuilder: (C) -> List<Step<C>>,
    private val timeBufferMs: Long = 200,
) {
    suspend fun run(context: C): ExecutionDecision {
        val plan = planBuilder(context)

        for (step in plan) {
            if (context.remainingTime() <= timeBufferMs) {
                return ExecutionDecision.Fail("Time budget exceeded before step ${step.name}")
            }

            try {
                val result =
                    withTimeout(context.remainingTime()) {
                        step.execute(context)
                    }

                context.results[step.name] = result

                return when (result.outcome) {
                    Outcome.FAIL -> ExecutionDecision.Fail("Step ${step.name} failed")
                    Outcome.FRICTION_REQUIRED ->
                        ExecutionDecision.Friction(
                            "Step ${step.name} requires friction",
                            result.details,
                        )

                    Outcome.PASS -> continue
                }
            } catch (e: TimeoutCancellationException) {
                return ExecutionDecision.Fail("Step ${step.name} timed out")
            } catch (e: Exception) {
                return ExecutionDecision.Fail("Step ${step.name} threw exception: ${e.message}")
            }
        }

        return ExecutionDecision.Success
    }
}
