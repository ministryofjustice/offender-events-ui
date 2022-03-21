plugins {
  id("uk.gov.justice.hmpps.gradle-spring-boot") version "4.1.0"
  kotlin("plugin.spring") version "1.6.10"
  kotlin("plugin.jpa") version "1.6.10"
}

configurations {
  implementation { exclude(module = "spring-boot-starter-security") }
}

dependencies {
  annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")

  implementation("org.springframework.boot:spring-boot-starter-data-redis")
  implementation("uk.gov.justice.service.hmpps:hmpps-sqs-spring-boot-starter:1.1.1")

  implementation("com.google.code.gson:gson:2.9.0")

  implementation("org.springframework.boot:spring-boot-starter-thymeleaf")
  implementation("nz.net.ultraq.thymeleaf:thymeleaf-layout-dialect:3.1.0")
  // pinned at 4.0.1 due to security vulnerability with 4.0.0 - remove when thymeleaf upgraded
  implementation("org.apache.groovy:groovy:4.0.1")

  testImplementation("net.javacrumbs.json-unit:json-unit-assertj:2.32.0")
  testImplementation("org.testcontainers:localstack:1.16.3")
  testImplementation("org.awaitility:awaitility-kotlin:4.2.0")
  testImplementation("it.ozimov:embedded-redis:0.7.3") { exclude("org.slf4j", "slf4j-simple") }
}

java {
  toolchain.languageVersion.set(JavaLanguageVersion.of(17))
}

tasks {
  withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    kotlinOptions {
      jvmTarget = "17"
    }
  }
}
