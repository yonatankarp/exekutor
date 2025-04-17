package com.yonatankarp.exekutor.core.registry

import com.yonatankarp.exekutor.utils.FailStep
import com.yonatankarp.exekutor.utils.PassStep
import com.yonatankarp.exekutor.utils.TestContext
import com.yonatankarp.exekutor.utils.resetStepRegistry
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.collections.shouldContainExactly
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class PlanBuilderTest {
    @BeforeEach
    fun setUp() {
        resetStepRegistry()
    }

    @Test
    fun `defaultPlanBuilder should return empty list when registry is empty`() {
        // Given
        val planBuilder = defaultPlanBuilder<TestContext>()
        val context = TestContext()

        // When
        val plan = planBuilder(context)

        // Then
        plan.shouldBeEmpty()
    }

    @Test
    fun `defaultPlanBuilder should return all registered steps`() {
        // Given
        val step1 = PassStep()
        val step2 = FailStep()
        registerSteps {
            step { step1 }
            step { step2 }
        }
        val planBuilder = defaultPlanBuilder<TestContext>()
        val context = TestContext()

        // When
        val plan = planBuilder(context)

        // Then
        plan shouldContainExactly listOf(step1, step2)
    }
}
