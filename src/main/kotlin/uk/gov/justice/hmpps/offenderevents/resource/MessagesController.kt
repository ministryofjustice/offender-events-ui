package uk.gov.justice.hmpps.offenderevents.resource

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.servlet.ModelAndView
import uk.gov.justice.hmpps.offenderevents.service.OffenderEventStore

data class DisplayMessage(val eventType: String, val messageDetails: Map<String, String?> = mapOf(), val topic: String)

@Controller
class MessagesController(@Value("\${ui.pageSize}") val pageSize: Int) {

  @Autowired
  private lateinit var offenderEventStore: OffenderEventStore

  @GetMapping("/messages")
  fun messages(
    @RequestParam(name = "include-event-type-filter", required = false) includeEventTypeFilter: List<String>?,
    @RequestParam(name = "exclude-event-type-filter", required = false) excludeEventTypeFilter: List<String>?,
    @RequestParam(name = "include-topic-filter", required = false) includeTopicFilter: List<String>?,
    @RequestParam(name = "exclude-topic-filter", required = false) excludeTopicFilter: List<String>?,
    @RequestParam(name = "text-filter", required = false) textFilter: String?
  ): ModelAndView = ModelAndView(
    "index",
    mutableMapOf(
      "displayMessages" to offenderEventStore.getPageOfMessages(
        includeEventTypeFilter,
        excludeEventTypeFilter,
        includeTopicFilter,
        excludeTopicFilter,
        textFilter,
        pageSize
      ).toList(),
      "allEventTypes" to offenderEventStore.getAllEventTypes(),
      "allTopics" to offenderEventStore.getAllTopics(),
      "includeEventTypeFilter" to includeEventTypeFilter,
      "excludeEventTypeFilter" to excludeEventTypeFilter,
      "includeTopicFilter" to includeTopicFilter,
      "excludeTopicFilter" to excludeTopicFilter,
      "textFilter" to textFilter
    )
  )
}
