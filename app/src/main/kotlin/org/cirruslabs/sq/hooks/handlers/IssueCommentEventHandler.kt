package org.cirruslabs.sq.hooks.handlers

import org.cirruslabs.sq.github.api.GitHubApi
import org.cirruslabs.sq.github.model.IssueCommentEvent

class IssueCommentEventHandler(
  val gitHubApi: GitHubApi
) {
  suspend fun handle(event: IssueCommentEvent): String {
    val submitComment = event.action == "created" && event.comment.body.contains("/submit")
    if (!submitComment) {
      return "Noop"
    }
    val userPermission = gitHubApi.userPermission(event.repository.full_name, event.installation.id, event.comment.user.login)
    if (userPermission != "admin" && userPermission != "write") {
      // todo: comment that permissions are not sophisticated?
      return "No permissions!"
    }
    return "TODO"
  }
}
