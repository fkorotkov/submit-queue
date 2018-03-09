package org.cirruslabs.sq.github.model

class Comment {
  var id: Long = 0
  var author_association: String = ""
  var body: String = ""
  var user: User = User()
}
