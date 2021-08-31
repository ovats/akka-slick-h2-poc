package com.cc.domain

import com.cc.domain.request.ProductRequest

import java.time.LocalDate

case class Product(
    id: Option[ProductId],
    name: String,
    vendor: String,
    price: BigDecimal,
    expirationDate: Option[LocalDate],
) {

  //TODO not sure if this code is fine here
  def toProductView: ProductRequest = {
    ProductRequest(name = this.name, vendor = this.vendor, price = this.price, expirationDate = this.expirationDate)
  }

}
