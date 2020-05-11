package uk.gov.justice.hmpps.offenderevents.service

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service

data class StoredMessage(val eventType: EventType, val message: Message)

@Service
class OffenderEventStore(@Value("\${model.cacheSize}") val cacheSize: Int,
                         private val store: MutableList<StoredMessage> = mutableListOf()) : MutableList<StoredMessage> by store {

  override fun add(element: StoredMessage) =
      store.add(element)
          .also { if (store.size > cacheSize) store.removeAt(0) }

  fun handleMessage(message: Message) = add(StoredMessage(message.MessageAttributes.eventType, message))

  fun getPageOfMessages(eventTypeFilter: String?, textFilter: String?, pageSize: Int): List<StoredMessage> =
      store.reversed()
          .asSequence()
          .filterIfNotEmpty(eventTypeFilter) { it.eventType.Value == eventTypeFilter }
          .filterIfNotEmpty(textFilter) { it.message.Message.contains(textFilter!!.trim()) } // The function does the null check, but the compiler doesn't realise hence bang bang
          .take(pageSize)
          .toList()

  fun getAllEventTypes(): List<String> =
      store.map { it.eventType.Value }
          .distinct()
          .toList()

  private fun <T> Sequence<T>.filterIfNotEmpty(value: String?, predicate: (T) -> Boolean): Sequence<T> {
    return when {
      value.isNullOrBlank() -> this
      else -> filter(predicate)
    }
  }
}