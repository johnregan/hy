package io.explicitly.service

import io.explicitly.model.{ ConnectedCount, HasConnection, Node, Person, Relationship }
import org.scalatest.{ FreeSpec, Matchers }
import org.scalatest.prop.TableDrivenPropertyChecks._

class ConnectionCounterTest extends FreeSpec with Matchers {
  "A Connection Counter" - {
    "When given a large resultset" - {
      "should return the correct first and second degree counts for a user" in {
        val people = List(Person("John"), Person("Harry"), Person("Peter"), Person("George"), Person("Anna"))
        val relationships = Set(
          Relationship(HasConnection, Node("John"), Node("Peter")),
          Relationship(HasConnection, Node("John"), Node("George")),
          Relationship(HasConnection, Node("Peter"), Node("George")),
          Relationship(HasConnection, Node("Peter"), Node("Anna"))
        )

        val fractions =
          Table(
            ("name", "expectedFirstDegree", "expectedSecondDegree"),
            ("John", 2, 1),
            ("Peter", 3, 0),
            ("George", 2, 1),
            ("Harry", 0, 0),
            ("Anna", 1, 2)
          )

        forAll(fractions) { (name: String, expectedFirstDegree: Int, expectedSecondDegree: Int) =>
          ConnectionCounter.count(people, relationships, Person(name)) shouldEqual ConnectedCount(
            expectedFirstDegree,
            expectedSecondDegree
          )
        }
      }

      "should return the correct first and second degree counts for a user with minimal data" in {
        ConnectionCounter.count(List.empty, Set.empty, Person("JJ")) shouldEqual ConnectedCount(0, 0)
      }

      "should return the correct first and second degree counts for a user with only people data" in {
        val people = List(Person("John"))
        ConnectionCounter.count(people, Set.empty, Person("John")) shouldEqual ConnectedCount(0, 0)
      }

      "should return the correct first and second degree counts for a user with only relationship data" in {
        val relationships = Set(
          Relationship(HasConnection, Node("John"), Node("Peter")),
          Relationship(HasConnection, Node("John"), Node("George")),
          Relationship(HasConnection, Node("Peter"), Node("George")),
          Relationship(HasConnection, Node("Peter"), Node("Anna"))
        )
        ConnectionCounter.count(List.empty, relationships, Person("John")) shouldEqual ConnectedCount(0, 0)
      }
    }
  }
}
