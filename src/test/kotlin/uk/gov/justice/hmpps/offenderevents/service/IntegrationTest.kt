package uk.gov.justice.hmpps.offenderevents.service

import com.amazonaws.services.sqs.AmazonSQS
import org.awaitility.kotlin.await
import org.awaitility.kotlin.matches
import org.awaitility.kotlin.untilCallTo
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.boot.test.mock.mockito.SpyBean
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Import
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.TestPropertySource
import uk.gov.justice.hmpps.offenderevents.config.RedisExtension
import uk.gov.justice.hmpps.offenderevents.data.Event
import uk.gov.justice.hmpps.offenderevents.data.EventRepository

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@TestPropertySource(properties = ["ui.pageSize=2"])
@ExtendWith(RedisExtension::class)
@Import(IntegrationTest.TestConfig::class)
class IntegrationTest {
  @SpyBean
  @Qualifier("awsSqsClient")
  internal lateinit var awsSqsClient: AmazonSQS

  @Autowired
  internal lateinit var queueUrl: String

  @Autowired
  internal lateinit var eventStore: OffenderEventStore

  @Autowired
  internal lateinit var eventRepository: EventRepository

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

  @BeforeEach
  fun `Wait for empty queue`() {
    await untilCallTo { getNumberOfMessagesCurrentlyOnQueue(awsSqsClient, queueUrl) } matches { it == 0 }
  }

  @AfterEach
  fun `Clear message store`() {
    eventStore.retainAll(listOf(eventStore.get(0)))
    eventRepository.deleteAll()
  }

  internal fun getNumberOfMessagesCurrentlyOnQueue(awsSqsClient: AmazonSQS, queueUrl: String): Int? {
    val queueAttributes = awsSqsClient.getQueueAttributes(queueUrl, listOf("ApproximateNumberOfMessages"))
    return queueAttributes.attributes["ApproximateNumberOfMessages"]?.toInt()
  }

  internal fun String.readResourceAsText(): String {
    return ListenerIntegrationTest::class.java.getResource(this).readText()
  }
}
