package uk.gov.justice.hmpps.offenderevents.service

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import uk.gov.justice.hmpps.offenderevents.data.EventRepository
import uk.gov.justice.hmpps.offenderevents.resource.DisplayMessage

class OffenderEventStoreTest {

  companion object {
    const val CACHE_SIZE = 3
  }

  private val eventRepository = mock<EventRepository>()
  private val offenderEventStore = OffenderEventStore(CACHE_SIZE, eventRepository)

  @Test
  fun `The cache size is respected`() {
    val message = aMessage()
    (0 until CACHE_SIZE + 1).forEach { _ ->
      offenderEventStore.handleMessage(message)
    }

    assertThat(offenderEventStore.store.size).isEqualTo(CACHE_SIZE)
  }

  @Test
  fun `The cache is first in first out`() {
    offenderEventStore.handleMessage(aMessage("1"))
    offenderEventStore.handleMessage(aMessage("2"))
    offenderEventStore.handleMessage(aMessage("3"))
    offenderEventStore.handleMessage(aMessage("4"))

    assertThat(offenderEventStore.getAllEventTypes()).doesNotContain("1")
    verify(eventRepository).deleteById("1")
    assertThat(offenderEventStore.getAllEventTypes()).contains("2")
    assertThat(offenderEventStore.getAllEventTypes()).contains("3")
    assertThat(offenderEventStore.getAllEventTypes()).contains("4")
  }

  @Test
  fun `The include event type filter is applied`() {
    offenderEventStore.handleMessage(aMessage().copy(MessageAttributes = MessageAttributes(EventType("1"))))
    offenderEventStore.handleMessage(aMessage().copy(MessageAttributes = MessageAttributes(EventType("2"))))

    assertThat(offenderEventStore.getPageOfMessages(listOf("1"), null, null, null, null, 2))
      .extracting<String>(DisplayMessage::eventType).containsExactly("1")
  }

  @Test
  fun `Multiple include event types are applied`() {
    offenderEventStore.handleMessage(aMessage().copy(MessageAttributes = MessageAttributes(EventType("1"))))
    offenderEventStore.handleMessage(aMessage().copy(MessageAttributes = MessageAttributes(EventType("2"))))
    offenderEventStore.handleMessage(aMessage().copy(MessageAttributes = MessageAttributes(EventType("3"))))

    assertThat(offenderEventStore.getPageOfMessages(listOf("1", "3"), null, null, null, null, 3))
      .extracting<String>(DisplayMessage::eventType).containsExactlyInAnyOrder("1", "3")
  }

  @Test
  fun `The exclude event type filter is applied`() {
    offenderEventStore.handleMessage(aMessage().copy(MessageAttributes = MessageAttributes(EventType("1"))))
    offenderEventStore.handleMessage(aMessage().copy(MessageAttributes = MessageAttributes(EventType("2"))))

    assertThat(offenderEventStore.getPageOfMessages(null, listOf("1"), null, null, null, 2))
      .extracting<String>(DisplayMessage::eventType).containsExactly("2")
  }

  @Test
  fun `Multiple exclude event types are applied`() {
    offenderEventStore.handleMessage(aMessage().copy(MessageAttributes = MessageAttributes(EventType("1"))))
    offenderEventStore.handleMessage(aMessage().copy(MessageAttributes = MessageAttributes(EventType("2"))))
    offenderEventStore.handleMessage(aMessage().copy(MessageAttributes = MessageAttributes(EventType("3"))))

    assertThat(offenderEventStore.getPageOfMessages(null, listOf("1", "3"), null, null, null, 3))
      .extracting<String>(DisplayMessage::eventType).containsExactlyInAnyOrder("2")
  }

  @Test
  fun `A combination of include and exclude event types are applied`() {
    offenderEventStore.handleMessage(aMessage().copy(MessageAttributes = MessageAttributes(EventType("1"))))
    offenderEventStore.handleMessage(aMessage().copy(MessageAttributes = MessageAttributes(EventType("2"))))
    offenderEventStore.handleMessage(aMessage().copy(MessageAttributes = MessageAttributes(EventType("3"))))

    assertThat(offenderEventStore.getPageOfMessages(listOf("1", "2"), listOf("2", "3"), null, null, null, 3))
      .extracting<String>(DisplayMessage::eventType).containsExactlyInAnyOrder("1")
  }

  @Test
  fun `Nested arrays can be deserialized`() {
    offenderEventStore.handleMessage(aNestedArrayMessage())

    val stored = offenderEventStore.getPageOfMessages(listOf(), listOf(), listOf(), listOf(), "", 1)
    assertThat(stored.get(0).messageDetails["offenderIdDisplay"]).isEqualTo("G0373GG")
    assertThat(stored.get(0).messageDetails["offenders"]).contains("1025558")
  }

  private fun aMessage(identifier: String = "ANY_MESSAGE_ID") = Message(
    "{\"ANY_KEY\": \"ANY_MESSAGE\"}",
    identifier,
    MessageAttributes(EventType(identifier)),
    "f221e27fcfcf78f6ab4f4c3cc165eee7"
  )

  private fun aNestedArrayMessage() = Message(
    "{\"offenderIdDisplay\":\"G0373GG\",\"offenders\":[{\"offenderId\":1025558,\"bookings\":[{\"offenderBookId\":12678}]}]}",
    "NESTED_MESSAGE_ID",
    MessageAttributes(EventType("DATA_COMPLIANCE_DELETE-OFFENDER")),
    "f221e27fcfcf78f6ab4f4c3cc165eee7",
  )
}
