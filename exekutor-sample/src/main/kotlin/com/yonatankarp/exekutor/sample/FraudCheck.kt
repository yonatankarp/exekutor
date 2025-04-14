package com.yonatankarp.exekutor.sample

import com.yonatankarp.exekutor.core.Outcome
import com.yonatankarp.exekutor.core.StepResult
import com.yonatankarp.exekutor.core.steps.AutoRegisteredStep
import io.github.oshai.kotlinlogging.KotlinLogging

class FraudCheck : AutoRegisteredStep<RiskContext>() {
    override val name = "FraudCheck"

    override suspend fun execute(context: RiskContext): StepResult {
        logger.info { "🕵️‍♂️ Running FraudCheck" }
        return if (context.payload.contains("fraud")) {
            StepResult(Outcome.FAIL)
        } else {
            StepResult(Outcome.PASS)
        }
    }

    companion object {
        private val logger = KotlinLogging.logger {}
    }
}
