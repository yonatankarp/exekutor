package com.yonatankarp.exekutor.sample

import com.yonatankarp.exekutor.core.ExecutionContext
import com.yonatankarp.exekutor.core.StepResult

class RiskContext(
    val payload: String,
    override val timeBudgetMs: Long,
) : ExecutionContext {
    private val start = System.currentTimeMillis()

    override fun remainingTime(): Long = timeBudgetMs - (System.currentTimeMillis() - start)

    override val results = mutableMapOf<String, StepResult>()
}
