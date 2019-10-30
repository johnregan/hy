package io.explicitly.model

import org.scalatest.{ FreeSpec, Matchers }

class ConnectionTest extends FreeSpec with Matchers {

  "A Connection" - {
    "when created with relationships and the desired id of the connection" - {
      "should have the correct connection info " in {
        val relationships = Set(
          Relationship(HasConnection, Node("John"), Node("Peter")),
          Relationship(HasConnection, Node("John"), Node("George")),
          Relationship(HasConnection, Node("Peter"), Node("George")),
          Relationship(HasConnection, Node("Peter"), Node("Anna"))
        )
        Connection(relationships, Person("John")) shouldEqual Connection("John", Set(Node("Peter"), Node("George")))
      }
    }
  }
}
