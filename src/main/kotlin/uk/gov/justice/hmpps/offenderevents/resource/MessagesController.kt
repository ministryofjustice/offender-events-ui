package uk.gov.justice.hmpps.offenderevents.resource

import com.google.common.reflect.TypeToken
import com.microsoft.applicationinsights.core.dependencies.google.gson.Gson
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.servlet.ModelAndView
import uk.gov.justice.hmpps.offenderevents.service.OffenderEventStore
import uk.gov.justice.hmpps.offenderevents.service.StoredMessage

data class DisplayMessage(val eventType: String, val messageDetails: Map<String, String>)

@Controller
class MessagesController(@Value("\${ui.pageSize}") val pageSize: Int) {

  companion object {
    inline fun <reified T> fromJson(json: String): T {
      return Gson().fromJson(json, object : TypeToken<T>() {}.type)
    }
  }

  @Autowired
  private lateinit var offenderEventStore: OffenderEventStore

  @GetMapping("/messages")
  fun messages(
      @RequestParam(name = "event-type-filter", required = false) eventTypeFilter: String?,
      @RequestParam(name = "text-filter", required = false) textFilter: String?) =
      ModelAndView("index",
          mutableMapOf(
              "displayMessages" to offenderEventStore.getPageOfMessages(eventTypeFilter, textFilter, pageSize).map(::transformMessage).toList(),
              "allEventTypes" to offenderEventStore.getAllEventTypes(),
              "eventTypeFilter" to eventTypeFilter,
              "textFilter" to textFilter
          )
      )

  fun transformMessage(storedMessage: StoredMessage) =
      fromJson<MutableMap<String, String>>(storedMessage.message.Message)
          .also { it.remove("eventType") }
          .let { DisplayMessage(storedMessage.eventType.Value, it.toMap()) }

}
