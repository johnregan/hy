package io.explicitly.service

import io.explicitly.model._

object ConnectionCounter extends ConnectionCounterAlgebra {

  def count(people: List[Person], relations: Set[Relationship], person: Person): ConnectedCount = {
    val gns: List[Connection]                   = people.map(p => Connection(relations, p))
    val nodeConnections: Map[String, Set[Node]] = gns.map(gn => (gn.id, gn.connections)).toMap
    val firstDegreeConnections: Set[Node]       = nodeConnections.getOrElse(person.name, Set.empty)
    val connectionGroups: Set[Node] =
      firstDegreeConnections.flatMap(fdc => nodeConnections.getOrElse(fdc.id, Nil).filterNot(_.id == person.name))
    val secondDegreeConnections = connectionGroups -- firstDegreeConnections

    ConnectedCount(firstDegreeConnections.size, secondDegreeConnections.size)
  }
}
