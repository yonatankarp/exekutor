package com.yonatankarp.exekutor.sample

import com.yonatankarp.exekutor.core.api.Outcome
import com.yonatankarp.exekutor.core.api.StepResult
import com.yonatankarp.exekutor.core.base.AutoRegisteredStep
import io.github.oshai.kotlinlogging.KotlinLogging

class CreditCheck : AutoRegisteredStep<RiskContext>() {
    override val name = "CreditCheck"

    override suspend fun execute(context: RiskContext): StepResult {
        logger.info { "💳 Running CreditCheck" }
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

    companion object {
        private val logger = KotlinLogging.logger {}
    }
}
