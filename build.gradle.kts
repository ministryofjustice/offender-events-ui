plugins {
  id("uk.gov.justice.hmpps.gradle-spring-boot") version "5.1.4-beta-3"
  kotlin("plugin.spring") version "1.8.20"
  kotlin("plugin.jpa") version "1.8.20"
}

configurations {
  implementation { exclude(module = "spring-boot-starter-security") }
}

repositories {
  mavenCentral()
}
dependencies {
  annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")

  implementation("org.springframework.boot:spring-boot-starter-data-redis")
  implementation("uk.gov.justice.service.hmpps:hmpps-sqs-spring-boot-starter:2.0.0-beta-15")

  implementation("com.google.code.gson:gson:2.10.1")

  implementation("org.springframework.boot:spring-boot-starter-thymeleaf")
  implementation("nz.net.ultraq.thymeleaf:thymeleaf-layout-dialect:3.2.1")
  implementation("org.apache.groovy:groovy:4.0.11")
  implementation("io.opentelemetry.instrumentation:opentelemetry-instrumentation-annotations:1.25.0")
  implementation("software.amazon.awssdk:sts:2.20.53")

  testImplementation("net.javacrumbs.json-unit:json-unit-assertj:2.37.0")
  testImplementation("org.testcontainers:localstack:1.18.0")
  testImplementation("com.amazonaws:aws-java-sdk-core:1.12.454")
  testImplementation("org.awaitility:awaitility-kotlin:4.2.0")
  testImplementation("it.ozimov:embedded-redis:0.7.3") { exclude("org.slf4j", "slf4j-simple") }
  testImplementation("commons-io:commons-io:2.11.0") // override vulnerable version
}

java {
  toolchain.languageVersion.set(JavaLanguageVersion.of(19))
}

tasks {
  withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    kotlinOptions {
      jvmTarget = "19"
    }
  }
}
