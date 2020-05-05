package uk.gov.justice.hmpps.offenderevents.service

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class ListenerIntegrationTest : IntegrationTest() {

  @Test
  fun `Should consume and store an offender event message`() {
    val message = "/messages/externalMovement.json".readResourceAsText()

    awsSqsClient.sendMessage(queueUrl, message)

    `Wait for empty queue`()

    assertThat(eventStore.getAllMessages()).extracting<EventType>(StoredMessage::eventType).containsExactly(EventType("EXTERNAL_MOVEMENT_RECORD-INSERTED"))

  }

}


