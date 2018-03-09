package org.cirruslabs.sq.github.model

import com.google.common.io.Resources
import org.junit.Assert.assertEquals
import org.junit.Test
import java.nio.charset.Charset
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class IssueCommentEventTest {
  @Test
  fun testParsingWithPr() {
    val resource = Resources.getResource("issue_comment_created_with_pr.json")
    val event = IssueCommentEvent.parse(Resources.toString(resource, Charset.defaultCharset()))
    assertEquals("created", event.action)

    assertEquals(303196639, event.issue.id)
    assertEquals(1, event.issue.number)
    assertTrue(event.issue.isPR)

    assertEquals(371223771, event.comment.id)
    assertEquals("/submit", event.comment.body)
    assertEquals("CONTRIBUTOR", event.comment.author_association)

    assertEquals(124271577, event.repository.id)
    assertEquals("cirruslabs/sandbox", event.repository.full_name)
    assertEquals("master", event.repository.default_branch)

    assertEquals(102236, event.installation.id)
  }
  @Test
  fun testParsingNoPr() {
    val resource = Resources.getResource("issue_comment_created_no_pr.json")
    val event = IssueCommentEvent.parse(Resources.toString(resource, Charset.defaultCharset()))
    assertEquals("created", event.action)

    assertEquals(303837886, event.issue.id)
    assertEquals(2, event.issue.number)
    assertFalse(event.issue.isPR)

    assertEquals(371809453, event.comment.id)
    assertEquals("test /submit", event.comment.body)
    assertEquals("CONTRIBUTOR", event.comment.author_association)

    assertEquals(124271577, event.repository.id)
    assertEquals("cirruslabs/sandbox", event.repository.full_name)
    assertEquals("master", event.repository.default_branch)

    assertEquals(102236, event.installation.id)
  }
}
