package org.cirruslabs.sq.github.api

interface GitHubApi {
  suspend fun userPermission(repositoryFullName: String, installationId: Long, username: String): String
}
