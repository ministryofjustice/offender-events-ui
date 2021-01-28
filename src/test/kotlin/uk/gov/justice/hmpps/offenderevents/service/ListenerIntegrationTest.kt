package uk.gov.justice.hmpps.offenderevents.service

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import uk.gov.justice.hmpps.offenderevents.resource.DisplayMessage

class ListenerIntegrationTest : IntegrationTest() {

  @Test
  fun `Should consume and store an offender event message`() {
    val message = "/messages/externalMovement.json".readResourceAsText()

    awsSqsClient.sendMessage(queueUrl, message)

    `Wait for messages to be processed`()
    `Wait for event store to contain messages`(1)

    assertThat(eventStore.getPageOfMessages(null, null, null, 1))
      .extracting<String>(DisplayMessage::eventType)
      .containsExactly("EXTERNAL_MOVEMENT_RECORD-INSERTED")
  }
}
