package org.cirruslabs.sq.github.model

import java.util.*

class PulleRequest {
  var id: Int = 0
  var number: Int = 0
  var state: String = ""
  var body: String = ""
  var created_at: Date = Date()
  var closed_at: Date? = null
  var merged_at: Date? = null
  var merge_commit_sha: String = ""
  var labels: List<Label> = emptyList()
  var head: RefInfo = RefInfo()
  var base: RefInfo = RefInfo()
}
