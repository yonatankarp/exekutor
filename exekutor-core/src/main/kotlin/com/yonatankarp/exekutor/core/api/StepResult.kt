package com.yonatankarp.exekutor.core.api

data class StepResult(
    val outcome: Outcome,
    val details: Any? = null,
)
