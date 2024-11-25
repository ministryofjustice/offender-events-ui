plugins {
  id("uk.gov.justice.hmpps.gradle-spring-boot") version "6.1.0"
  kotlin("plugin.spring") version "2.0.21"
  kotlin("plugin.jpa") version "2.0.21"
}

configurations {
  implementation {
    exclude(module = "spring-boot-starter-security")
    exclude(module = "spring-boot-starter-oauth2-client")
    exclude(module = "spring-boot-starter-oauth2-resource-server")
  }
}

dependencies {
  annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")

  implementation("uk.gov.justice.service.hmpps:hmpps-kotlin-spring-boot-starter:1.1.0")
  implementation("org.springframework.boot:spring-boot-starter-data-redis")
  implementation("uk.gov.justice.service.hmpps:hmpps-sqs-spring-boot-starter:5.2.0")

  implementation("com.google.code.gson:gson:2.11.0")
  implementation("com.google.guava:guava:33.3.1-jre")

  implementation("org.springframework.boot:spring-boot-starter-thymeleaf")
  implementation("nz.net.ultraq.thymeleaf:thymeleaf-layout-dialect:3.3.0")
  implementation("org.apache.groovy:groovy:4.0.24")
  implementation("io.opentelemetry.instrumentation:opentelemetry-instrumentation-annotations:2.9.0")

  testImplementation("net.javacrumbs.json-unit:json-unit-assertj:4.0.0")
  testImplementation("org.testcontainers:localstack:1.20.4")
  testImplementation("com.amazonaws:aws-java-sdk-core:1.12.778")
  testImplementation("org.awaitility:awaitility-kotlin:4.2.2")
  testImplementation("it.ozimov:embedded-redis:0.7.3") { exclude("org.slf4j", "slf4j-simple") }
}

kotlin {
  jvmToolchain(21)
}

tasks {
  withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    compilerOptions.jvmTarget = org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_21
  }
}
