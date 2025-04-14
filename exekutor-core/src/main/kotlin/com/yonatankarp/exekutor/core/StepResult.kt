package com.yonatankarp.exekutor.core

data class StepResult(
    val outcome: Outcome,
    val details: Any? = null,
)

enum class Outcome {
    PASS,
    FAIL,
    FRICTION_REQUIRED,
}
