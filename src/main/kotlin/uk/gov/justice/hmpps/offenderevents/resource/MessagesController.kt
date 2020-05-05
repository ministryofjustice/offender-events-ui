package uk.gov.justice.hmpps.offenderevents.resource

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.servlet.ModelAndView
import uk.gov.justice.hmpps.offenderevents.service.OffenderEventStore

@Controller
class MessagesController {

  @Autowired
  private lateinit var offenderEventStore: OffenderEventStore

  @GetMapping("/messages")
  fun messages(): ModelAndView {
    return ModelAndView("index", mutableMapOf("storedMessages" to offenderEventStore.getAllMessages()))
  }

}
