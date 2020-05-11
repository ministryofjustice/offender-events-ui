package uk.gov.justice.hmpps.offenderevents.service

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

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

  fun aMessage() = Message("ANY_MESSAGE", "ANY_MESSAGE_ID", MessageAttributes(EventType("ANY_EVENT_TYPE")))
}