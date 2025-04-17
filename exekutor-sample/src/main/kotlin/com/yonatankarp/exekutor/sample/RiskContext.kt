package com.yonatankarp.exekutor.sample

import com.yonatankarp.exekutor.api.ExecutionContext
import com.yonatankarp.exekutor.api.StepResult

class RiskContext(
    val payload: String,
    override val timeBudgetMs: Long,
) : ExecutionContext {
    private val start = System.currentTimeMillis()

    override fun remainingTime(): Long = timeBudgetMs - (System.currentTimeMillis() - start)

    override val results = mutableMapOf<String, StepResult>()
}
