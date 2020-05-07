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

  fun getAllMessages(eventTypeFilter: String?, textFilter: String?): List<StoredMessage> {

    return store.filterIfNotEmpty(eventTypeFilter) { it.eventType.Value == eventTypeFilter }
        .filterIfNotEmpty(textFilter) { it.message.Message.contains(textFilter!!.trim()) } // The function does the null check, but the compiler doesn't realise hence bang bang
        .takeLast(pageSize)
        .reversed()
        .toList()
  }

  fun getAllEventTypes(): List<String> {
    return store.map { it.eventType.Value }
        .distinct()
        .toList()
  }

  fun clear() {
    store.clear()
  }

  fun <T> MutableList<T>.filterIfNotEmpty(value: String?, predicate: (T) -> Boolean): MutableList<T> {
    return when {
      value.isNullOrBlank() -> this
      else -> filterTo(ArrayList(), predicate)
    }
  }
}