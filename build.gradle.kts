plugins {
  id("uk.gov.justice.hmpps.gradle-spring-boot") version "5.10.1"
  kotlin("plugin.spring") version "1.9.21"
  kotlin("plugin.jpa") version "1.9.21"
}

configurations {
  implementation { exclude(module = "spring-boot-starter-security") }
}

repositories {
  mavenCentral()
}

dependencyCheck {
  // Thymeleaf through 3.1.1.RELEASE, as used in spring-boot-admin (aka Spring Boot Admin) through 3.1.1 and other products, allows sandbox bypass via crafted HTML. This may be relevant for SSTI (Server Side Template Injection) and code execution in spring-boot-admin if MailNotifier is enabled and there is write access to environment variables via the UI.
  // Not relevant for us as can't pass html in.  Suppressed.
  suppressionFiles.add("thymeleaf-suppressions.xml")
}

dependencies {
  annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")

  implementation("org.springframework.boot:spring-boot-starter-data-redis")
  implementation("uk.gov.justice.service.hmpps:hmpps-sqs-spring-boot-starter:2.1.1")

  implementation("com.google.code.gson:gson:2.10.1")
  implementation("com.google.guava:guava:32.1.3-jre")

  implementation("org.springframework.boot:spring-boot-starter-thymeleaf")
  implementation("nz.net.ultraq.thymeleaf:thymeleaf-layout-dialect:3.3.0")
  implementation("org.apache.groovy:groovy:4.0.16")
  implementation("io.opentelemetry.instrumentation:opentelemetry-instrumentation-annotations:1.32.0")

  testImplementation("net.javacrumbs.json-unit:json-unit-assertj:3.2.2")
  testImplementation("org.testcontainers:localstack:1.19.3")
  testImplementation("com.amazonaws:aws-java-sdk-core:1.12.606")
  testImplementation("org.awaitility:awaitility-kotlin:4.2.0")
  testImplementation("it.ozimov:embedded-redis:0.7.3") { exclude("org.slf4j", "slf4j-simple") }
  testImplementation("commons-io:commons-io:2.15.1") // override vulnerable version
}

kotlin {
  jvmToolchain(21)
}

tasks {
  withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    kotlinOptions {
      jvmTarget = "21"
    }
  }
}
