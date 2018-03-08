package org.cirruslabs.sq.github.model

import com.google.gson.Gson

class IssueCommentEvent {
  companion object {
    private val gson = Gson()
    fun parse(text: String): IssueCommentEvent = gson.fromJson(text, IssueCommentEvent::class.java)
  }

  var action: String = ""
  var issue: Issue = Issue()
  var comment: Comment = Comment()
  var repository: Repository = Repository()
  var installation: Installation = Installation()
}
