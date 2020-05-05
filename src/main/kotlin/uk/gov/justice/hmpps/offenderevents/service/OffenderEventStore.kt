package uk.gov.justice.hmpps.offenderevents.service

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service

data class StoredMessage(val eventType: EventType, val message: Message)

@Service
class OffenderEventStore(@Value("\${ui.pageSize}") val pageSize: Int) {

  private val store: MutableList<StoredMessage> = mutableListOf()

  fun handleMessage(eventType: EventType, message: Message) {
    store.add(StoredMessage(eventType, message))
  }

  fun getAllMessages(): List<StoredMessage> {
    return store.takeLast(pageSize).toList()
  }

  fun clear() {
    store.clear()
  }

}