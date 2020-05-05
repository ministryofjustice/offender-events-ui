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


}