package com.yonatankarp.exekutor.core.registry

import com.yonatankarp.exekutor.utils.FailStep
import com.yonatankarp.exekutor.utils.FrictionStep
import com.yonatankarp.exekutor.utils.PassStep
import com.yonatankarp.exekutor.utils.resetStepRegistry
import io.kotest.assertions.assertSoftly
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.collections.shouldContain
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Timeout
import java.util.concurrent.TimeUnit

class StepRegistryTest {
    @BeforeEach
    fun setUp() {
        resetStepRegistry()
    }

    @Test
    fun `register should add a step to the registry`() {
        // Given
        val step = PassStep()

        // When
        StepRegistry.register(step)

        // Then
        val allSteps = StepRegistry.all()
        assertSoftly {
            allSteps shouldHaveSize 1
            allSteps shouldContain step
        }
    }

    @Test
    fun `register should add different types of steps to the registry`() {
        // Given
        val passStep = PassStep()
        val failStep = FailStep()
        val frictionStep = FrictionStep()

        // When
        StepRegistry.register(passStep)
        StepRegistry.register(failStep)
        StepRegistry.register(frictionStep)

        // Then
        val allSteps = StepRegistry.all()
        assertSoftly {
            allSteps shouldHaveSize 3
            allSteps shouldContain passStep
            allSteps shouldContain failStep
            allSteps shouldContain frictionStep
        }
    }

    @Test
    fun `all should return a copy of the internal list`() {
        // Given
        val step1 = PassStep()
        val step2 = FailStep()
        StepRegistry.register(step1)
        StepRegistry.register(step2)

        // When
        val allSteps = StepRegistry.all()

        // Then
        assertSoftly {
            allSteps shouldHaveSize 2
            allSteps shouldContain step1
            allSteps shouldContain step2

            // Verify that modifying the returned list doesn't affect the registry
            val mutableList = allSteps.toMutableList()
            mutableList.remove(step1)
            mutableList shouldHaveSize 1
            StepRegistry.all() shouldHaveSize 2
        }
    }

    @Test
    fun `all should return empty list when no steps are registered`() {
        // When
        val allSteps = StepRegistry.all()

        // Then
        allSteps.shouldBeEmpty()
    }

    @Test
    fun `register should allow duplicate step registration`() {
        // Given
        val step = PassStep("DuplicateStep")

        // When
        StepRegistry.register(step)
        StepRegistry.register(step)

        // Then
        val allSteps = StepRegistry.all()
        assertSoftly {
            allSteps shouldHaveSize 2
            allSteps[0] shouldBe step
            allSteps[1] shouldBe step
        }
    }

    @Test
    fun `all should return steps in the order they were registered`() {
        // Given
        val pass = PassStep("PassStep")
        val fail = FailStep("FailStep")
        val friction = FrictionStep("FrictionStep")

        // When
        StepRegistry.register(friction)
        StepRegistry.register(pass)
        StepRegistry.register(fail)

        // Then
        val allSteps = StepRegistry.all()
        assertSoftly {
            allSteps shouldHaveSize 3
            allSteps shouldContainExactly listOf(friction, pass, fail)
        }
    }

    @Test
    fun `register should accept steps with same name but different instances`() {
        // Given
        val step1 = PassStep("SameName")
        val step2 = PassStep("SameName")

        // When
        StepRegistry.register(step1)
        StepRegistry.register(step2)

        // Then
        val allSteps = StepRegistry.all()
        assertSoftly {
            allSteps shouldHaveSize 2
            allSteps[0] shouldBe step1
            allSteps[1] shouldBe step2
        }
    }

    @Test
    fun `register should be thread-safe when accessed concurrently`() =
        runTest {
            // Given
            val numberOfSteps = 100

            // When
            val jobs =
                (1..numberOfSteps).map { index ->
                    async(Dispatchers.Default) {
                        val step = PassStep("ConcurrentStep$index")
                        StepRegistry.register(step)
                    }
                }

            jobs.awaitAll()

            // Then
            val allSteps = StepRegistry.all()
            allSteps shouldHaveSize numberOfSteps
        }

    @Test
    fun `all should be thread-safe when accessed concurrently with register`() =
        runTest {
            // Given
            val numberOfOperations = 50

            // When
            val jobs =
                (1..numberOfOperations).map { index ->
                    async(Dispatchers.Default) {
                        if (index % 2 == 0) {
                            // Even indices: register a new step
                            val step = PassStep("MixedStep$index")
                            StepRegistry.register(step)
                        } else {
                            // Odd indices: read the current registry
                            StepRegistry.all()
                        }
                    }
                }

            jobs.awaitAll()

            // Then
            val finalSteps = StepRegistry.all()
            finalSteps shouldHaveSize numberOfOperations / 2
        }

    @Test
    @Timeout(value = 5, unit = TimeUnit.SECONDS)
    fun `register should handle a large number of steps efficiently`() {
        // Given
        val largeNumber = 100_000

        // When
        repeat(largeNumber) { index ->
            val step = PassStep("Step$index")
            StepRegistry.register(step)
        }

        // Then
        StepRegistry.all() shouldHaveSize largeNumber
    }
}
