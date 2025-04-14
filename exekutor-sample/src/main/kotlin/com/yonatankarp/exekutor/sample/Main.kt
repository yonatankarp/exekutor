package com.yonatankarp.exekutor.sample

import com.yonatankarp.exekutor.core.StepExecutionEngine

suspend fun main() {
    val context =
        RiskContext(
            payload = "sample-user", // Try "fraud" or "abc" to test behavior
            timeBudgetMs = 3000,
        )

    val engine =
        StepExecutionEngine<RiskContext>(
            planBuilder = { listOf(FraudCheck(), CreditCheck()) },
        )

    val result = engine.run(context)

    println("✅ Final decision: $result")
}
