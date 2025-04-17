package com.yonatankarp.exekutor.core.engine

import com.yonatankarp.exekutor.api.ExecutionDecision
import com.yonatankarp.exekutor.api.Outcome
import com.yonatankarp.exekutor.api.Step
import com.yonatankarp.exekutor.utils.ExceptionStep
import com.yonatankarp.exekutor.utils.RecordingStep
import com.yonatankarp.exekutor.utils.TestContext
import com.yonatankarp.exekutor.utils.TimeoutStep
import io.kotest.assertions.assertSoftly
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeInstanceOf
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.assertThrows
import kotlin.test.Test

class StepExecutionEngineTest {
    private lateinit var testContext: TestContext

    @BeforeEach
    fun setUp() {
        testContext = TestContext()
    }

    @Test
    fun `run should execute all steps when all steps pass`() =
        runTest {
            // Given
            val step1 = RecordingStep("Step1", Outcome.PASS)
            val step2 = RecordingStep("Step2", Outcome.PASS)
            val step3 = RecordingStep("Step3", Outcome.PASS)
            val planBuilder: (TestContext) -> List<Step<TestContext>> = { listOf(step1, step2, step3) }
            val engine = StepExecutionEngine(planBuilder)

            // When
            val result = engine.run(testContext)

            // Then
            result shouldBe ExecutionDecision.Success
            assertSoftly {
                step1.executed shouldBe true
                step2.executed shouldBe true
                step3.executed shouldBe true
                testContext.results.size shouldBe 3
                testContext.results["Step1"]?.outcome shouldBe Outcome.PASS
                testContext.results["Step2"]?.outcome shouldBe Outcome.PASS
                testContext.results["Step3"]?.outcome shouldBe Outcome.PASS
            }
        }

    @Test
    fun `run should stop execution when a step fails`() =
        runTest {
            // Given
            val step1 = RecordingStep("Step1", Outcome.PASS)
            val step2 = RecordingStep("Step2", Outcome.FAIL, "Failure details")
            val step3 = RecordingStep("Step3", Outcome.PASS)
            val planBuilder: (TestContext) -> List<Step<TestContext>> = { listOf(step1, step2, step3) }
            val engine = StepExecutionEngine(planBuilder)

            // When
            val result = engine.run(testContext)

            // Then
            assertSoftly {
                result.shouldBeInstanceOf<ExecutionDecision.Fail>()
                result.reason shouldBe "Step Step2 failed"
                step1.executed shouldBe true
                step2.executed shouldBe true
                step3.executed shouldBe false
                testContext.results.size shouldBe 2
                testContext.results["Step1"]?.outcome shouldBe Outcome.PASS
                testContext.results["Step2"]?.outcome shouldBe Outcome.FAIL
                testContext.results["Step3"] shouldBe null
            }
        }

    @Test
    fun `run should stop execution when friction is required`() =
        runTest {
            // Given
            val frictionDetails = "Verification needed"
            val step1 = RecordingStep("Step1", Outcome.PASS)
            val step2 = RecordingStep("Step2", Outcome.FRICTION_REQUIRED, frictionDetails)
            val step3 = RecordingStep("Step3", Outcome.PASS)
            val planBuilder: (TestContext) -> List<Step<TestContext>> = { listOf(step1, step2, step3) }
            val engine = StepExecutionEngine(planBuilder)

            // When
            val result = engine.run(testContext)

            // Then
            assertSoftly {
                result.shouldBeInstanceOf<ExecutionDecision.Friction>()
                result.reason shouldBe "Step Step2 requires friction"
                result.data shouldBe frictionDetails
                step1.executed shouldBe true
                step2.executed shouldBe true
                step3.executed shouldBe false
                testContext.results.size shouldBe 2
                testContext.results["Step1"]?.outcome shouldBe Outcome.PASS
                testContext.results["Step2"]?.outcome shouldBe Outcome.FRICTION_REQUIRED
                testContext.results["Step3"] shouldBe null
            }
        }

    @Test
    fun `run should handle exceptions in steps`() =
        runTest {
            // Given
            val step1 = RecordingStep("Step1", Outcome.PASS)
            val step2 = ExceptionStep("ErrorStep")
            val step3 = RecordingStep("Step3", Outcome.PASS)
            val planBuilder: (TestContext) -> List<Step<TestContext>> = { listOf(step1, step2, step3) }
            val engine = StepExecutionEngine(planBuilder)

            // When
            val result = engine.run(testContext)

            // Then
            assertSoftly {
                result.shouldBeInstanceOf<ExecutionDecision.Fail>()
                result.reason shouldBe "Step ErrorStep failed"
                step1.executed shouldBe true
                step3.executed shouldBe false
                testContext.results.size shouldBe 2
                testContext.results["Step1"]?.outcome shouldBe Outcome.PASS
                testContext.results["ErrorStep"]?.outcome shouldBe Outcome.FAIL
                testContext.results["ErrorStep"]?.details.toString() shouldBe "Step ErrorStep threw exception: Test exception"
            }
        }

    @Test
    fun `run should handle timeout exceptions`() =
        runTest {
            // Given
            val step1 = RecordingStep("Step1", Outcome.PASS)
            val step2 = TimeoutStep("TimeoutStep")
            val step3 = RecordingStep("Step3", Outcome.PASS)
            val context = TestContext()

            val planBuilder: (TestContext) -> List<Step<TestContext>> = { listOf(step1, step2, step3) }
            val engine = StepExecutionEngine(planBuilder)

            // When
            val result = engine.run(context)

            // Then
            assertSoftly {
                result.shouldBeInstanceOf<ExecutionDecision.Fail>()
                result.reason shouldBe "Step TimeoutStep failed"
                step1.executed shouldBe true
                step3.executed shouldBe false
                context.results.size shouldBe 2
                context.results["Step1"]?.outcome shouldBe Outcome.PASS
                context.results["TimeoutStep"]?.outcome shouldBe Outcome.FAIL
                context.results["TimeoutStep"]?.details.toString() shouldBe "Step TimeoutStep timed out"
            }
        }

    @Test
    fun `run should throw an error for an empty plan`() =
        runTest {
            // Given
            val planBuilder: (TestContext) -> List<Step<TestContext>> = { emptyList() }
            val engine = StepExecutionEngine(planBuilder)

            // When / Then
            assertThrows<IllegalArgumentException> {
                engine.run(testContext)
            }
        }

    @Test
    fun `run should support a no time limit`() =
        runTest {
            // Given
            val step1 = RecordingStep("Step1", Outcome.PASS)
            val context = TestContext(timeBudgetMs = 1000, remainingTimeMs = null)
            val planBuilder: (TestContext) -> List<Step<TestContext>> = { listOf(step1) }

            val engine = StepExecutionEngine(planBuilder, timeBufferMs = 0)

            // When
            val result = engine.run(context)

            // Then
            assertSoftly {
                result shouldBe ExecutionDecision.Success
                step1.executed shouldBe true
                context.results.size shouldBe 1
            }
        }
}
