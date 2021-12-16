@file:Suppress("ClassName")

package uk.gov.justice.hmpps.offenderevents.service

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.never
import org.mockito.kotlin.verify
import uk.gov.justice.hmpps.offenderevents.data.Event
import uk.gov.justice.hmpps.offenderevents.data.EventRepository

internal class OffenderEventListenerTest {
  private val offenderEventStore: OffenderEventStore = mock()
  private val eventRepository: EventRepository = mock()
  private val gson: Gson = GsonBuilder().create()
  private val excludedMessages: Set<String> = setOf("BOB", "FRED")
  private val listener: OffenderEventListener = OffenderEventListener(offenderEventStore, eventRepository, gson, excludedMessages)

  @Nested
  internal inner class receiveMessage {
    @Test
    fun `receive message`() {
      val message = "messages/externalMovement.json".readResourceAsText()
      listener.receiveMessage(message)
      verify(eventRepository).save(Event("b18d62f0-3059-5c2c-af05-346667bd4a28", message))
      verify(offenderEventStore).handleMessage(any())
    }

    @Test
    fun `ignore message`() {
      val message = "messages/externalMovement.json".readResourceAsText()
      listener.receiveMessage(message.replace("EXTERNAL_MOVEMENT_RECORD-INSERTED", "FRED"))
      verify(eventRepository, never()).save(any())
      verify(offenderEventStore, never()).handleMessage(any())
    }
  }

  @Suppress("RECEIVER_NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
  internal fun String.readResourceAsText(): String = OffenderEventListenerTest::class.java.classLoader.getResource(this).readText()
}
