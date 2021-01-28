package uk.gov.justice.hmpps.offenderevents

import net.javacrumbs.jsonunit.assertj.JsonAssertions.assertThatJson
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.annotation.DirtiesContext.ClassMode
import org.springframework.test.annotation.DirtiesContext.MethodMode.AFTER_METHOD
import org.springframework.test.context.ActiveProfiles
import java.net.URL

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@DirtiesContext(classMode = ClassMode.AFTER_CLASS)
class AppTest {

  @LocalServerPort
  val port: Int = 0

  @Test
  fun `The application starts`() {
    val infoResponse = URL("http://localhost:$port/info").readText()
    assertThatJson(infoResponse).node("build.name").isEqualTo("offender-events-ui")
  }
}