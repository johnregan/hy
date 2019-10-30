package io.explicitly.service

import io.explicitly.model.{ ConnectedCount, Person, Relationship }

trait ConnectionCounterAlgebra {

  def count(people: List[Person], relations: Set[Relationship], person: Person): ConnectedCount
}
