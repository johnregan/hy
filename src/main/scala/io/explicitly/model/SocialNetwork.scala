package io.explicitly.model

sealed trait SocialNetwork
case object Facebook extends SocialNetwork
case object Twitter  extends SocialNetwork
