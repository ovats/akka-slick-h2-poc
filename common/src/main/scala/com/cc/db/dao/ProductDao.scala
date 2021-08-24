package com.cc.db.dao

import com.cc.db.BaseDao
import com.cc.domain.{Product, ProductId}
import slick.jdbc.H2Profile.api._

import java.util.UUID
import scala.concurrent.Future

object ProductDao extends BaseDao {

  // Standard CRUD
  def findAll: Future[Seq[Product]]                    = productsTable.result
  def create(product: Product): Future[ProductId]      = productsTable.returning(productsTable.map(_.id)) += product
  def findById(id: ProductId): Future[Option[Product]] = productsTable.filter(_.id === id).result.headOption
  def delete(idProd: ProductId): Future[Int]           = productsTable.filter(_.id === idProd).delete

  // Other operations
  def findByUUID(id: UUID): Future[Option[Product]]          = productsTable.filter(_.uuid === id).result.headOption
  def findByVendor(vendorName: String): Future[Seq[Product]] = productsTable.filter(_.vendor === vendorName).result

  // For creating products table
  def createSchema: Future[Unit] = db.run(productsTable.schema.create)

}
