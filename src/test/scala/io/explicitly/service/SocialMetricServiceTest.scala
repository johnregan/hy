package io.explicitly.service

import cats.effect.IO
import io.explicitly.client.ThirdPartyApiAlgebra
import io.explicitly.model.{
  ConnectedCount,
  Facebook,
  HasConnection,
  Node,
  Person,
  Relationship,
  SocialNetwork,
  ThirdPartyResponse
}
import org.scalatest.{ FreeSpec, Matchers }

class SocialMetricServiceTest extends FreeSpec with Matchers {

  private def sut(firstDegreeConnections: Int, secondDegreeConnections: Int): SocialMetricService = {
    val tpa = new ThirdPartyApiAlgebra[IO] {
      override def retrieveRelationships(sn: SocialNetwork): IO[ThirdPartyResponse] =
        IO(
          ThirdPartyResponse(
            Facebook,
            List(Person("John"), Person("Harry"), Person("Peter"), Person("George"), Person("Anna")),
            List(
              Relationship(HasConnection, Node("John"), Node("Peter")),
              Relationship(HasConnection, Node("John"), Node("George")),
              Relationship(HasConnection, Node("Peter"), Node("George")),
              Relationship(HasConnection, Node("Peter"), Node("Anna"))
            )
          )
        )
    }
    val cca = new ConnectionCounterAlgebra {
      override def count(people: List[Person], relations: Set[Relationship], person: Person): ConnectedCount =
        ConnectedCount(firstDegreeConnections, secondDegreeConnections)
    }

    new SocialMetricService(tpa, cca)
  }

  "A Social Metric Service" - {
    "When called to fetch count for non connected users" - {
      "should have size 0 if all users are first degree connected" in {
        sut(1, 0).nonConnectedPeople(Facebook).unsafeRunSync() shouldEqual 0
      }

      "should have size 0 if all users are second degree connected" in {
        sut(0, 1).nonConnectedPeople(Facebook).unsafeRunSync() shouldEqual 0
      }

      "should have size 0 if all users are first and second degree connected" in {
        sut(1, 1).nonConnectedPeople(Facebook).unsafeRunSync() shouldEqual 0
      }

      "should have size 5 if all users are not connected" in {
        sut(0, 0).nonConnectedPeople(Facebook).unsafeRunSync() shouldEqual 5
      }

      "should return aggregated counts for a user" in {
        sut(2, 2).countsForUser(Person("JJ")).unsafeRunSync() shouldEqual ConnectedCount(4, 4)
      }
    }
  }
}
