package uk.gov.justice.hmpps.offenderevents.service

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import uk.gov.justice.hmpps.offenderevents.resource.DisplayMessage

class ListenerIntegrationTest : IntegrationTest() {
  @Test
  fun `Should consume and store an offender event message`() {
    val message = "/messages/externalMovement.json".readResourceAsText()

    awsSqsClient.sendMessage(queueUrl, message)

    `Wait for empty queue`()

    assertThat(eventStore.getPageOfMessages(null, null, null, null, null, 1))
      .extracting<String>(DisplayMessage::eventType)
      .containsExactly("EXTERNAL_MOVEMENT_RECORD-INSERTED")
  }

  @Test
  fun `Should consume and store an offender event message in Redis`() {
    val message = "/messages/externalMovement2.json".readResourceAsText()

    awsSqsClient.sendMessage(queueUrl, message)

    `Wait for empty queue`()

    val redisEvent = eventRepository.findById("b730c957-1e17-4739-9808-a3419cecdd4a")

    assertThat(redisEvent.get().messageId).isEqualTo("b730c957-1e17-4739-9808-a3419cecdd4a")
    assertThat(redisEvent.get().wholeMessage).isEqualTo(message)
  }
}
