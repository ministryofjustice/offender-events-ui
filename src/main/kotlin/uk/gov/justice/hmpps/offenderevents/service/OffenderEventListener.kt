package uk.gov.justice.hmpps.offenderevents.service

import com.google.gson.Gson
import io.awspring.cloud.sqs.annotation.SqsListener
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import uk.gov.justice.hmpps.offenderevents.data.Event
import uk.gov.justice.hmpps.offenderevents.data.EventRepository

data class EventType(val Value: String)
data class MessageAttributes(val eventType: EventType)
data class Message(val Message: String, val MessageId: String, val MessageAttributes: MessageAttributes, var TopicArn: String)

@Service
class OffenderEventListener(
  val offenderEventStore: OffenderEventStore,
  val eventRepository: EventRepository,
  val gson: Gson,
  @Value("\${sqs.messages.exclude}") val excludedMessages: Set<String>
) {

  private companion object {
    val log: Logger = LoggerFactory.getLogger(this::class.java)
  }

  @SqsListener(queueNames = ["event"], factory = "hmppsQueueContainerFactoryProxy", maxInflightMessagesPerQueue = "1")
  fun receiveMessage(requestJson: String) {
    log.debug("Offender event received raw message: {}", requestJson)
    val message = gson.fromJson(requestJson, Message::class.java)
    val eventType = EventType(message.MessageAttributes.eventType.Value)
    if (excludedMessages.contains(eventType.Value)) {
      log.info("Excluding message {} type {}", message.MessageId, eventType.Value)
    } else {
      log.info("Received message {} type {}", message.MessageId, eventType.Value)
      eventRepository.save(Event(message.MessageId, requestJson))
      offenderEventStore.handleMessage(message)
    }
  }
}
