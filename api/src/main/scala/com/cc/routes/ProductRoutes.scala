package com.cc.routes

import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import com.cc.domain.request.ProductRequest
import com.cc.services.ServiceResponse._
import com.cc.services.{ProductsService, ServiceResponse}
import com.typesafe.scalalogging.LazyLogging

import java.util.UUID
import scala.concurrent.Future
import scala.util.{Failure, Success, Try}

class ProductRoutes(productsService: ProductsService) extends LazyLogging with RoutesConfig with ResponseHandler {

  private val createProduct = post {
    path(Segment) { productUUID =>
      pathEndOrSingleSlash {
        logger.debug(s"POST /products/$productUUID")
        Try(UUID.fromString(productUUID)) match {
          case Failure(_) => complete(StatusCodes.BadRequest, s"Invalid uuid: $productUUID")
          case Success(validUuid) =>
            entity(as[ProductRequest]) { productData =>
              val fut = productsService.addProduct(validUuid, productData)
              onComplete(fut) {
                case Success(ProductCreated(newId)) =>
                  complete(StatusCodes.OK, s"New product id: $newId")
                case Success(response) => handleServiceResponse(response, "products")
                case Failure(ex)       => handleFailure(ex, "products")
              }
            }
        }
      }
    }
  }

  private val findProducts = get {
    pathEndOrSingleSlash {
      parameters('vendor ?) { vendorName =>
        logger.debug(s"GET /products?vendor=")
        val fut: Future[ServiceResponse] = productsService.getListOfProducts(vendorName)
        onComplete(fut) {
          case Success(ProductList(products)) =>
            complete(StatusCodes.OK, products.map(_.toProductView))
          case Success(response) => handleServiceResponse(response, "products")
          case Failure(ex)       => handleFailure(ex, "products")
        }
      }
    }
  }

  val routes: Route = pathPrefix("products") {
    findProducts ~ createProduct
  }

}
