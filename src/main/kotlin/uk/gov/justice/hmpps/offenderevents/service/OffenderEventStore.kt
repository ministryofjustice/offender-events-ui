package uk.gov.justice.hmpps.offenderevents.service

import com.google.common.reflect.TypeToken
import com.google.gson.Gson
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import uk.gov.justice.hmpps.offenderevents.resource.DisplayMessage

@Service
class OffenderEventStore(
  @Value("\${model.cacheSize}") private val cacheSize: Int,
  private val store: MutableList<DisplayMessage> = mutableListOf()
) : MutableList<DisplayMessage> by store {

  companion object {
    fun fromJson(json: String): MutableMap<String, String> {
      return try {
        Gson().fromJson(json, object : TypeToken<MutableMap<String, String>>() {}.type)
      } catch (e1: Exception) {
        try {
          val anyMap: Map<String, Any> = Gson().fromJson(json, object : TypeToken<Map<String, Any>>() {}.type)
          anyMap.entries.associate { it.key to it.value.toString() }.toMutableMap()
        } catch (e2: Exception) {
          mutableMapOf("BadMessage" to json, "CausedException" to e1.toString())
        }
      }
    }
    val log: Logger = LoggerFactory.getLogger(this::class.java)
  }

  override fun add(element: DisplayMessage) =
    store.add(element)
      .also {
        if (store.size > cacheSize) {
          log.info("Removing an element as cache size $cacheSize exceeded")
          store.removeAt(0)
        }
      }

  fun handleMessage(message: Message) {
    log.info("Store has ${this.size} messages before insertion. Store id=${this.hashCode()}")
    log.info("Adding message to store: $message")
    add(transformMessage(message))
    log.info("Store has ${this.size} messages after insertion. Store id=${this.hashCode()}")
  }

  fun getPageOfMessages(includeEventTypeFilter: List<String>?, excludeEventTypeFilter: List<String>?, textFilter: String?, pageSize: Int): List<DisplayMessage> =
    store.reversed()
      .asSequence()
      .filterIfNotEmpty(includeEventTypeFilter) { includeEventTypeFilter!!.contains(it.eventType) }
      .filterIfNotEmpty(excludeEventTypeFilter) { excludeEventTypeFilter!!.contains(it.eventType).not() }
      .filterIfNotEmpty(textFilter) { it.messageDetails.containsText(textFilter!!) }
      .take(pageSize)
      .toList()

  private fun Map<String, Any>.containsText(text: String): Boolean {
    return this.keys.any { it.contains(text.trim(), ignoreCase = true) } ||
      this.values.any { it.toString().contains(text.trim(), ignoreCase = true) }
  }

  private fun transformMessage(message: Message) =
    fromJson(message.Message)
      .also { keyValuePairs -> keyValuePairs.remove("eventType") }
      .let { keyValuePairs -> DisplayMessage(message.MessageAttributes.eventType.Value, keyValuePairs.toMap()) }

  fun getAllEventTypes(): List<String> =
    store.map { it.eventType }
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
