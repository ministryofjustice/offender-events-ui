package uk.gov.justice.hmpps.offenderevents.service

import com.amazonaws.services.sqs.AmazonSQS
import org.assertj.core.api.Assertions.assertThat
import org.awaitility.kotlin.await
import org.awaitility.kotlin.matches
import org.awaitility.kotlin.untilCallTo
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.BeforeEach
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.SpyBean
import org.springframework.test.context.ActiveProfiles

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class ListenerIntegrationTest {

  @SpyBean
  @Qualifier("awsSqsClient")
  internal lateinit var awsSqsClient: AmazonSQS

  @Autowired
  internal lateinit var eventStore: OffenderEventStore

  @Autowired
  internal lateinit var queueUrl: String

  @BeforeEach
  fun `Wait for empty queue`() {
    await untilCallTo { getNumberOfMessagesCurrentlyOnQueue(awsSqsClient, queueUrl) } matches { it == 0 }
  }

  @Test
  fun `Should consume and store an offender event message`() {
    val message = "/messages/externalMovement.json".readResourceAsText()

    awsSqsClient.sendMessage(queueUrl, message)

    `Wait for empty queue`()

    assertThat(eventStore.getAllMessages()).extracting<EventType>(StoredMessage::eventType).containsExactly(EventType("EXTERNAL_MOVEMENT_RECORD-INSERTED"))

  }

  private fun getNumberOfMessagesCurrentlyOnQueue(awsSqsClient: AmazonSQS, queueUrl: String): Int? {
    val queueAttributes = awsSqsClient.getQueueAttributes(queueUrl, listOf("ApproximateNumberOfMessages"))
    return queueAttributes.attributes["ApproximateNumberOfMessages"]?.toInt()
  }

  private fun String.readResourceAsText(): String {
    return ListenerIntegrationTest::class.java.getResource(this).readText()
  }

}


