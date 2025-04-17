package com.yonatankarp.exekutor.api

import com.yonatankarp.exekutor.core.registry.StepRegistry
import com.yonatankarp.exekutor.utils.FailAutoRegisteredStep
import com.yonatankarp.exekutor.utils.PassAutoRegisteredStep
import com.yonatankarp.exekutor.utils.resetStepRegistry
import io.kotest.assertions.assertSoftly
import io.kotest.matchers.collections.shouldContain
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.collections.shouldHaveSize
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class AutoRegisteredStepTest {
    @BeforeEach
    fun setUp() {
        resetStepRegistry()
    }

    @Test
    fun `should register automatically with StepRegistry during initialization`() {
        // When
        val step = PassAutoRegisteredStep()

        // Then
        val registeredSteps = StepRegistry.all()
        assertSoftly {
            registeredSteps shouldHaveSize 1
            registeredSteps shouldContain step
        }
    }

    @Test
    fun `should register each instance of the same class`() {
        // When
        val step1 = PassAutoRegisteredStep("Step1")
        val step2 = PassAutoRegisteredStep("Step2")
        val step3 = PassAutoRegisteredStep("Step3")

        // Then
        val registeredSteps = StepRegistry.all()
        assertSoftly {
            registeredSteps shouldHaveSize 3
            registeredSteps shouldContainExactly listOf(step1, step2, step3)
        }
    }

    @Test
    fun `should register instances of different subclasses`() {
        // When
        val step1 = PassAutoRegisteredStep()
        val step2 = FailAutoRegisteredStep()

        // Then
        val registeredSteps = StepRegistry.all()
        assertSoftly {
            registeredSteps shouldHaveSize 2
            registeredSteps shouldContainExactly listOf(step1, step2)
        }
    }
}
