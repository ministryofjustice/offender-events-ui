package uk.gov.justice.hmpps.offenderevents.service

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import uk.gov.justice.hmpps.offenderevents.resource.DisplayMessage

class OffenderEventStoreTest {

  companion object {
    const val CACHE_SIZE = 3
  }

  private val offenderEventStore = OffenderEventStore(CACHE_SIZE)

  @Test
  fun `The cache size is respected`() {
    val message = aMessage()
    (0 until CACHE_SIZE+1).forEach { _ ->
      offenderEventStore.handleMessage(message)
    }

    assertThat(offenderEventStore.size).isEqualTo(CACHE_SIZE)
  }

  @Test
  fun `The cache is first in first out`() {
    offenderEventStore.handleMessage(aMessage().copy(MessageAttributes = MessageAttributes(EventType("1"))))
    offenderEventStore.handleMessage(aMessage().copy(MessageAttributes = MessageAttributes(EventType("2"))))
    offenderEventStore.handleMessage(aMessage().copy(MessageAttributes = MessageAttributes(EventType("3"))))
    offenderEventStore.handleMessage(aMessage().copy(MessageAttributes = MessageAttributes(EventType("4"))))

    assertThat(offenderEventStore.getAllEventTypes()).doesNotContain("1")
    assertThat(offenderEventStore.getAllEventTypes()).contains("2")
    assertThat(offenderEventStore.getAllEventTypes()).contains("3")
    assertThat(offenderEventStore.getAllEventTypes()).contains("4")
  }

  @Test
  fun `The include event type filter is applied`() {
    offenderEventStore.handleMessage(aMessage().copy(MessageAttributes = MessageAttributes(EventType("1"))))
    offenderEventStore.handleMessage(aMessage().copy(MessageAttributes = MessageAttributes(EventType("2"))))

    assertThat(offenderEventStore.getPageOfMessages(listOf("1"), null, null, 2))
        .extracting<String>(DisplayMessage::eventType).containsExactly("1")
  }

  @Test
  fun `Multiple include event types are applied`() {
    offenderEventStore.handleMessage(aMessage().copy(MessageAttributes = MessageAttributes(EventType("1"))))
    offenderEventStore.handleMessage(aMessage().copy(MessageAttributes = MessageAttributes(EventType("2"))))
    offenderEventStore.handleMessage(aMessage().copy(MessageAttributes = MessageAttributes(EventType("3"))))

    assertThat(offenderEventStore.getPageOfMessages(listOf("1", "3"), null, null, 3))
        .extracting<String>(DisplayMessage::eventType).containsExactlyInAnyOrder("1", "3")
  }

  @Test
  fun `The exclude event type filter is applied`() {
    offenderEventStore.handleMessage(aMessage().copy(MessageAttributes = MessageAttributes(EventType("1"))))
    offenderEventStore.handleMessage(aMessage().copy(MessageAttributes = MessageAttributes(EventType("2"))))

    assertThat(offenderEventStore.getPageOfMessages(null, listOf("1"), null, 2))
        .extracting<String>(DisplayMessage::eventType).containsExactly("2")
  }

  @Test
  fun `Multiple exclude event types are applied`() {
    offenderEventStore.handleMessage(aMessage().copy(MessageAttributes = MessageAttributes(EventType("1"))))
    offenderEventStore.handleMessage(aMessage().copy(MessageAttributes = MessageAttributes(EventType("2"))))
    offenderEventStore.handleMessage(aMessage().copy(MessageAttributes = MessageAttributes(EventType("3"))))

    assertThat(offenderEventStore.getPageOfMessages(null, listOf("1", "3"), null, 3))
        .extracting<String>(DisplayMessage::eventType).containsExactlyInAnyOrder("2")
  }

  @Test
  fun `A combination of include and exclude event types are applied`() {
    offenderEventStore.handleMessage(aMessage().copy(MessageAttributes = MessageAttributes(EventType("1"))))
    offenderEventStore.handleMessage(aMessage().copy(MessageAttributes = MessageAttributes(EventType("2"))))
    offenderEventStore.handleMessage(aMessage().copy(MessageAttributes = MessageAttributes(EventType("3"))))

    assertThat(offenderEventStore.getPageOfMessages(listOf("1", "2"), listOf("2", "3"), null, 3))
        .extracting<String>(DisplayMessage::eventType).containsExactlyInAnyOrder("1")
  }

  fun aMessage() = Message("ANY_MESSAGE", "ANY_MESSAGE_ID", MessageAttributes(EventType("ANY_EVENT_TYPE")))
}