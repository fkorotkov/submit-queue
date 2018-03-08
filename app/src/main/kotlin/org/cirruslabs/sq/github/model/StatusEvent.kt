package org.cirruslabs.sq.github.model

import com.google.gson.Gson

class StatusEvent {
  companion object {
    private val gson = Gson()
    fun parse(text: String): StatusEvent = gson.fromJson(text, StatusEvent::class.java)
  }

  var id: Long = 0
  var sha: String = ""
  var name: String = ""
  var context: String = ""
  var state: String = ""
  var repository: Repository = Repository()
  var installation: Installation = Installation()
}
