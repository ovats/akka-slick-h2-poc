package com.cc.db.dao

import slick.jdbc.H2Profile.api._
import com.cc.domain.{Product, ProductId}

import java.time.LocalDate

class ProductTable(tag: Tag) extends Table[Product](tag, "product") {

  def id             = column[ProductId]("id", O.PrimaryKey, O.AutoInc)
  def name           = column[String]("name")
  def vendor         = column[String]("vendor")
  def price          = column[BigDecimal]("price")
  def expirationDate = column[Option[LocalDate]]("expirationDate")

  def * = (id.?, name, vendor, price, expirationDate).mapTo[Product]
}
