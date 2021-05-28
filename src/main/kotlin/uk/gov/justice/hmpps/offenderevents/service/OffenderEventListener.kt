package uk.gov.justice.hmpps.offenderevents.service

import com.microsoft.applicationinsights.core.dependencies.google.gson.Gson
import com.microsoft.applicationinsights.core.dependencies.google.gson.GsonBuilder
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.jms.annotation.JmsListener
import org.springframework.stereotype.Service
import uk.gov.justice.hmpps.offenderevents.data.Event
import uk.gov.justice.hmpps.offenderevents.data.EventRepository

data class EventType(val Value: String)
data class MessageAttributes(val eventType: EventType)
data class Message(val Message: String, val MessageId: String, val MessageAttributes: MessageAttributes, var TopicArn: String)

@Service
class OffenderEventListener(
  val offenderEventStore: OffenderEventStore,
  val eventRepository: EventRepository
) {

  companion object {
    val log: Logger = LoggerFactory.getLogger(this::class.java)
    val gson: Gson = GsonBuilder().create()
  }

  val topicMap = mapOf(
    "f221e27fcfcf78f6ab4f4c3cc165eee7" to "Prison",
    "453cac1179377186788c5fcd12525870" to "Probation",
    "e29fb030a51b3576dd645aa5e460e573" to "Domain"
  )

  @JmsListener(destination = "\${sqs.queue.name}")
  fun receiveMessage(requestJson: String) {
    log.debug("Offender event received raw message: $requestJson")
    val message = gson.fromJson(requestJson, Message::class.java)
    val topic = topicMap[message.TopicArn.substringAfterLast('-')].toString()
    val eventType = EventType(message.MessageAttributes.eventType.Value)
    log.info("Received message ${message.MessageId} type ${eventType.Value}")

    eventRepository.save(Event(message.MessageId, requestJson, topic))

    offenderEventStore.handleMessage(message, topic)
  }

  enum class topics(val description: String) {
    PRISON("Prison"),
    PROBATION("Probation"),
    DOMAIN("Domain")
  }
}
