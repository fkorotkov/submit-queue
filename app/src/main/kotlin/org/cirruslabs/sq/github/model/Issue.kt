package org.cirruslabs.sq.github.model

class Issue {
  var id: Long = 0
  var number: Long = 0
  var labels: List<String> = emptyList()
  var pull_request: Map<String, String>? = null

  val isPR: Boolean
    get() = pull_request != null
}
