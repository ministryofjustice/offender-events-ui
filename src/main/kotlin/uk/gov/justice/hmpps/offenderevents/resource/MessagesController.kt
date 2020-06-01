package uk.gov.justice.hmpps.offenderevents.resource

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.servlet.ModelAndView
import uk.gov.justice.hmpps.offenderevents.service.OffenderEventStore

data class DisplayMessage(val eventType: String, val messageDetails: Map<String, String>)

@Controller
class MessagesController(@Value("\${ui.pageSize}") val pageSize: Int) {

  @Autowired
  private lateinit var offenderEventStore: OffenderEventStore

  @GetMapping("/messages")
  fun messages(
      @RequestParam(name = "include-event-type-filter", required = false) includeEventTypeFilter: List<String>?,
      @RequestParam(name = "exclude-event-type-filter", required = false) excludeEventTypeFilter: List<String>?,
      @RequestParam(name = "text-filter", required = false) textFilter: String?) =
      ModelAndView("index",
          mutableMapOf(
              "displayMessages" to offenderEventStore.getPageOfMessages(includeEventTypeFilter, excludeEventTypeFilter, textFilter, pageSize).toList(),
              "allEventTypes" to offenderEventStore.getAllEventTypes(),
              "includeEventTypeFilter" to includeEventTypeFilter,
              "excludeEventTypeFilter" to excludeEventTypeFilter,
              "textFilter" to textFilter
          )
      )
}
