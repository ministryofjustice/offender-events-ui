package uk.gov.justice.hmpps.offenderevents.config

import nz.net.ultraq.thymeleaf.layoutdialect.LayoutDialect
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class ThymeleafConfig {
  @Bean
  fun layoutDialect() = LayoutDialect()
}
