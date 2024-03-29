package uk.gov.justice.hmpps.offenderevents.service

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.boot.test.web.server.LocalServerPort
import software.amazon.awssdk.services.sqs.model.SendMessageRequest
import java.net.URL

class UiIntegrationTest : IntegrationTest() {

  @LocalServerPort
  private var port: Int = 0

  internal lateinit var baseUrl: String

  @BeforeEach
  internal fun setupPort() {
    baseUrl = "http://localhost:$port"
  }

  @Test
  fun `Should Display captured event`() {
    val message = "/messages/externalMovement.json".readResourceAsText()
    awsSqsClient.sendMessage(SendMessageRequest.builder().queueUrl(queueUrl).messageBody(message).build()).get()
    `Wait for empty queue`()

    val response = URL("$baseUrl/messages").readText()

    assertThat(response).contains("EXTERNAL_MOVEMENT_RECORD-INSERTED")
  }

  @Test
  fun `Should only retrieve last page of messages`() {
    val message1 = "/messages/externalMovement.json".readResourceAsText()
    val message2 = message1.replace("1200835", "1200836")
    val message3 = message1.replace("1200835", "1200837")

    awsSqsClient.sendMessage(SendMessageRequest.builder().queueUrl(queueUrl).messageBody(message1).build()).get()
    awsSqsClient.sendMessage(SendMessageRequest.builder().queueUrl(queueUrl).messageBody(message2).build()).get()
    awsSqsClient.sendMessage(SendMessageRequest.builder().queueUrl(queueUrl).messageBody(message3).build()).get()

    `Wait for empty queue`()

    val response = URL("$baseUrl/messages").readText()

    assertThat(response).doesNotContain("1200835")
    assertThat(response).contains("1200836")
    assertThat(response).contains("1200837")
  }

  @Test
  fun `Should filter on include event types`() {
    val message1 = "/messages/externalMovement.json".readResourceAsText()
    val message2 = message1.replace("EXTERNAL_MOVEMENT_RECORD-INSERTED", "INTERNAL_MOVEMENT_RECORD-INSERTED")
      .replace("1200835", "1200836")
    val message3 = message1.replace("EXTERNAL_MOVEMENT_RECORD-INSERTED", "ANOTHER_MOVEMENT_RECORD-INSERTED")
      .replace("1200835", "1200837")

    awsSqsClient.sendMessage(SendMessageRequest.builder().queueUrl(queueUrl).messageBody(message1).build()).get()
    awsSqsClient.sendMessage(SendMessageRequest.builder().queueUrl(queueUrl).messageBody(message2).build()).get()
    awsSqsClient.sendMessage(SendMessageRequest.builder().queueUrl(queueUrl).messageBody(message3).build()).get()

    `Wait for empty queue`()

    val response = URL("$baseUrl/messages?include-event-type-filter=INTERNAL_MOVEMENT_RECORD-INSERTED,ANOTHER_MOVEMENT_RECORD-INSERTED").readText()

    assertThat(response).doesNotContain("1200835")
    assertThat(response).contains("1200836")
    assertThat(response).contains("1200837")
  }

  @Test
  fun `Should filter on exclude event types`() {
    val message1 = "/messages/externalMovement.json".readResourceAsText()
    val message2 = message1.replace("EXTERNAL_MOVEMENT_RECORD-INSERTED", "INTERNAL_MOVEMENT_RECORD-INSERTED")
      .replace("1200835", "1200836")
    val message3 = message1.replace("EXTERNAL_MOVEMENT_RECORD-INSERTED", "ANOTHER_MOVEMENT_RECORD-INSERTED")
      .replace("1200835", "1200837")

    awsSqsClient.sendMessage(SendMessageRequest.builder().queueUrl(queueUrl).messageBody(message1).build()).get()
    awsSqsClient.sendMessage(SendMessageRequest.builder().queueUrl(queueUrl).messageBody(message2).build()).get()
    awsSqsClient.sendMessage(SendMessageRequest.builder().queueUrl(queueUrl).messageBody(message3).build()).get()

    `Wait for empty queue`()

    val response = URL("$baseUrl/messages?exclude-event-type-filter=EXTERNAL_MOVEMENT_RECORD-INSERTED,ANOTHER_MOVEMENT_RECORD-INSERTED").readText()

    assertThat(response).doesNotContain("1200835")
    assertThat(response).contains("1200836")
    assertThat(response).doesNotContain("1200837")
  }

