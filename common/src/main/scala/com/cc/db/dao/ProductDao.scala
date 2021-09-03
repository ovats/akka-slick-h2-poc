package com.cc.db.dao

import com.cc.db.BaseDao
import com.cc.domain.{Product, ProductId}
import slick.jdbc.H2Profile.api._

import scala.concurrent.Future

object ProductDao extends BaseDao {

  // Standard CRUD
  def create(product: Product): Future[ProductId] = productsTable.returning(productsTable.map(_.id)) += product

  //TODO this must be improved, remove using "db.run()" method.
  //def update(product: Product)                         = productsTable.map(_.id === product.id).update()
  def update(product: Product) = {
    val q = for {
      l <- productsTable if l.id === product.id
    } yield (l.name, l.vendor, l.expirationDate, l.price)
    db.run(q.update(product.name, product.vendor, product.expirationDate, product.price))
  }

  def findById(id: ProductId): Future[Option[Product]] = productsTable.filter(_.id === id).result.headOption
  def delete(idProd: ProductId): Future[Int]           = productsTable.filter(_.id === idProd).delete
  def findAll: Future[Seq[Product]]                    = productsTable.result

  // Other operations
  def findByVendor(vendorName: String, caseSensitive: Boolean): Future[Seq[Product]] = {
    if (caseSensitive)
      productsTable.filter(_.vendor === vendorName).result
    else
      productsTable.filter(_.vendor.toLowerCase === vendorName.toLowerCase()).result
  }

  def createSchema(): Future[Unit] = db.run(productsTable.schema.create)
  def deleteAll(): Future[Int]     = db.run(productsTable.delete)

}
