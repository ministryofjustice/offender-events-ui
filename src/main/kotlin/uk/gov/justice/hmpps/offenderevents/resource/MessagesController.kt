package uk.gov.justice.hmpps.offenderevents.resource

import com.google.common.reflect.TypeToken
import com.microsoft.applicationinsights.core.dependencies.google.gson.Gson
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.servlet.ModelAndView
import uk.gov.justice.hmpps.offenderevents.service.OffenderEventStore
import uk.gov.justice.hmpps.offenderevents.service.StoredMessage

data class DisplayMessage(val eventType: String, val messageDetails: Map<String, String>)

@Controller
class MessagesController {

  companion object {
    inline fun <reified T> fromJson(json: String): T {
      return Gson().fromJson(json, object: TypeToken<T>(){}.type)
    }
  }

  @Autowired
  private lateinit var offenderEventStore: OffenderEventStore

  @GetMapping("/messages")
  fun messages(): ModelAndView {
    return ModelAndView("index", mutableMapOf("displayMessages" to offenderEventStore.getAllMessages().map(::transformMessage).toList()))
  }

  fun transformMessage(storedMessage: StoredMessage): DisplayMessage {
    val keyValuePairs = fromJson<MutableMap<String, String>>(storedMessage.message.Message)
    keyValuePairs.remove("eventType")
    return DisplayMessage(storedMessage.eventType.Value, keyValuePairs.toMap())
  }

}
