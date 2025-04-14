plugins {
    id("jacoco")
    id("exekutor.code-metrics")
    id("exekutor.java-conventions")
    id("exekutor.publishing-conventions")
    alias(libs.plugins.spotless) apply true
    alias(libs.plugins.kotlin.jvm) apply false
}

allprojects {
    group = "com.yonatankarp"
    version = "0.0.1"

    repositories {
        mavenLocal()
        mavenCentral()
    }
}
