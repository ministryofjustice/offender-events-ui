package uk.gov.justice.hmpps.offenderevents.service

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import uk.gov.justice.hmpps.offenderevents.resource.DisplayMessage

class RedisStartupTest : IntegrationTest() {
  @Test
  fun `Should consume and store an offender event message`() {
    assertThat(eventStore.getPageOfMessages(null, null, null, null, null, 1))
      .extracting<String>(DisplayMessage::eventType)
      .containsExactly("OFFENDER_MANAGEMENT_TIER_CALCULATION_REQUIRED")
  }
}
