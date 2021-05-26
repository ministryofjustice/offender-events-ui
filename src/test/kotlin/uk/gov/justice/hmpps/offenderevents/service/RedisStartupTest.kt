package uk.gov.justice.hmpps.offenderevents.service

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import uk.gov.justice.hmpps.offenderevents.data.Event
import uk.gov.justice.hmpps.offenderevents.data.EventRepository
import uk.gov.justice.hmpps.offenderevents.resource.DisplayMessage

class RedisStartupTest : IntegrationTest() {
  @TestConfiguration
  class TestConfig(val eventRepository: EventRepository) {
    @Bean
    fun startupRedisMessage(): String {
      eventRepository.save(
        Event(
          "startup-message",
          """
      {
        "Type" : "Notification",
        "MessageId" : "startup-message",
        "TopicArn" : "arn:aws:sns:localhost:1111111:cloud-platform-Digital-Prison-Services-123456",
        "Message" : "{\"offenderId\":2500530271,\"crn\":\"X404348\",\"sourceId\":99198,\"eventDatetime\":\"2021-05-25T14:10:06\"}",
        "Timestamp" : "2021-05-25T13:10:16.494Z",
        "SignatureVersion" : "1",
        "MessageAttributes" : {
          "eventType" : {"Type":"String","Value":"OFFENDER_MANAGEMENT_TIER_CALCULATION_REQUIRED"},
          "source" : {"Type":"String","Value":"delius"},
          "id" : {"Type":"String","Value":"fc7197f8-b5a3-e677-d8da-52a4a4d307f7"},
          "contentType" : {"Type":"String","Value":"text/plain;charset=UTF-8"},
          "timestamp" : {"Type":"Number.java.lang.Long","Value":"1621948216486"}
        }
      }
          """.trimIndent()
        )
      )
      return "startupRedisMessage"
    }
  }

  @Test
  fun `Should consume and store an offender event message`() {
    assertThat(eventStore.getPageOfMessages(null, null, null, 1))
      .extracting<String>(DisplayMessage::eventType)
      .containsExactly("OFFENDER_MANAGEMENT_TIER_CALCULATION_REQUIRED")
  }
}
