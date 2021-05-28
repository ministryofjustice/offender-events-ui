package uk.gov.justice.hmpps.offenderevents.service

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.context.event.ContextRefreshedEvent
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component
import uk.gov.justice.hmpps.offenderevents.data.EventRepository

@Component
class OffenderEventStartUp(
  val offenderEventStore: OffenderEventStore,
  val eventRepository: EventRepository,
  val gson: com.google.gson.Gson,
) {
  private companion object {
    val log: Logger = LoggerFactory.getLogger(this::class.java)
  }

  @EventListener(ContextRefreshedEvent::class)
  fun readAllEvents() {
    log.info("Found {} items in redis", eventRepository.count())
    // oddly expired messages come through as null so have to filter out
    eventRepository.findAll().filterNotNull().forEach {
      val message = gson.fromJson(it.wholeMessage, Message::class.java)
      val eventType = EventType(message.MessageAttributes.eventType.Value)
      val topic = it.topic
      log.info("Reloaded message {} of type {}", message.MessageId, eventType.Value)

      offenderEventStore.handleMessage(message, topic)
    }
    log.info("Finished loading existing messages")
  }
}
