package org.cirruslabs.sq.github.model

import com.google.common.io.Resources
import org.junit.Assert.*
import org.junit.Test
import java.nio.charset.Charset

class StatusEventTest {
  @Test
  fun testParsing() {
    val resource = Resources.getResource("status.json")
    val event = StatusEvent.parse(Resources.toString(resource, Charset.defaultCharset()))
    assertEquals(4689303527, event.id)
    assertEquals("e1d6f86e4ee55806475b87412e737c4f6a05f7ee", event.sha)
    assertEquals("cirruslabs/submit-queue", event.name)
    assertEquals("check", event.context)
    assertEquals("success", event.state)

    assertEquals("cirruslabs/submit-queue", event.repository.full_name)

    assertEquals(102236, event.installation.id)
  }
}
