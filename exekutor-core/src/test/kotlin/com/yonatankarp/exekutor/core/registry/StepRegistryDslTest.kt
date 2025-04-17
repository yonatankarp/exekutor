package com.yonatankarp.exekutor.core.registry

import com.yonatankarp.exekutor.utils.FailStep
import com.yonatankarp.exekutor.utils.FrictionStep
import com.yonatankarp.exekutor.utils.PassStep
import com.yonatankarp.exekutor.utils.TestContext
import com.yonatankarp.exekutor.utils.resetStepRegistry
import io.kotest.assertions.assertSoftly
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class StepRegistryDslTest {
    @BeforeEach
    fun setUp() {
        resetStepRegistry()
    }

    @Test
    fun `step function should register a step instance`() {
        // Given
        val dsl = StepRegistryDsl()

        // When
        dsl.step { PassStep("SingleStep") }

        // Then
        assertSoftly {
            StepRegistry.all() shouldHaveSize 1
            StepRegistry.all().first().name shouldBe "SingleStep"
        }
    }

    @Test
    fun `step function should register different types of steps`() {
        // Given
        val dsl = StepRegistryDsl()

        // When
        dsl.step { PassStep("PassStepTest") }
        dsl.step { FailStep("FailStepTest") }
        dsl.step { FrictionStep("FrictionStepTest") }

        // Then
        assertSoftly {
            StepRegistry.all() shouldHaveSize 3
            StepRegistry.all().map { it.name } shouldContainExactly
                listOf(
                    "PassStepTest",
                    "FailStepTest",
                    "FrictionStepTest",
                )
        }
    }

    @Test
    fun `registerSteps should apply the DSL block`() {
        // When
        registerSteps {
            step { PassStep("Pass1") }
            step { FailStep("Fail1") }
        }

        // Then
        val registeredSteps = StepRegistry.all()
        assertSoftly {
            registeredSteps shouldHaveSize 2
            registeredSteps.map { it.name } shouldContainExactly
                listOf(
                    "Pass1",
                    "Fail1",
                )
        }
    }

    @Test
    fun `multiple step registrations should accumulate in the registry`() {
        // When - First registration
        registerSteps {
            step { PassStep("PassStep") }
        }

        // Then - Check first registration
        StepRegistry.all() shouldHaveSize 1

        // When - Second registration
        registerSteps {
            step { FailStep("FailStep") }
            step { FrictionStep("FrictionStep") }
        }

        // Then - Check accumulated steps
        val registeredSteps = StepRegistry.all()
        assertSoftly {
            registeredSteps shouldHaveSize 3
            registeredSteps.map { it.name } shouldContainExactly
                listOf(
                    "PassStep",
                    "FailStep",
                    "FrictionStep",
                )
        }
    }

    @Test
    fun `factory throwing exception should propagate exception`() {
        // Given
        val dsl = StepRegistryDsl()
        val exceptionMessage = "Factory exception"

        // When/Then
        assertThrows<RuntimeException> {
            registerSteps {
                dsl.step<TestContext> { throw RuntimeException(exceptionMessage) }
            }
        }

        // Verify that no steps were registered
        StepRegistry.all().shouldBeEmpty()
    }

    @Test
    fun `should register step that has side effects in factory`() {
        // Given
        val dsl = StepRegistryDsl()
        var sideEffectOccurred = false

        // When
        dsl.step {
            sideEffectOccurred = true
            PassStep("SideEffectStep")
        }

        // Then
        assertSoftly {
            StepRegistry.all() shouldHaveSize 1
            StepRegistry.all().first().name shouldBe "SideEffectStep"
            assert(sideEffectOccurred) { "Side effect should have occurred" }
        }
    }

    @Test
    fun `should support chained registrations`() {
        // Given
        val dsl = StepRegistryDsl()

        // When
        dsl.apply {
            step { PassStep("Step1") }
            step { PassStep("Step2") }
            step { PassStep("Step3") }
        }

        // Then
        StepRegistry.all() shouldHaveSize 3
    }

    @Test
    fun `first test adding steps to the registry`() {
        // When
        StepRegistry.register(PassStep("StateTest1"))

        // Then
        StepRegistry.all().size shouldBe 1
    }
}
