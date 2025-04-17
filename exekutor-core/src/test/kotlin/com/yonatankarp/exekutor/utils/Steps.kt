package com.yonatankarp.exekutor.utils

import com.yonatankarp.exekutor.api.AutoRegisteredStep
import com.yonatankarp.exekutor.api.ExecutionContext
import com.yonatankarp.exekutor.api.Outcome
import com.yonatankarp.exekutor.api.Step
import com.yonatankarp.exekutor.api.StepResult
import kotlinx.coroutines.delay

class PassStep(
    override val name: String = "PassStep",
) : Step<TestContext> {
    override suspend fun execute(context: TestContext): StepResult = StepResult(Outcome.PASS, "Success details")
}

class FailStep(
    override val name: String = "FailStep",
) : Step<TestContext> {
    override suspend fun execute(context: TestContext): StepResult = StepResult(Outcome.FAIL, "Failure details")
}

class FrictionStep(
    override val name: String = "FrictionStep",
) : Step<TestContext> {
    override suspend fun execute(context: TestContext): StepResult = StepResult(Outcome.FRICTION_REQUIRED, "Friction details")
}

class PassAutoRegisteredStep(
    override val name: String = "SimpleAutoRegisteredStep",
) : AutoRegisteredStep<TestContext>() {
    override suspend fun execute(context: TestContext): StepResult = StepResult(Outcome.PASS)
}

class FailAutoRegisteredStep(
    override val name: String = "FailAutoRegisteredStep",
) : AutoRegisteredStep<TestContext>() {
    override suspend fun execute(context: TestContext): StepResult = StepResult(Outcome.FAIL)
}

class RecordingStep(
    override val name: String,
    private val outcomeToReturn: Outcome,
    private val details: Any? = null,
) : Step<TestContext> {
    var executed: Boolean = false
        private set

    override suspend fun execute(context: TestContext): StepResult {
        executed = true
        return StepResult(outcomeToReturn, details)
    }
}

class ExceptionStep(
    override val name: String,
) : Step<TestContext> {
    override suspend fun execute(context: TestContext): StepResult = throw RuntimeException("Test exception")
}

class TimeoutStep(
    override val name: String,
    private val delayMs: Long = 1000,
) : Step<TestContext> {
    override suspend fun execute(context: TestContext): StepResult {
        delay(delayMs)
        return StepResult(Outcome.PASS)
    }
}

fun resetStepRegistry() {
    val stepsField =
        Class
            .forName("com.yonatankarp.exekutor.core.registry.StepRegistry")
            .getDeclaredField("steps")
    stepsField.isAccessible = true
    val stepsInstance = stepsField.get(null) as MutableList<*>
    stepsInstance.clear()
}

class TestContext(
    override val timeBudgetMs: Long = 1000,
    private val remainingTimeMs: Long? = 500,
) : ExecutionContext {
    override val results: MutableMap<String, StepResult> = mutableMapOf()

    override fun remainingTime(): Long? = remainingTimeMs
}
