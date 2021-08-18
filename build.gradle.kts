plugins {
  id("uk.gov.justice.hmpps.gradle-spring-boot") version "3.3.6"
  kotlin("plugin.spring") version "1.5.21"
  kotlin("plugin.jpa") version "1.5.21"
}

configurations {
  implementation { exclude(module = "spring-boot-starter-security") }
}

dependencies {
  annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")

  implementation("org.springframework.boot:spring-boot-starter-data-redis")
  implementation("uk.gov.justice.service.hmpps:hmpps-sqs-spring-boot-starter:0.9.0")

  implementation("com.google.code.gson:gson:2.8.7")

  implementation("org.springframework.boot:spring-boot-starter-thymeleaf")
  implementation("nz.net.ultraq.thymeleaf:thymeleaf-layout-dialect:2.5.3")

  testImplementation("net.javacrumbs.json-unit:json-unit-assertj:2.27.0")
  testImplementation("org.testcontainers:localstack:1.16.0")
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
