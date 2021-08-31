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

class ProductRoutesSpec extends BaseSpec with ScalatestRouteTest with FailFastCirceSupport {

  private val config          = ApiAppConfig()
  private val productsService = new ProductsService(config)
  private val productRoutes   = new ProductRoutes(productsService)
  ProductDao.createSchema()

  "POST /products" should "return 200 Ok when adding a new product" in {
    val expirationDate = LocalDate.now().plusDays(1)
    val productRequest =
      ProductView(name = "name", vendor = "vendor", price = 10.99, expirationDate = Option(expirationDate))
    val request = Post(uri = s"/products", productRequest)
    request ~> productRoutes.routes ~> check {
      status shouldBe StatusCodes.OK
    }
  }

  it should "return 200 Ok when adding a new product without expiration date" in {
    val expirationDate = LocalDate.now().plusDays(1)
    val productRequest =
      ProductView(name = "name", vendor = "vendor", price = 10.99, expirationDate = Option(expirationDate))
    val request = Post(uri = s"/products", productRequest)
    request ~> productRoutes.routes ~> check {
      status shouldBe StatusCodes.OK
    }
  }

  override def beforeEach(): Unit = {
    super.beforeEach()
    ProductDao.deleteAll()
  }

}
