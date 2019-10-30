package io.explicitly.service

import cats.effect.IO
import io.explicitly.client.ThirdPartyApiAlgebra
import io.explicitly.model.{ ConnectedCount, Facebook, Person, SocialNetwork, Twitter }

class SocialMetricService(alg: ThirdPartyApiAlgebra[IO], connectionCounterAlgebra: ConnectionCounterAlgebra) {

  def nonConnectedPeople(sn: SocialNetwork): IO[Int] =
    alg
      .retrieveRelationships(sn)
      .map { rs =>
        rs.people.map(p => connectionCounterAlgebra.count(rs.people, rs.relationships.toSet, p))
      }
      .flatMap(
        countInfo =>
          IO {
            countInfo.count {
              case ConnectedCount(firstDegree, secondDegree) =>
                firstDegree == 0 && secondDegree == 0
            }
          }
      )

  def countsForUser(person: Person): IO[ConnectedCount] =
    for {
      rsF                             <- alg.retrieveRelationships(Facebook)
      rsT                             <- alg.retrieveRelationships(Twitter)
      ConnectedCount(firstF, secondF) = connectionCounterAlgebra.count(rsF.people, rsF.relationships.toSet, person)
      ConnectedCount(firstT, secondT) = connectionCounterAlgebra.count(rsT.people, rsT.relationships.toSet, person)
    } yield {
      ConnectedCount(firstF + firstT, secondF + secondT)
    }
}
