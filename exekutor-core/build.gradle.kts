plugins {
    id("exekutor.code-metrics")
    alias(libs.plugins.kotlin.jvm)
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
