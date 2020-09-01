plugins {
  id("uk.gov.justice.hmpps.gradle-spring-boot") version "1.0.0"
  kotlin("plugin.spring") version "1.4.0"
  kotlin("plugin.jpa") version "1.4.0"
}

dependencies {
  annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")

  implementation("org.springframework.boot:spring-boot-devtools")
  implementation("org.springframework:spring-jms")

  implementation("com.google.code.gson:gson:2.8.6")

  implementation("com.amazonaws:amazon-sqs-java-messaging-lib:1.0.8")

  implementation("org.springframework.boot:spring-boot-starter-thymeleaf")
  implementation("nz.net.ultraq.thymeleaf:thymeleaf-layout-dialect:2.4.1")

  testImplementation("net.javacrumbs.json-unit:json-unit-assertj:2.18.1")
  testImplementation("org.testcontainers:localstack:1.14.3")
  testImplementation("org.awaitility:awaitility-kotlin:4.0.3")
}
