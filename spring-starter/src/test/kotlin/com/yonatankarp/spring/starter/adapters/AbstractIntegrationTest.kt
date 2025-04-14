package com.yonatankarp.spring.starter.adapters

import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.junit.jupiter.Container

abstract class AbstractIntegrationTest {
    companion object {
        @Container
        private val postgreSQLContainer = PostgreSQLContainer<Nothing>("postgres:latest")

        @DynamicPropertySource
        @JvmStatic
        @Suppress("unused")
        fun registerDynamicProperties(registry: DynamicPropertyRegistry) {
            registry.add("spring.datasource.url", postgreSQLContainer::getJdbcUrl)
            registry.add("spring.datasource.username", postgreSQLContainer::getUsername)
            registry.add("spring.datasource.password", postgreSQLContainer::getPassword)
        }
    }
}
