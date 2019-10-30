package io.explicitly.client

import io.explicitly.model.{ SocialNetwork, ThirdPartyResponse }

trait ThirdPartyApiAlgebra[F[_]] {

  def retrieveRelationships(sn: SocialNetwork): F[ThirdPartyResponse]

}
