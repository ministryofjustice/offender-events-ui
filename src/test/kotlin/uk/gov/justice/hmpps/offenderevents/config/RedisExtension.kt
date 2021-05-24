package uk.gov.justice.hmpps.offenderevents.config

import org.junit.jupiter.api.extension.AfterAllCallback
import org.junit.jupiter.api.extension.BeforeAllCallback
import org.junit.jupiter.api.extension.ExtensionContext
import redis.embedded.RedisServer

class RedisExtension : BeforeAllCallback, AfterAllCallback {
  companion object {
    @JvmField
    val redisServer: RedisServer = RedisServer(6380)
  }

  override fun beforeAll(context: ExtensionContext) {
    redisServer.start()
  }

  override fun afterAll(context: ExtensionContext) {
    redisServer.stop()
  }
}
