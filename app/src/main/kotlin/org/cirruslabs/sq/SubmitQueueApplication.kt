package org.cirruslabs.sq

import io.dropwizard.Application
import io.dropwizard.setup.Environment
import org.cirruslabs.sq.config.SubmitQueueConfig
import org.cirruslabs.sq.github.api.GitHubApi
import org.cirruslabs.sq.github.api.GithubSecrets
import org.cirruslabs.sq.github.api.impl.GitHubApiImpl
import org.cirruslabs.sq.hooks.GithubHooksResource
import org.eclipse.jetty.servlets.CrossOriginFilter
import java.util.*
import javax.servlet.DispatcherType

fun main(vararg args: String) {
  SubmitQueueApplication().run(*args)
}

class SubmitQueueApplication : Application<SubmitQueueConfig>() {
  override fun run(configuration: SubmitQueueConfig, environment: Environment) {
    enableCORS(environment)

    val gitHubApi = GitHubApiImpl(
      System.getenv("GITHUB_CLIENT_ID_FILE"),
      GithubSecrets.initialize()
    )

    environment.jersey().apply {
      register(GithubHooksResource(gitHubApi))
    }
  }

  private fun enableCORS(environment: Environment) {
    // to allow requests from localhost
    val cors = environment.servlets().addFilter("CORS", CrossOriginFilter::class.java)
    cors.setInitParameter(CrossOriginFilter.ALLOWED_ORIGINS_PARAM, "*")
    cors.setInitParameter(CrossOriginFilter.ALLOWED_HEADERS_PARAM, "*")
    cors.addMappingForUrlPatterns(EnumSet.allOf(DispatcherType::class.java), true, "/*")
  }
}
