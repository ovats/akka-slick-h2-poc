package com.cc.services

import com.cc.db.dao.ProductDao
import com.cc.domain.{Product, ProductId}
import com.cc.services.ServiceResponse._
import com.cc.view.ProductView
import com.typesafe.scalalogging.LazyLogging

import scala.concurrent.{ExecutionContext, Future}

class ProductsService()(implicit ec: ExecutionContext) extends LazyLogging {

  def addProduct(productData: ProductView): Future[ServiceResponse] = {
    //TODO validations must be implemented here (Cats Validated)
    val newProduct =
      Product(
        id = None,
        name = productData.name,
        vendor = productData.vendor,
        price = productData.price,
        productData.expirationDate,
      )
    ProductDao
      .create(newProduct)
      .map(newId => ProductCreated(newId.toString))
      .recoverWith(handleExceptions(None, "creating"))
  }

  def deleteProduct(productId: ProductId): Future[ServiceResponse] = {
    ProductDao.findById(productId).flatMap {
      case None => Future.successful(ProductNotFound)
      case Some(_) =>
        ProductDao
          .delete(productId)
          .map(_ => ProductDeleted)
          .recoverWith(handleExceptions(Some(productId), "deleting"))
    }
  }

  def getProductById(id: ProductId): Future[ServiceResponse] = {
    ProductDao
      .findById(id)
      .flatMap {
        case Some(productFound) => Future.successful(ProductFound(productFound))
        case None               => Future.successful(ProductNotFound)
      }
      .recoverWith(handleExceptions(Some(id), "retrieving"))
  }

  def getProductsFilteredByVendor(vendorName: String, caseSensitive: String): Future[ServiceResponse] = {
    val cs                           = if (caseSensitive == "no") false else true
    val result: Future[Seq[Product]] = ProductDao.findByVendor(vendorName, cs)
    result
      .map(l => ProductList(l.toList))
      .recoverWith(handleExceptions(None, "retrieving list of products"))
  }

  def getAllProducts(): Future[ServiceResponse] = {
    val result: Future[Seq[Product]] = ProductDao.findAll
    result
      .map(l => ProductList(l.toList))
      .recoverWith(handleExceptions(None, "retrieving list of products"))
  }

  private def handleExceptions(
      maybeId: Option[ProductId],
      action: String,
  ): PartialFunction[Throwable, Future[ServiceResponse]] = {
    case e: Throwable =>
      val uuid     = maybeId.map(x => s"product id ${x.toString}").getOrElse("")
      val errorMsg = s"Error when $action $uuid: ${e.getMessage}"
      logger.error(errorMsg, e)
      Future.successful(UnknownError(errorMsg))
  }
}
