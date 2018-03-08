package org.cirruslabs.sq.github.model

import com.google.gson.Gson

class PullRequestEvent {
  companion object {
    private val gson = Gson()
    fun parse(text: String): PullRequestEvent = gson.fromJson(text, PullRequestEvent::class.java)
  }

  var action: String = ""
  var number: Long = 0
  var pull_request: PulleRequest = PulleRequest()
  var label: Label? = null
  var repository: Repository = Repository()
  var installation: Installation = Installation()
}
