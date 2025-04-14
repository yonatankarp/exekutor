# Plugins

The template contains the following plugins:

- [jacoco](https://docs.gradle.org/current/userguide/jacoco_plugin.html)

Provides code coverage metrics for Java code via integration with JaCoCo.
You can create the report by running the following command:

  ```bash
    ./gradlew jacocoTestReport
  ```

The report will be created in build/reports subdirectory in your project
directory.

- [io.spring.dependency-management](https://github.com/spring-gradle-plugins/dependency-management-plugin)

Provides Maven-like dependency management. Allows you to declare dependency
management using `dependency 'groupId:artifactId:version'` or
`dependency group:'group', name:'name', version:version'`.

- [org.springframework.boot](http://projects.spring.io/spring-boot/)

Reduces the amount of work needed to create a Spring application

- [org.jetbrains.kotlin.jvm](https://kotlinlang.org/docs/gradle.html)

This plugin is a requirement in order to use Kotlin within your project.

- [org.jetbrains.kotlin.plugin.spring](https://kotlinlang.org/docs/all-open-plugin.html#spring-support)

This plugin is a requirement in order to successfully compile Kotlin with
Spring.
