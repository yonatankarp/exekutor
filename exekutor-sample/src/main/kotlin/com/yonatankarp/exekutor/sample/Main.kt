package com.yonatankarp.exekutor.sample

import com.yonatankarp.exekutor.core.api.ExecutionDecision
import com.yonatankarp.exekutor.core.engine.StepExecutionEngine
import com.yonatankarp.exekutor.core.registry.defaultPlanBuilder
import com.yonatankarp.exekutor.core.registry.registerSteps
import io.github.oshai.kotlinlogging.KotlinLogging
import kotlinx.coroutines.runBlocking

private val logger = KotlinLogging.logger {}

fun main() =
    runBlocking {
        registerSteps {
            step { FraudCheck() }
            step { CreditCheck() }
        }

        val context =
            RiskContext(
                payload = "simple-user", // Try "fraud" or "abc" to test behavior
                timeBudgetMs = 3000,
            )

        val engine =
            StepExecutionEngine<RiskContext>(
                planBuilder = defaultPlanBuilder(),
            )

        when (val result = engine.run(context)) {
            is ExecutionDecision.Success -> logger.info { "✅ All steps passed" }
            is ExecutionDecision.Fail -> logger.error { "❌ Step failed: ${result.reason}" }
            is ExecutionDecision.Friction -> logger.warn { "⚠️ Step requires friction: ${result.reason}" }
        }
    }
