plugins {
  id("uk.gov.justice.hmpps.gradle-spring-boot") version "6.0.0"
  kotlin("plugin.spring") version "2.0.0"
  kotlin("plugin.jpa") version "2.0.0"
}

configurations {
  implementation { exclude(module = "spring-boot-starter-security") }
}

dependencies {
  annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")

  implementation("org.springframework.boot:spring-boot-starter-data-redis")
  implementation("uk.gov.justice.service.hmpps:hmpps-sqs-spring-boot-starter:4.0.0")

  implementation("com.google.code.gson:gson:2.11.0")
  implementation("com.google.guava:guava:33.2.0-jre")

  implementation("org.springframework.boot:spring-boot-starter-thymeleaf")
  implementation("nz.net.ultraq.thymeleaf:thymeleaf-layout-dialect:3.3.0")
  implementation("org.apache.groovy:groovy:4.0.21")
  implementation("io.opentelemetry.instrumentation:opentelemetry-instrumentation-annotations:1.33.3")

  testImplementation("net.javacrumbs.json-unit:json-unit-assertj:3.2.7")
  testImplementation("org.testcontainers:localstack:1.19.8")
  testImplementation("com.amazonaws:aws-java-sdk-core:1.12.730")
  testImplementation("org.awaitility:awaitility-kotlin:4.2.1")
  testImplementation("it.ozimov:embedded-redis:0.7.3") { exclude("org.slf4j", "slf4j-simple") }
  testImplementation("commons-io:commons-io:2.16.1") // override vulnerable version
}

kotlin {
  jvmToolchain(21)
}

tasks {
  withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    compilerOptions.jvmTarget = org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_21
  }
}
