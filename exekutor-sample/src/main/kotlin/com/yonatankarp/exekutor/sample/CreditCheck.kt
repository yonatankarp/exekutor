package com.yonatankarp.exekutor.sample

import com.yonatankarp.exekutor.core.Outcome
import com.yonatankarp.exekutor.core.Step
import com.yonatankarp.exekutor.core.StepResult

class CreditCheck : Step<RiskContext> {
    override val name = "CreditCheck"

    override suspend fun execute(context: RiskContext): StepResult {
        println("→ Running $name...")
        val creditScore = context.payload.length // placeholder logic
        return if (creditScore < 10) {
            StepResult(
                Outcome.FRICTION_REQUIRED,
                details = "Low credit score",
            )
        } else {
            StepResult(Outcome.PASS)
        }
    }
}
