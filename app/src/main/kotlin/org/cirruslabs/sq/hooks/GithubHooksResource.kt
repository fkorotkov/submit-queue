package org.cirruslabs.sq.hooks

import kotlinx.coroutines.experimental.runBlocking
import org.apache.commons.io.IOUtils
import org.cirruslabs.sq.github.api.GitHubApi
import org.cirruslabs.sq.github.model.IssueCommentEvent
import org.cirruslabs.sq.hooks.handlers.IssueCommentEventHandler
import java.nio.charset.Charset
import javax.servlet.http.HttpServletRequest
import javax.ws.rs.POST
import javax.ws.rs.Path
import javax.ws.rs.core.Context


@Path("/hooks")
class GithubHooksResource(gitHubApi: GitHubApi) {
  private val issueCommentEventHandler = IssueCommentEventHandler(gitHubApi)

  @POST
  @Path("github")
  fun push(@Context req: HttpServletRequest): String {
    val event = req.getHeader("X-GitHub-Event").toLowerCase()
    val delivery = req.getHeader("X-GitHub-Delivery").toLowerCase()
    val signature = req.getHeader("X-Hub-Signature")?.toLowerCase()
    val content = IOUtils.toString(req.inputStream, Charset.defaultCharset())
    return runBlocking {
      when (event) {
        "issue_comment" ->{
          issueCommentEventHandler.handle(IssueCommentEvent.parse(content))
        }
        else -> "Unsupported"
      }
    }
  }
}
