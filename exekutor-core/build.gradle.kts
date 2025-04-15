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

tasks.dokkaHtml {
    outputDirectory.set(layout.buildDirectory.get().asFile.resolve("dokka"))
    dokkaSourceSets.configureEach {
        reportUndocumented.set(true)
        skipDeprecated.set(true)
    }
}
