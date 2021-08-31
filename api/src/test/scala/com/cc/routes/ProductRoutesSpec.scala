package com.cc.routes

import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.testkit.ScalatestRouteTest
import com.cc.config.ApiAppConfig
import com.cc.db.dao.ProductDao
import com.cc.services.ProductsService
import com.cc.services.ServiceResponse.ValidationErrors
import com.cc.test.BaseSpec
import com.cc.view.ProductView
import de.heikoseeberger.akkahttpcirce.FailFastCirceSupport
import io.circe.generic.auto._

import java.time.LocalDate
import java.util.UUID

class ProductRoutesSpec extends BaseSpec with ScalatestRouteTest with FailFastCirceSupport {

  private val config          = ApiAppConfig()
  private val productsService = new ProductsService(config)
  private val productRoutes   = new ProductRoutes(productsService)
  ProductDao.createSchema()

  "POST /products/{uuid}" should "return 200 Ok when adding a new product" in {
    val uuid           = UUID.randomUUID()
    val expirationDate = LocalDate.now().plusDays(1)
    val productRequest =
      ProductView(name = "name", vendor = "vendor", price = 10.99, expirationDate = Option(expirationDate))
    val request = Post(uri = s"/products/$uuid", productRequest)
    request ~> productRoutes.routes ~> check {
      status shouldBe StatusCodes.OK
    }
  }

  it should "return 200 Ok when adding a new product without expiration date" in {
    val uuid           = UUID.randomUUID()
    val expirationDate = LocalDate.now().plusDays(1)
    val productRequest =
      ProductView(name = "name", vendor = "vendor", price = 10.99, expirationDate = Option(expirationDate))
    val request = Post(uri = s"/products/$uuid", productRequest)
    request ~> productRoutes.routes ~> check {
      status shouldBe StatusCodes.OK
    }
  }

  it should "return 400 Bad Request when uuid is not valid" in {
    val expirationDate = LocalDate.now().plusDays(1)
    val productRequest =
      ProductView(name = "name", vendor = "vendor", price = 10.99, expirationDate = Option(expirationDate))
    val request = Post(uri = s"/products/1234", productRequest)
    request ~> productRoutes.routes ~> check {
      status shouldBe StatusCodes.BadRequest
    }
  }

  it should "return 409 Conflict when trying to add a product with an uuid already existing" in {
    val uuid           = UUID.randomUUID()
    val expirationDate = LocalDate.now().plusDays(1)
    val productRequest1 =
      ProductView(name = "name", vendor = "vendor", price = 10.99, expirationDate = Option(expirationDate))
    val request1 = Post(uri = s"/products/$uuid", productRequest1)
    request1 ~> productRoutes.routes ~> check {
      status shouldBe StatusCodes.OK
    }

    val productRequest2 =
      ProductView(name = "name2", vendor = "vendor2", price = 28.99, expirationDate = Option(expirationDate))
    val request2 = Post(uri = s"/products/$uuid", productRequest2)
    request2 ~> productRoutes.routes ~> check {
      status shouldBe StatusCodes.Conflict
    }
  }

  override def beforeEach(): Unit = {
    super.beforeEach()
    ProductDao.deleteAll()
  }

}
