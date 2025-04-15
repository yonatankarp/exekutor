package com.yonatankarp.exekutor.sample

import com.yonatankarp.exekutor.core.api.Outcome
import com.yonatankarp.exekutor.core.api.StepResult
import com.yonatankarp.exekutor.core.base.AutoRegisteredStep
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
