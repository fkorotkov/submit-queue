package org.cirruslabs.sq.github.model

import com.google.common.io.Resources
import org.junit.Assert.assertEquals
import org.junit.Test
import java.nio.charset.Charset

class PullRequestEventTest {
  @Test
  fun testParsing() {
    val resource = Resources.getResource("pull_request_labeled.json")
    val event = PullRequestEvent.parse(Resources.toString(resource, Charset.defaultCharset()))
    assertEquals("labeled", event.action)
    assertEquals(1, event.number)

    assertEquals(173526256, event.pull_request.id)
    assertEquals(1, event.pull_request.number)
    assertEquals("open", event.pull_request.state)

    assertEquals("queued", event.label?.name)

    assertEquals("cirruslabs/sandbox", event.repository.full_name)

    assertEquals(102236, event.installation.id)
  }
}
