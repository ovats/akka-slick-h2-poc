package com.cc.domain

import java.time.LocalDate

final case class Product(
    id: Option[ProductId],
    name: String,
    vendor: String,
    price: BigDecimal,
    expirationDate: Option[LocalDate],
)