  @Test
  fun `Should filter on text type`() {
    val message1 = "/messages/externalMovement.json".readResourceAsText()
    val message2 = message1.replace("1200835", "1200836")

    awsSqsClient.sendMessage(SendMessageRequest.builder().queueUrl(queueUrl).messageBody(message1).build()).get()
    awsSqsClient.sendMessage(SendMessageRequest.builder().queueUrl(queueUrl).messageBody(message2).build()).get()

    `Wait for empty queue`()

    val response = URL("$baseUrl/messages?text-filter=1200836").readText()

    assertThat(response).doesNotContain("1200835")
    assertThat(response).contains("1200836")
  }

  @Test
  fun `Should handle invalid json`() {
    val message1 = "/messages/externalMovement.json".readResourceAsText()
    val message2 = "/messages/invalidJson.json".readResourceAsText()

    awsSqsClient.sendMessage(SendMessageRequest.builder().queueUrl(queueUrl).messageBody(message1).build()).get()
    awsSqsClient.sendMessage(SendMessageRequest.builder().queueUrl(queueUrl).messageBody(message2).build()).get()

    `Wait for empty queue`()

    val response = URL("$baseUrl/messages").readText()

    assertThat(response).contains("1200835")
    assertThat(response).contains("INVALID-JSON")
  }

  @Test
  fun `Should filter case insensitively`() {
    val message1 = "/messages/externalMovement.json".readResourceAsText()

    awsSqsClient.sendMessage(SendMessageRequest.builder().queueUrl(queueUrl).messageBody(message1).build()).get()

    `Wait for empty queue`()

    val response = URL("$baseUrl/messages?text-filter=EVENTDATETIME").readText()

    assertThat(response).contains("1200835")
  }

  @Test
  fun `should filter on topic prison`() {
    val message1 = "/messages/externalMovement.json".readResourceAsText()
    val message2 = "/messages/tierCalculationRequired.json".readResourceAsText()
    val message3 = "/messages/tierCalculationComplete.json".readResourceAsText()

    awsSqsClient.sendMessage(SendMessageRequest.builder().queueUrl(queueUrl).messageBody(message1).build()).get()
    awsSqsClient.sendMessage(SendMessageRequest.builder().queueUrl(queueUrl).messageBody(message2).build()).get()
    awsSqsClient.sendMessage(SendMessageRequest.builder().queueUrl(queueUrl).messageBody(message3).build()).get()

    `Wait for empty queue`()

    val response = URL("$baseUrl/messages?include-topic-filter=Prison").readText()

    assertThat(response).contains("1200835")
    assertThat(response).contains("EXTERNAL_MOVEMENT_RECORD-INSERTED")
    assertThat(response).contains("Prison")
    assertThat(response).doesNotContain("X405099")
    assertThat(response).doesNotContain("X905358")
  }

  @Test
  fun `should filter on topic probation`() {
    val message1 = "/messages/externalMovement.json".readResourceAsText()
    val message2 = "/messages/tierCalculationRequired.json".readResourceAsText()
    val message3 = "/messages/tierCalculationComplete.json".readResourceAsText()

    awsSqsClient.sendMessage(SendMessageRequest.builder().queueUrl(queueUrl).messageBody(message1).build()).get()
    awsSqsClient.sendMessage(SendMessageRequest.builder().queueUrl(queueUrl).messageBody(message2).build()).get()
    awsSqsClient.sendMessage(SendMessageRequest.builder().queueUrl(queueUrl).messageBody(message3).build()).get()

    `Wait for empty queue`()

    val response = URL("$baseUrl/messages?include-topic-filter=Probation").readText()

    assertThat(response).doesNotContain("1200835")
    assertThat(response).contains("OFFENDER_MANAGEMENT_TIER_CALCULATION_REQUIRED")
    assertThat(response).contains("Probation")
    assertThat(response).contains("X405099")
    assertThat(response).doesNotContain("X905358")
  }

  @Test
  fun `should filter on topic domain`() {
    val message1 = "/messages/externalMovement.json".readResourceAsText()
    val message2 = "/messages/tierCalculationRequired.json".readResourceAsText()
    val message3 = "/messages/tierCalculationComplete.json".readResourceAsText()

    awsSqsClient.sendMessage(SendMessageRequest.builder().queueUrl(queueUrl).messageBody(message1).build()).get()
    awsSqsClient.sendMessage(SendMessageRequest.builder().queueUrl(queueUrl).messageBody(message2).build()).get()
    awsSqsClient.sendMessage(SendMessageRequest.builder().queueUrl(queueUrl).messageBody(message3).build()).get()

    `Wait for empty queue`()

    val response = URL("$baseUrl/messages?include-topic-filter=Domain").readText()

    assertThat(response).doesNotContain("1200835")
    assertThat(response).doesNotContain("X405099")
    assertThat(response).contains("TIER_CALCULATION_COMPLETE")
    assertThat(response).contains("Domain")
    assertThat(response).contains("X905358")
  }
}
