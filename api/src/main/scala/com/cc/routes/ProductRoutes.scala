package com.cc.routes

import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import com.cc.services.ServiceResponse._
import com.cc.services.{ProductsService, ServiceResponse}
import com.cc.view.ProductView
import com.typesafe.scalalogging.LazyLogging
import io.circe.generic.auto._

import scala.concurrent.Future
import scala.util.{Failure, Success}

class ProductRoutes(productsService: ProductsService) extends LazyLogging with RoutesConfig with ResponseHandler {

  private val createProduct = post {
    pathEndOrSingleSlash {
      logger.debug(s"POST /products")
      entity(as[ProductView]) { productData =>
        val fut = productsService.addProduct(productData)
        onComplete(fut) {
          case Success(ProductCreated(newId)) =>
            complete(StatusCodes.OK, s"New product id: $newId")
          case Success(response) => handleServiceResponse(response, "products")
          case Failure(ex)       => handleFailure(ex, "products")
        }
      }
    }
  }

  private val updateProduct = put {
    path(IntNumber) { prodId =>
      pathEndOrSingleSlash {
        logger.debug(s"PUT /products")
        entity(as[ProductView]) { productData =>
          val fut = productsService.updateProduct(prodId, productData)
          onComplete(fut) {
            case Success(ProductUpdated(p)) => complete(StatusCodes.OK, p.toRestView)
            case Success(ProductNotFound)   => complete(StatusCodes.NotFound, s"Product not found id: $prodId")
            case Success(response)          => handleServiceResponse(response, "products")
            case Failure(ex)                => handleFailure(ex, "products")
          }
        }
      }
    }
  }

  private val deleteProduct = delete {
    path(IntNumber) { prodId =>
      pathEndOrSingleSlash {
        logger.debug(s"DELETE /products/$prodId")
        val fut = productsService.deleteProduct(prodId)
        onComplete(fut) {
          case Success(ProductDeleted)  => complete(StatusCodes.NoContent)
          case Success(ProductNotFound) => complete(StatusCodes.NotFound, s"Product not found id: $prodId")
          case Success(response)        => handleServiceResponse(response, "products")
          case Failure(ex)              => handleFailure(ex, "products")
        }
      }
    }
  }

  private val findProducts = get {
    parameters("vendor".as[String], "case".as[String].withDefault("no")) { (vendorName, caseSensitive) =>
      logger.debug(s"GET /products?vendor=")
      val fut: Future[ServiceResponse] = productsService.getProductsFilteredByVendor(vendorName, caseSensitive)
      onComplete(fut) {
        case Success(ProductList(products)) =>
          complete(StatusCodes.OK, products.map(_.toRestView))
        case Success(response) => handleServiceResponse(response, "products")
        case Failure(ex)       => handleFailure(ex, "products")
      }
    } ~ pathEndOrSingleSlash {
      logger.debug(s"GET /products")
      val fut: Future[ServiceResponse] = productsService.getAllProducts()
      onComplete(fut) {
        case Success(ProductList(products)) =>
          complete(StatusCodes.OK, products.map(_.toRestView))
        case Success(response) => handleServiceResponse(response, "products")
        case Failure(ex)       => handleFailure(ex, "products")
      }
    } ~ path(IntNumber) { id =>
      logger.debug(s"GET /products/$id")
      val fut = productsService.getProductById(id)
      onComplete(fut) {
        case Success(ProductFound(p)) => complete(StatusCodes.OK, p.toRestView)
        case Success(ProductNotFound) =>
          complete(StatusCodes.NotFound, ErrorsResponse(List(s"Not found id: $id")))
        case Success(response) => handleServiceResponse(response, "products")
        case Failure(ex)       => handleFailure(ex, "products")
      }
    }
  }

  val routes: Route = pathPrefix("products") {
    findProducts ~ createProduct ~ deleteProduct ~ updateProduct
  }

}
