package com.yonatankarp.exekutor.sample

import com.yonatankarp.exekutor.core.ExecutionDecision
import com.yonatankarp.exekutor.core.StepExecutionEngine
import io.github.oshai.kotlinlogging.KotlinLogging
import kotlinx.coroutines.runBlocking

private val logger = KotlinLogging.logger {}

fun main() = runBlocking {
    val context =
        RiskContext(
            payload = "sample-user", // Try "fraud" or "abc" to test behavior
            timeBudgetMs = 3000,
        )

    val engine =
        StepExecutionEngine<RiskContext>(
            planBuilder = { listOf(FraudCheck(), CreditCheck()) },
        )

    when (val result = engine.run(context)) {
        is ExecutionDecision.Success -> logger.info { "✅ All steps passed" }
        is ExecutionDecision.Fail -> logger.error { "❌ Step failed: ${result.reason}" }
        is ExecutionDecision.Friction -> logger.warn { "⚠️ Step requires friction: ${result.reason}" }
    }
}
