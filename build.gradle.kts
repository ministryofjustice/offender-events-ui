plugins {
  id("uk.gov.justice.hmpps.gradle-spring-boot") version "3.3.9"
  kotlin("plugin.spring") version "1.5.31"
  kotlin("plugin.jpa") version "1.5.31"
}

configurations {
  implementation { exclude(module = "spring-boot-starter-security") }
}

dependencies {
  annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")

  implementation("org.springframework.boot:spring-boot-starter-data-redis")
  implementation("uk.gov.justice.service.hmpps:hmpps-sqs-spring-boot-starter:1.0.2")

  implementation("com.google.code.gson:gson:2.8.8")

  implementation("org.springframework.boot:spring-boot-starter-thymeleaf")
  implementation("nz.net.ultraq.thymeleaf:thymeleaf-layout-dialect:3.0.0")

  testImplementation("net.javacrumbs.json-unit:json-unit-assertj:2.28.0")
  testImplementation("org.testcontainers:localstack:1.16.0")
  testImplementation("org.awaitility:awaitility-kotlin:4.1.0")
  testImplementation("it.ozimov:embedded-redis:0.7.3") { exclude("org.slf4j", "slf4j-simple") }
}

java {
  toolchain.languageVersion.set(JavaLanguageVersion.of(16))
}

tasks {
  withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    kotlinOptions {
      jvmTarget = "16"
    }
  }
}
