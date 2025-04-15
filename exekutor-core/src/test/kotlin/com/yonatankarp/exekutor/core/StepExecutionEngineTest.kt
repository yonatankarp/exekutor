package com.yonatankarp.exekutor.core

import com.yonatankarp.exekutor.core.api.ExecutionContext
import com.yonatankarp.exekutor.core.api.ExecutionDecision
import com.yonatankarp.exekutor.core.api.Outcome
import com.yonatankarp.exekutor.core.api.Step
import com.yonatankarp.exekutor.core.api.StepResult
import com.yonatankarp.exekutor.core.engine.StepExecutionEngine
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class StepExecutionEngineTest {
    @Test
    fun `engine succeeds if all steps pass`() =
        runTest {
            // Given
            val planBuilder: (DummyContext) -> List<Step<DummyContext>> = {
                listOf(PassingStep(), PassingStep())
            }
            val engine = StepExecutionEngine(planBuilder)

            // When
            val result = engine.run(DummyContext(timeBudgetMs = 1000))

            // Then
            assertEquals(ExecutionDecision.Success, result)
        }

    @Test
    fun `engine stops on failure`() =
        runBlocking {
            // Given
            val planBuilder: (DummyContext) -> List<Step<DummyContext>> = {
                listOf(PassingStep(), FailingStep(), PassingStep())
            }
            val engine = StepExecutionEngine(planBuilder)

            // When
            val result = engine.run(DummyContext(timeBudgetMs = 1000))

            // Then
            assertEquals(ExecutionDecision.Fail("Step Fail failed"), result)
        }

    class DummyContext(
        override val timeBudgetMs: Long,
    ) : ExecutionContext {
        private val startTime = System.currentTimeMillis()

        override fun remainingTime(): Long = timeBudgetMs - (System.currentTimeMillis() - startTime)

        override val results = mutableMapOf<String, StepResult>()
    }

    class PassingStep : Step<DummyContext> {
        override val name = "Pass"

        override suspend fun execute(context: DummyContext): StepResult =
            StepResult(
                Outcome.PASS,
            )
    }

    class FailingStep : Step<DummyContext> {
        override val name = "Fail"

        override suspend fun execute(context: DummyContext): StepResult =
            StepResult(
                Outcome.FAIL,
            )
    }
}
