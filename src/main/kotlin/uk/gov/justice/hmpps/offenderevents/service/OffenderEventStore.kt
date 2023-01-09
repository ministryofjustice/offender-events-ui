@file:Suppress("UnstableApiUsage")

package uk.gov.justice.hmpps.offenderevents.service

import com.google.common.reflect.TypeToken
import com.google.gson.Gson
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import uk.gov.justice.hmpps.offenderevents.data.EventRepository
import uk.gov.justice.hmpps.offenderevents.resource.DisplayMessage
import uk.gov.justice.hmpps.offenderevents.service.OffenderEventStore.Topics.DOMAIN
import uk.gov.justice.hmpps.offenderevents.service.OffenderEventStore.Topics.PRISON
import uk.gov.justice.hmpps.offenderevents.service.OffenderEventStore.Topics.PROBATION
import uk.gov.justice.hmpps.offenderevents.service.OffenderEventStore.Topics.UNKNOWN

@Service
class OffenderEventStore(
  @Value("\${model.cacheSize}") private val cacheSize: Int,
  val eventRepository: EventRepository,
  val store: ArrayDeque<DisplayMessage> = ArrayDeque(),
) {

  companion object {
    fun fromJson(json: String): MutableMap<String, String?> {
      return try {
        Gson().fromJson(json, object : TypeToken<MutableMap<String, String?>>() {}.type)
      } catch (e1: Exception) {
        try {
          val anyMap: Map<String, Any?> = Gson().fromJson(json, object : TypeToken<Map<String, Any?>>() {}.type)
          anyMap.entries.associate { it.key to it.value?.toString() }.toMutableMap()
        } catch (e2: Exception) {
          mutableMapOf("BadMessage" to json, "CausedException" to e1.toString())
        }
      }
    }
    val log = LoggerFactory.getLogger(this::class.java)
  }

  fun add(element: DisplayMessage) = store.addLast(element)
    .also {
      if (store.size > cacheSize) {
        val removed = store.removeFirst()
        eventRepository.deleteById(removed.messageId)
      }
    }

  val topicMap = mapOf(
    "f221e27fcfcf78f6ab4f4c3cc165eee7" to PRISON,
    "453cac1179377186788c5fcd12525870" to PROBATION,
    "e29fb030a51b3576dd645aa5e460e573" to DOMAIN,
  )

  fun handleMessage(message: Message) = add(transformMessage(message))

  enum class Topics(val description: String) {
    PRISON("Prison"),
    PROBATION("Probation"),
    DOMAIN("Domain"),
    UNKNOWN("Unknown"),
  }

  fun getPageOfMessages(
    includeEventTypeFilter: List<String>?,
    excludeEventTypeFilter: List<String>?,
    includeTopicFilter: List<String>?,
    excludeTopicFilter: List<String>?,
    textFilter: String?,
    pageSize: Int
  ): List<DisplayMessage> =
    store.reversed()
      .asSequence()
      .onEach { it?.eventType ?: log.error("Unable to process stored event $it") }
      .filter { it?.eventType != null }
      .filterIfNotEmpty(includeEventTypeFilter) { includeEventTypeFilter!!.contains(it.eventType) }
      .filterIfNotEmpty(excludeEventTypeFilter) { excludeEventTypeFilter!!.contains(it.eventType).not() }
      .filterIfNotEmpty(includeTopicFilter) { includeTopicFilter!!.contains(it.topic) }
      .filterIfNotEmpty(excludeTopicFilter) { excludeTopicFilter!!.contains(it.topic).not() }
      .filterIfNotEmpty(textFilter) { message -> message.messageDetails.containsText(textFilter!!) }
      .take(pageSize)
      .toList()

  private fun Map<String, Any?>.containsText(text: String): Boolean {
    return this.keys.any { it.contains(text.trim(), ignoreCase = true) } ||
      this.values.any { it?.toString()?.contains(text.trim(), ignoreCase = true) ?: false }
  }

  private fun transformMessage(message: Message): DisplayMessage {
    val topic = topicMap[message.TopicArn.substringAfterLast('-')] ?: UNKNOWN
    return fromJson(message.Message)
      .also { keyValuePairs -> keyValuePairs.remove("eventType") }
      .let { keyValuePairs ->
        DisplayMessage(
          message.MessageAttributes.eventType.Value,
          message.MessageId,
          keyValuePairs.toMap(),
          topic.description
        )
      }
  }

  fun getAllEventTypes(): List<String> =
    store.mapNotNull { it.eventType }
      .distinct()
      .sorted()
      .toList()

  fun getAllTopics(): List<String> =
    store.mapNotNull { it.topic }
      .distinct()
      .sorted()
      .toList()

  private fun <T> Sequence<T>.filterIfNotEmpty(value: String?, predicate: (T) -> Boolean): Sequence<T> {
    return when {
      value.isNullOrBlank() -> this
      else -> filter(predicate)
    }
  }

  private fun <T> Sequence<T>.filterIfNotEmpty(value: List<String>?, predicate: (T) -> Boolean): Sequence<T> {
    return when {
      value.isNullOrEmpty() -> this
      else -> filter(predicate)
    }
  }
}
