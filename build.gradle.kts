plugins {
  id("uk.gov.justice.hmpps.gradle-spring-boot") version "3.3.0-beta"
  kotlin("plugin.spring") version "1.5.10"
  kotlin("plugin.jpa") version "1.5.10"
}

dependencies {
  annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")

  implementation("org.springframework.boot:spring-boot-starter-data-redis")
  implementation("org.springframework:spring-jms")

  implementation("com.google.code.gson:gson:2.8.7")

  implementation("com.amazonaws:amazon-sqs-java-messaging-lib:1.0.8")

  implementation("org.springframework.boot:spring-boot-starter-thymeleaf")
  implementation("nz.net.ultraq.thymeleaf:thymeleaf-layout-dialect:2.5.3")

  testImplementation("net.javacrumbs.json-unit:json-unit-assertj:2.26.0")
  testImplementation("org.testcontainers:localstack:1.15.3")
  testImplementation("org.awaitility:awaitility-kotlin:4.1.0")
  testImplementation("it.ozimov:embedded-redis:0.7.3") { exclude("org.slf4j", "slf4j-simple") }
}

tasks {
  compileKotlin {
    kotlinOptions {
      jvmTarget = "16"
    }
  }
}
