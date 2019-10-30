package io.explicitly.model

sealed trait RelationshipType
case object HasConnection extends RelationshipType
