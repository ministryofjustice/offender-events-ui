package uk.gov.justice.hmpps.offenderevents.service

import org.springframework.stereotype.Service

data class StoredMessage(val eventType: EventType, val message: Message)

@Service
class OffenderEventStore {

  private val store: MutableList<StoredMessage> = mutableListOf()

  fun handleMessage(eventType: EventType, message: Message) {
    store.add(StoredMessage(eventType, message))
  }

  fun getAllMessages(): List<StoredMessage> {
    return store.toList()
  }

}