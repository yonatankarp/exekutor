package com.yonatankarp.exekutor.api

/**
 * Represents possible outcomes of a [Step] execution.
 */
enum class Outcome {
    /**
     * The step completed successfully.
     */
    PASS,

    /**
     * The step failed and execution should stop.
     */
    FAIL,

    /**
     * The step requires user friction (e.g., OTP, re-verification).
     */
    FRICTION_REQUIRED,
}
