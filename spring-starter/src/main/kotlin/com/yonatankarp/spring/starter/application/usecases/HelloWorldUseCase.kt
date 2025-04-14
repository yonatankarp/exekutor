package com.yonatankarp.spring.starter.application.usecases

import com.yonatankarp.spring.starter.application.ports.HelloWorldPort
import com.yonatankarp.spring.starter.domain.valueobject.HelloWorld
import org.springframework.stereotype.Service

@Service
class HelloWorldUseCase : HelloWorldPort {
    override suspend fun greet() = HelloWorld().sayHello()
}
