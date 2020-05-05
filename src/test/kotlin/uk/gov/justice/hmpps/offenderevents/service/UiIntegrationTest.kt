package uk.gov.justice.hmpps.offenderevents.service

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.boot.web.server.LocalServerPort
import java.net.URL

class UiIntegrationTest : IntegrationTest() {

  @LocalServerPort
  private var port: Int = 0

  internal lateinit var baseUrl: String

  @BeforeEach
  internal fun setupPort() {
    baseUrl = "http://localhost:$port/"
  }

  @Test
  fun `Should Display captured event`() {
    val message = "/messages/externalMovement.json".readResourceAsText()
    awsSqsClient.sendMessage(queueUrl, message)
    `Wait for empty queue`()

    val response = URL("$baseUrl/messages").readText()

    assertThat(response).contains("EXTERNAL_MOVEMENT_RECORD-INSERTED")
  }

  @Test
  fun `Should only retrieve last page of messages`() {
    val message1 = "/messages/externalMovement.json".readResourceAsText()
    val message2 = message1.replace("1200835", "1200836")
    val message3 = message1.replace("1200835", "1200837")

    awsSqsClient.sendMessage(queueUrl, message1)
    awsSqsClient.sendMessage(queueUrl, message2)
    awsSqsClient.sendMessage(queueUrl, message3)

    `Wait for empty queue`()

    val response = URL("$baseUrl/messages").readText()

    assertThat(response).doesNotContain("1200835")
    assertThat(response).contains("1200836")
    assertThat(response).contains("1200837")
  }


}