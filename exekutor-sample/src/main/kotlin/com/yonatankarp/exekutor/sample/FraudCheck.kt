package com.yonatankarp.exekutor.sample

import com.yonatankarp.exekutor.core.Outcome
import com.yonatankarp.exekutor.core.Step
import com.yonatankarp.exekutor.core.StepResult

class FraudCheck : Step<RiskContext> {
    override val name = "FraudCheck"

    override suspend fun execute(context: RiskContext): StepResult {
        println("→ Running $name...")
        return if (context.payload.contains("fraud")) {
            StepResult(Outcome.FAIL)
        } else {
            StepResult(Outcome.PASS)
        }
    }
}
