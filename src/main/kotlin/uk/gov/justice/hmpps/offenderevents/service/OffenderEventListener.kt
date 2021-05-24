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
data class Message(val Message: String, val MessageId: String, val MessageAttributes: MessageAttributes)

@Service
class OffenderEventListener(
  val offenderEventStore: OffenderEventStore,
  val eventRepository: EventRepository
) {

  companion object {
    val log: Logger = LoggerFactory.getLogger(this::class.java)
    val gson: Gson = GsonBuilder().create()
  }

  @JmsListener(destination = "\${sqs.queue.name}")
  fun receiveMessage(requestJson: String) {
    log.debug("Offender event received raw message: $requestJson")
    val message = gson.fromJson(requestJson, Message::class.java)
    val eventType = EventType(message.MessageAttributes.eventType.Value)
    log.info("Received message ${message.MessageId} type ${eventType.Value}")

    eventRepository.save(Event(message.MessageId, requestJson))

    offenderEventStore.handleMessage(message)
  }
}
