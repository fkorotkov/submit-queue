package org.cirruslabs.sq.github.model

import com.google.common.io.Resources
import org.junit.Assert.assertEquals
import org.junit.Test
import java.nio.charset.Charset

class IssueCommentEventTest {
  @Test
  fun testParsing() {
    val resource = Resources.getResource("issue_comment_created.json")
    val event = IssueCommentEvent.parse(Resources.toString(resource, Charset.defaultCharset()))
    assertEquals("created", event.action)

    assertEquals(303196639, event.issue.id)
    assertEquals(1, event.issue.number)

    assertEquals(371223771, event.comment.id)
    assertEquals("/submit", event.comment.body)
    assertEquals("CONTRIBUTOR", event.comment.author_association)

    assertEquals(124271577, event.repository.id)
    assertEquals("cirruslabs/sandbox", event.repository.full_name)
    assertEquals("master", event.repository.default_branch)
  }
}
