plugins {
  id("uk.gov.justice.hmpps.gradle-spring-boot") version "4.5.5"
  kotlin("plugin.spring") version "1.7.10"
  kotlin("plugin.jpa") version "1.7.10"
}

configurations {
  implementation { exclude(module = "spring-boot-starter-security") }
}

dependencies {
  annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")

  implementation("org.springframework.boot:spring-boot-starter-data-redis")
  implementation("uk.gov.justice.service.hmpps:hmpps-sqs-spring-boot-starter:1.1.12")

  implementation("com.google.code.gson:gson:2.9.1")

  implementation("org.springframework.boot:spring-boot-starter-thymeleaf")
  implementation("nz.net.ultraq.thymeleaf:thymeleaf-layout-dialect:3.1.0")
  // pinned at 4.0.1 due to security vulnerability with 4.0.0 - remove when thymeleaf upgraded
  implementation("org.apache.groovy:groovy:4.0.5")

  testImplementation("net.javacrumbs.json-unit:json-unit-assertj:2.36.0")
  testImplementation("org.testcontainers:localstack:1.17.5")
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
