package uk.gov.justice.hmpps.offenderevents.data

import org.springframework.data.annotation.Id
import org.springframework.data.redis.core.RedisHash
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface EventRepository : CrudRepository<Event, String>

@RedisHash(value = "event", timeToLive = 86400) // expire all tokens after a day
data class Event(
  @Id
  val messageId: String,
  val wholeMessage: String,
)
