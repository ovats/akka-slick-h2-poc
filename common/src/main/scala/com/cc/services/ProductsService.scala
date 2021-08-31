package com.cc.services

import com.cc.config.ApiAppConfig
import com.cc.db.dao.ProductDao
import com.cc.domain.{Product, ProductId}
import com.cc.domain.request.ProductRequest
import com.cc.services.ServiceResponse._
import com.typesafe.scalalogging.LazyLogging

import scala.concurrent.{ExecutionContext, Future}

class ProductsService(config: ApiAppConfig)(implicit ec: ExecutionContext) extends LazyLogging {

  def addProduct(productData: ProductRequest): Future[ServiceResponse] = {
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
      .recoverWith {
        case e: Throwable =>
          val errorMsg = s"Error when creating product: ${e.getMessage}"
          Future.successful(UnknownError(errorMsg))
      }
  }

  def deleteProduct(productId: ProductId): Future[ServiceResponse] = {
    ProductDao.findById(productId).flatMap {
      case None => Future.successful(ProductNotFound)
      case Some(_) =>
        ProductDao
          .delete(productId)
          .map(_ => ProductDeleted)
          .recoverWith {
            case e: Throwable =>
              val errorMsg = s"Error when deleting product: ${e.getMessage}"
              Future.successful(UnknownError(errorMsg))
          }
    }
  }

  def getListOfProducts(vendorName: Option[String]): Future[ServiceResponse] = {
    val result: Future[Seq[Product]] = vendorName match {
      case Some(name) => ProductDao.findByVendor(name, config.general.defaultCaseSensitiveSearch)
      case None       => ProductDao.findAll
    }
    result
      .map(l => ProductList(l.toList))
      .recoverWith {
        case e: Throwable =>
          val errorMsg = s"Error when retrieving list of products: ${e.getMessage}"
          logger.error(errorMsg, e)
          Future.successful(UnknownError(errorMsg))
      }

  }

}
