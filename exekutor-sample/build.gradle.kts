plugins {
    alias(libs.plugins.kotlin.jvm)
    application
}

dependencies {
    implementation(project(":exekutor-core"))
    implementation(libs.bundles.kotlin.all)
    testImplementation(libs.bundles.test.all)
}

application {
    mainClass.set("com.yonatankarp.exekutor.sample.MainKt")
}

kotlin {
    jvmToolchain {
        languageVersion.set(JavaLanguageVersion.of(libs.versions.jvmTarget.get()))
        target { JavaLanguageVersion.of(libs.versions.jvmTarget.get()) }
    }
}

tasks.test {
    useJUnitPlatform()
}
