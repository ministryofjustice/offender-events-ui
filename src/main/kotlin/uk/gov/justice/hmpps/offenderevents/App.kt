package uk.gov.justice.hmpps.offenderevents

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory
import org.springframework.data.redis.core.RedisKeyValueAdapter.EnableKeyspaceEvents
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories
import org.springframework.data.redis.serializer.StringRedisSerializer
import uk.gov.justice.hmpps.offenderevents.data.Event

@SpringBootApplication
class Application

fun main(args: Array<String>) {
  runApplication<Application>(*args)
}

// we want keyspace notifications, but have to empty the config parameter (default Ex) since elasticache doesn't support
// changing the config.  If we move off elasticache then need to remove the config parameter and let it use the default.
@Configuration
@EnableRedisRepositories(
  enableKeyspaceEvents = EnableKeyspaceEvents.ON_STARTUP,
  keyspaceNotificationsConfigParameter = "\${application.keyspace-notifications:}",
)
class AppConfig {
  @Bean
  fun setupTemplate(conn: LettuceConnectionFactory): RedisTemplate<Event, String> {
    val template = RedisTemplate<Event, String>()
    template.connectionFactory = conn
    template.stringSerializer = StringRedisSerializer()
    return template
  }
}
