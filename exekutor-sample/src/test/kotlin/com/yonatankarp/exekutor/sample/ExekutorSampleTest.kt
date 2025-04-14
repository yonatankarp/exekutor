package com.yonatankarp.exekutor.sample

import com.yonatankarp.exekutor.core.ExecutionDecision
import com.yonatankarp.exekutor.core.StepExecutionEngine
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class ExekutorSampleTest {
    @Test
    fun `should approve a clean user`() =
        runTest {
            // Given
            val engine = engine()
            val context = context("sample-user")

            // When
            val result = engine.run(context)

            // Then
            assertEquals(ExecutionDecision.Success, result)
        }

    @Test
    fun `should fail if fraud is detected`() =
        runTest {
            // Given
            val engine = engine()
            val context = context("fraud")

            // When
            val result = engine.run(context)

            // Then
            assertEquals(ExecutionDecision.Fail("Step FraudCheck failed"), result)
        }

    @Test
    fun `should require friction for low credit score`() =
        runTest {
            // Given
            val engine = engine()
            val context = context("abc")

            // When
            val result = engine.run(context)

            // Then
            assertEquals(
                ExecutionDecision.Friction(
                    reason = "Step CreditCheck requires friction",
                    data = "Low credit score",
                ),
                result,
            )
        }

    private fun engine(): StepExecutionEngine<RiskContext> = StepExecutionEngine(planBuilder = { listOf(FraudCheck(), CreditCheck()) })

    private fun context(payload: String): RiskContext = RiskContext(payload = payload, timeBudgetMs = 3000)
}
