package com.yonatankarp.spring.starter.application.ports

fun interface HelloWorldPort {
    suspend fun greet(): String
}
