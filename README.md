# spring-starter

[build-badge]: https://github.com/yonatankarp/spring-starter/actions/workflows/build.yml/badge.svg
[build-state]: https://github.com/yonatankarp/spring-starter/actions/workflows/build.yml
[codeql-badge]: https://github.com/yonatankarp/spring-starter/actions/workflows/codeql.yml/badge.svg
[codeql-state]: https://github.com/yonatankarp/spring-starter/actions/workflows/codeql.yml

[quality-badge]: https://sonarcloud.io/api/project_badges/measure?project=yonatankarp_spring-starter&metric=alert_status
[quality-state]: https://sonarcloud.io/summary/new_code?id=yonatankarp_spring-starter
[maintainability-badge]: https://sonarcloud.io/api/project_badges/measure?project=yonatankarp_spring-starter&metric=sqale_rating
[maintainability-state]: https://sonarcloud.io/summary/new_code?id=yonatankarp_spring-starter
[tech-debt-badge]: https://sonarcloud.io/api/project_badges/measure?project=yonatankarp_spring-starter&metric=sqale_index
[tech-debt-state]: https://sonarcloud.io/summary/new_code?id=yonatankarp_spring-starter
[security-badge]: https://sonarcloud.io/api/project_badges/measure?project=yonatankarp_spring-starter&metric=security_rating
[security-state]: https://sonarcloud.io/summary/new_code?id=yonatankarp_spring-starter
[vulnerabilities-badge]: https://sonarcloud.io/api/project_badges/measure?project=yonatankarp_spring-starter&metric=vulnerabilities
[vulnerabilities-state]: https://sonarcloud.io/summary/new_code?id=yonatankarp_spring-starter
[license-badge]: https://img.shields.io/badge/License-MIT-yellow.svg
[license-link]: https://opensource.org/licenses/MIT

| **Type**     | **Status**                                                                                                                                                                             |
|--------------|----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| CI pipelines | [![Build][build-badge]][build-state]  [![CodeQL][codeql-badge]][codeql-state]                                                                                                          |
| Maintenance  | [![Quality Gate Status][quality-badge]][quality-state] [![Maintainability Rating][maintainability-badge]][maintainability-state] [![Technical Debt][tech-debt-badge]][tech-debt-state] |
| Security     | [![Security Rating][security-badge]][security-state] [![Vulnerabilities][vulnerabilities-badge]][vulnerabilities-state]                                                                |
| License      | [![License: MIT][license-badge]][license-link]                                                                                                                                         |



## Purpose

This is a template to fast and easily bootstrap new spring boot web
applications that including full build based on Gradle, CI based on GitHub
actions, and containers based on Docker

For the C4 diagram of the system visit [docs/c4/README.md](./docs/c4/README.md).

## What's inside

The template is a working application with a minimal setup. It contains:

- application skeleton
- setup script to prepare project
- common plugins and libraries
- docker & docker-compose setup
- code quality tools already set up
- integration with GitHub Actions
- integration with Dependabot
- integration with SonarCloudA
- Integration with PostgreSQL, Flyway & Jooq
- Integration with test containers
- integration with [CodeQL](https://github.com/yonatankarp/spring-starter/security/code-scanning)
- health check, readiness & liveness probs for k8s integration
- OpenApi spec code generation & documentation
- C4 model architecture diagrams
- MIT license and contribution information

## Setup

Located in `./bin/init.sh`. Simply run and follow the explanation how to
execute it. This script will self-destroy itself when finished.

## Getting Started

These instructions will get you a copy of the project up and running on your
local machine for development and testing purposes. See deployment for notes on
how to deploy the project on a live system.

### Prerequisites

To run the project you need to install the following:

- JDK 17 or newer
- Docker


### Building the application

The project uses [Gradle](https://gradle.org) as a build tool. It already contains
`./gradlew` wrapper script, so there's no need to install gradle.

To build the project execute the following command:

```shell
  ./gradlew build
```

### Running the application

Create the image of the application by executing the following command:

```shell
  ./gradlew assemble
```

You can run this project directly from Gradle by executing the following
command:

```shell
./gradlew bootRun
```

Otherwise, you can create docker image:

```shell
  docker compose build
```

For Apple M1 processor run the following instead:

```shell
DOCKER_BUILDKIT=0 docker compose build
```

Run the distribution (created in `spring-starter/build/install/spring-starter`
directory) by executing the following command:

```shell
  docker compose up
```

This will start the API container exposing the application's port
(set to `8080` in this app).

In order to test if the application is up, you can call its health endpoint:

```shell
  curl http://localhost:8080/health
```

You should get a response similar to this:

```json
  {"status":"UP","diskSpace":{"status":"UP","total":249644974080,"free":137188298752,"threshold":10485760}}
```

### API Documentation

Once the service is up, you can access the API documentation from your browser
at the following URL: `http://localhost:8080/api-docs`.

You can use this page as a playground to test the API, and the interactions with
the service.

### Alternative script to run application

To skip all the setting up and building, just execute the following command:

```shell
./bin/run-in-docker.sh
```

For more information:

```shell
./bin/run-in-docker.sh --help
```

## Running the tests

You can run the project tests via Gradle by executing the following command:

```shell
./gradlew test
```

### And coding style tests

This project uses [Spotless Gradle plugin](https://github.com/diffplug/spotless)
to enforce its code style. The plugin will run automatically after every
successful build, test, and assemble stage. However, if you would like to run
it manually you can do so by running the following commands:

To apply the code style to the project run:

```shell
./gradlew spotlessApply
```

To check your code without applying any changes you can execute:

```shell
./gradlew spotlessCheck
```

## Plugins

To read more about the plugins included in this project click
[here](docs/plugins.md).

## Built With

- [OpenJdk 17](https://openjdk.java.net/projects/jdk/17/)
- [Kotlin](https://kotlinlang.org/)
- [SpringBoot](https://spring.io/projects/spring-boot) - The web framework used
- [Gradle](https://gradle.org/) - Dependency Management
- [GitHub Actions](https://docs.github.com/en/actions) - Continuous Integration
- [Docker](https://www.docker.com/) - Container handling

## Versioning

We use [SemVer](http://semver.org/) for versioning. For the versions available,
see the [tags on this repository](https://github.com/your/project/tags).

## Authors

- **Yonatan Karp-Rudin** - *Initial work* - [yonatankarp](https://github.com/yonatankarp)
