plugins {
  id("uk.gov.justice.hmpps.gradle-spring-boot") version "2.1.0"
  kotlin("plugin.spring") version "1.4.21"
  kotlin("plugin.jpa") version "1.4.21"
}

dependencies {
  annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")

  implementation("org.springframework.boot:spring-boot-devtools")
  implementation("org.springframework:spring-jms")

  implementation("com.google.code.gson:gson:2.8.6")

  implementation("com.amazonaws:amazon-sqs-java-messaging-lib:1.0.8")

  implementation("org.springframework.boot:spring-boot-starter-thymeleaf")
  implementation("nz.net.ultraq.thymeleaf:thymeleaf-layout-dialect:2.5.1")

  testImplementation("net.javacrumbs.json-unit:json-unit-assertj:2.22.0")
  testImplementation("org.testcontainers:localstack:1.15.1")
  testImplementation("org.awaitility:awaitility-kotlin:4.0.3")
}
