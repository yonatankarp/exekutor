package com.yonatankarp.spring.starter

import com.yonatankarp.spring.starter.adapters.AbstractIntegrationTest
import org.jooq.DSLContext
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.TestConstructor
import org.springframework.test.context.TestConstructor.AutowireMode.ALL
import org.testcontainers.junit.jupiter.Testcontainers

@SpringBootTest
@Testcontainers
@TestConstructor(autowireMode = ALL)
class ApplicationTest(
    private val jooq: DSLContext,
) : AbstractIntegrationTest() {
    @Test
    fun `smoke test`() {
        assertNotNull(jooq)
    }

    @Test
    fun `context loads`() {
    }
}
