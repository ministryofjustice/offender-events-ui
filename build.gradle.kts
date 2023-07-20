plugins {
  id("uk.gov.justice.hmpps.gradle-spring-boot") version "5.3.0"
  kotlin("plugin.spring") version "1.9.0"
  kotlin("plugin.jpa") version "1.9.0"
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
  implementation("uk.gov.justice.service.hmpps:hmpps-sqs-spring-boot-starter:2.0.1")

  implementation("com.google.code.gson:gson:2.10.1")
  implementation("com.google.guava:guava:32.1.1-jre")

  implementation("org.springframework.boot:spring-boot-starter-thymeleaf")
  implementation("nz.net.ultraq.thymeleaf:thymeleaf-layout-dialect:3.2.1")
  implementation("org.apache.groovy:groovy:4.0.13")
  implementation("io.opentelemetry.instrumentation:opentelemetry-instrumentation-annotations:1.28.0")

  testImplementation("net.javacrumbs.json-unit:json-unit-assertj:3.0.0")
  testImplementation("org.testcontainers:localstack:1.18.3")
  testImplementation("com.amazonaws:aws-java-sdk-core:1.12.510")
  testImplementation("org.awaitility:awaitility-kotlin:4.2.0")
  testImplementation("it.ozimov:embedded-redis:0.7.3") { exclude("org.slf4j", "slf4j-simple") }
  testImplementation("commons-io:commons-io:2.13.0") // override vulnerable version
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
