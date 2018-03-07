package org.cirruslabs.sq.hooks

import org.apache.commons.io.IOUtils
import java.nio.charset.Charset
import javax.servlet.http.HttpServletRequest
import javax.ws.rs.POST
import javax.ws.rs.Path
import javax.ws.rs.core.Context


@Path("/hooks")
class GithubHooksResource {
  @POST
  @Path("github")
  fun push(@Context req: HttpServletRequest): String {
    val event = req.getHeader("X-GitHub-Event").toLowerCase()
    val delivery = req.getHeader("X-GitHub-Delivery").toLowerCase()
    val signature = req.getHeader("X-Hub-Signature")?.toLowerCase()
    val content = IOUtils.toString(req.inputStream, Charset.defaultCharset())
    return "ok"
  }
}
