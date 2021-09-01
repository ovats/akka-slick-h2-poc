package com.cc.view

import com.cc.domain.Product

trait RestView {

  implicit class ProductViewConverter(product: Product) {
    def toRestView: ProductView =
      ProductView(
        name = product.name,
        vendor = product.vendor,
        price = product.price,
        expirationDate = product.expirationDate,
      )
  }

}
