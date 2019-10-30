package io.explicitly.server

import io.explicitly.model.ConnectedCount

trait RestServerAlgebra[F[_]] {

  def getNonConnectedPersonCount: F[Int]

  def getConnectedCount: F[ConnectedCount]
}
