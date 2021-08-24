package com.cc.services

import com.cc.config.ApiAppConfig
import com.cc.db.dao.ProductDao
import com.cc.domain.Product
import com.cc.domain.request.ProductRequest
import com.cc.services.ServiceResponse._
import com.typesafe.scalalogging.LazyLogging

import java.util.UUID
import scala.concurrent.{ExecutionContext, Future}

class ProductsService(config: ApiAppConfig)(implicit ec: ExecutionContext) extends LazyLogging {

  def addProduct(uuid: UUID, productData: ProductRequest): Future[ServiceResponse] = {
    //TODO validations must be implemented here (Cats Validated)
    val newProduct =
      Product(
        id = None,
        name = productData.name,
        vendor = productData.vendor,
        price = productData.price,
        uuid = uuid,
        productData.expirationDate,
      )
    ProductDao
      .create(newProduct)
      .map(newId => ProductCreated(newId.toString))
      .recoverWith {
        case e: Throwable =>
          val errorMsg = s"Error when creating product uuid $uuid: ${e.getMessage}"
          Future.successful(ErrorWhenCreatingProduct(errorMsg))
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
          Future.successful(ErrorWhenCreatingProduct(errorMsg))
      }

  }

}
