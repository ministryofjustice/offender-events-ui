package uk.gov.justice.hmpps.offenderevents.service

import com.amazonaws.services.sqs.AmazonSQS
import org.awaitility.kotlin.await
import org.awaitility.kotlin.matches
import org.awaitility.kotlin.untilCallTo
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.SpyBean
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.TestPropertySource

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@TestPropertySource(properties = ["ui.pageSize=2"])
class IntegrationTest {

  companion object {
    val log: Logger = LoggerFactory.getLogger(this::class.java)
  }

  @SpyBean
  @Qualifier("awsSqsClient")
  internal lateinit var awsSqsClient: AmazonSQS

  @Autowired
  internal lateinit var queueUrl: String

  @Autowired
  internal lateinit var eventStore: OffenderEventStore

  @BeforeEach
  fun `Wait for messages to be processed`() {
    await untilCallTo { getNumberOfActiveMessages(awsSqsClient, queueUrl) } matches { it == 0 }
  }

  fun `Wait for event store to contain messages`(noMessages: Int) {
    await untilCallTo { eventStore.size } matches { it == noMessages }
  }

  @AfterEach
  fun `Clear message store`() {
    log.info("Store has ${eventStore.size} messages before clear. Store id=${eventStore.hashCode()}")
    eventStore.clear()
    log.info("Store has ${eventStore.size} messages before clear. Store id=${eventStore.hashCode()}")
  }

  internal fun getNumberOfActiveMessages(awsSqsClient: AmazonSQS, queueUrl: String): Int {
    val queueAttributes = awsSqsClient.getQueueAttributes(queueUrl, listOf("ApproximateNumberOfMessages", "ApproximateNumberOfMessagesNotVisible"))
    return queueAttributes.attributes["ApproximateNumberOfMessages"]?.toInt() ?: 0
      .plus(queueAttributes.attributes["ApproximateNumberOfMessagesNotVisible"]?.toInt() ?: 0)
  }

  internal fun String.readResourceAsText(): String {
    return ListenerIntegrationTest::class.java.getResource(this).readText()
  }
}
