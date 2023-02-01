plugins {
  id("uk.gov.justice.hmpps.gradle-spring-boot") version "5.0.1"
  kotlin("plugin.spring") version "1.8.0"
  kotlin("plugin.jpa") version "1.8.0"
}

configurations {
  implementation { exclude(module = "spring-boot-starter-security") }
}

repositories {
  maven { url = uri("https://repo.spring.io/milestone") }
  mavenCentral()
}
dependencies {
  annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")

  implementation("org.springframework.boot:spring-boot-starter-data-redis")
  implementation("uk.gov.justice.service.hmpps:hmpps-sqs-spring-boot-starter:2.0.0-beta-10")

  implementation("com.google.code.gson:gson:2.10.1")

  implementation("org.springframework.boot:spring-boot-starter-thymeleaf")
  implementation("nz.net.ultraq.thymeleaf:thymeleaf-layout-dialect:3.2.0")
  implementation("org.apache.groovy:groovy:4.0.8")
  implementation("io.opentelemetry.instrumentation:opentelemetry-instrumentation-annotations:1.22.1")

  testImplementation("net.javacrumbs.json-unit:json-unit-assertj:2.36.1")
  testImplementation("org.testcontainers:localstack:1.17.6")
  testImplementation("org.awaitility:awaitility-kotlin:4.2.0")
  testImplementation("it.ozimov:embedded-redis:0.7.3") { exclude("org.slf4j", "slf4j-simple") }
  testImplementation("commons-io:commons-io:2.11.0") // override vulnerable version
}

java {
  toolchain.languageVersion.set(JavaLanguageVersion.of(18))
}

tasks {
  withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    kotlinOptions {
      jvmTarget = "18"
    }
  }
}
