package io.explicitly.model

case class Connection(id: String, connections: Set[Node])

object Connection {
  def apply(relationships: Set[Relationship], person: Person): Connection = {
    val startNodeRelations: Set[Node] = relationships.filter(_.startNode.id == person.name).map(_.endNode)
    val endNodeRelations: Set[Node]   = relationships.filter(_.endNode.id == person.name).map(_.startNode)

    Connection(person.name, startNodeRelations ++ endNodeRelations)
  }
}
