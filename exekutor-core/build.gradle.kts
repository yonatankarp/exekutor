import org.jetbrains.dokka.gradle.DokkaTask

plugins {
    java
    `maven-publish`
    id("exekutor.publishing-conventions")
    id("exekutor.code-metrics")
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.dokka)
}

kotlin {
    jvmToolchain {
        languageVersion.set(JavaLanguageVersion.of(libs.versions.jvmTarget.get()))
        target { JavaLanguageVersion.of(libs.versions.jvmTarget.get()) }
    }
}

dependencies {
    implementation(libs.bundles.kotlin.all)

    testRuntimeOnly(libs.slf4j.sample)
    testImplementation(kotlin("test"))
    testImplementation(libs.bundles.test.all) {
        exclude(group = "org.junit.vintage", module = "junit-vintage-engine")
    }
}

tasks {
    build {
        finalizedBy(spotlessApply)
    }

    test {
        useJUnitPlatform()
    }
}

val tasksDependencies = mapOf(
    "spotlessKotlin" to listOf("compileKotlin", "compileTestKotlin", "test")
)

tasksDependencies.forEach { (taskName, dependencies) ->
    tasks.findByName(taskName)?.dependsOn(dependencies)
}

tasks.withType<DokkaTask> {
    outputDirectory = layout.buildDirectory.get().asFile.resolve("dokka")
    dokkaSourceSets.configureEach {
        reportUndocumented = true
        skipDeprecated = true
    }
}
