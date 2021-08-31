package com.cc.domain.request

import io.circe.Codec
import io.circe.generic.semiauto.deriveCodec

import java.time.LocalDate

final case class ProductRequest(name: String, vendor: String, price: BigDecimal, expirationDate: Option[LocalDate])

object ProductRequest {
  implicit val productRequestCodec: Codec[ProductRequest] = deriveCodec[ProductRequest]
}
