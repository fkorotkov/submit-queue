package org.cirruslabs.sq.github.api

import com.google.common.net.HttpHeaders
import com.google.gson.Gson
import org.apache.commons.io.IOUtils
import org.apache.http.client.HttpClient
import org.apache.http.client.methods.HttpGet
import org.apache.http.client.methods.HttpPost
import org.apache.http.client.utils.URIBuilder
import org.apache.http.impl.client.HttpClientBuilder
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager
import org.cirruslabs.sq.github.api.responses.AccessTokenResponse
import org.cirruslabs.sq.github.api.responses.PermissionResponse
import org.cirruslabs.sq.github.model.Repository
import java.io.InputStreamReader
import java.nio.charset.Charset
import java.time.Duration
import java.util.*

class GitHubApi(
  val appId: String,
  val secrets: GithubSecrets
) {
  val httpClient: HttpClient by lazy {
    HttpClientBuilder.create()
      .setConnectionManager(PoolingHttpClientConnectionManager())
      .build()
  }

  companion object {
    fun constructUrl(host: String, filePart: String, params: Map<String, String> = emptyMap()): String {
      val uriBuilder = URIBuilder()
      uriBuilder.scheme = "https"
      uriBuilder.host = host
      uriBuilder.path = filePart
      params.forEach { name, value ->
        uriBuilder.addParameter(name, value)
      }
      return uriBuilder.build().toString()
    }
  }

  private suspend fun acquireAccessToken(repo: Repository, installationId: Long): String {
    val jwt = secrets.signJWT(appId)
    val request = apiRequestPost("api.github.com", "/installations/$installationId/access_tokens")
    request.addHeader(HttpHeaders.AUTHORIZATION, "Bearer $jwt")
    request.addHeader(HttpHeaders.ACCEPT, "application/vnd.github.machine-man-preview+json")
    println("Acquiring access token for ${repo.full_name}...")
    try {
      val response = httpClient.execute(request)
      println("Acquired GitHub token for ${repo.full_name} with ${response.statusLine.statusCode}:${response.statusLine.reasonPhrase} response.")
      if (response.statusLine.statusCode in 400..499) {
        throw IllegalStateException("Failed to acquire an access token for ${repo.full_name}: ${response.statusLine.statusCode}:${response.statusLine.reasonPhrase}")
      }
      if (response.statusLine.statusCode != 201) {
        throw IllegalStateException("Failed to acquire an access token for ${repo.full_name}: ${response.statusLine.statusCode}:${response.statusLine.reasonPhrase}")
      }
      val accessTokenResponse = Gson().fromJson(
        InputStreamReader(response.entity.content),
        AccessTokenResponse::class.java
      ) ?: throw IllegalStateException("Failed to acquire an access token for ${repo.full_name}!")

      val expiresIn = Duration.between(
        Date().toInstant(),
        accessTokenResponse.expires_at.toInstant()
      )

      println("Got a token for ${repo.full_name} that expires in ${expiresIn.toMinutes()} minutes.")
      return accessTokenResponse.token
    } finally {
      request.releaseConnection()
    }
  }

  private fun apiRequestGet(host: String, path: String, params: Map<String, String> = emptyMap()): HttpGet {
    return HttpGet(constructUrl(host, path, params))
  }

  private fun apiRequestPost(host: String, path: String, params: Map<String, String> = emptyMap()): HttpPost {
    return HttpPost(constructUrl(host, path, params))
  }

  suspend fun userPermission(repo: Repository, installationId: Long, username: String): String {
    val request = apiRequestGet("api.github.com", "/repos/${repo.full_name}/collaborators/$username/permission")
    request.addHeader(HttpHeaders.ACCEPT, "application/vnd.github.hellcat-preview+json")
    acquireAccessToken(repo, installationId).let {
      request.addHeader(HttpHeaders.AUTHORIZATION, "token $it")
    }

    return try {
      val response = httpClient.execute(request)
      if (response.statusLine.statusCode != 200) {
        val content = IOUtils.toString(response.entity.content, Charset.defaultCharset())
        System.err.println("Failed to get permissions for user $username: ${response.statusLine}\n$content")
        throw IllegalStateException("Failed to get permissions for user $username for ${repo.full_name} repo!")
      }
      val permissionResponse = Gson().fromJson(
        InputStreamReader(response.entity.content),
        PermissionResponse::class.java
      )
      println("User $username has ${permissionResponse.permission} permissions for ${repo.full_name} repo!")
      permissionResponse.permission
    } finally {
      request.releaseConnection()
    }
  }
}
