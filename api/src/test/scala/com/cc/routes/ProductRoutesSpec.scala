package com.cc.routes

import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.testkit.ScalatestRouteTest
import com.cc.db.dao.ProductDao
import com.cc.domain.Product
import com.cc.services.ProductsService
import com.cc.services.ServiceResponse.ValidationErrors
import com.cc.test.BaseSpec
import com.cc.view.ProductView
import de.heikoseeberger.akkahttpcirce.FailFastCirceSupport
import io.circe.generic.auto._

import java.time.LocalDate

class ProductRoutesSpec extends BaseSpec with ScalatestRouteTest with FailFastCirceSupport {

  private val productsService = new ProductsService()
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

  "DELETE /products/{id}" should "return 204 No Content when deleting a product" in {
    val expirationDate = LocalDate.now().plusDays(1)
    val id = ProductDao
      .create(
        Product(id = None, name = "name", vendor = "vendor", price = 100, expirationDate = Option(expirationDate))
      )
      .futureValue
    val request = Delete(uri = s"/products/$id")
    request ~> productRoutes.routes ~> check {
      status shouldBe StatusCodes.NoContent
    }
  }

  it should "return 404 Not Found when deleting a product" in {
    val request = Delete(uri = s"/products/123456")
    request ~> productRoutes.routes ~> check {
      status shouldBe StatusCodes.NotFound
    }
  }

  "GET /products?vendor=..." should "return the correct list of products" in {
    val expirationDate = LocalDate.now().plusYears(2)
    // format: off
    val pr1 = Product(id = None, name = "iphone 6", vendor = "apple", price = 199.99, expirationDate = Option(expirationDate))
    val id1 = ProductDao.create(pr1).futureValue
    val pr2 = Product(id = None, name = "iphone 7", vendor = "apple", price = 399.99, expirationDate = Option(expirationDate))
    val id2 = ProductDao.create(pr2).futureValue
    val pr3 = Product(id = None, name = "iphone 8", vendor = "apple", price = 699.99, expirationDate = Option(expirationDate))
    val id3 = ProductDao.create(pr3).futureValue
    val pr4 = Product(id = None, name = "notebook", vendor = "dell", price = 999.99, expirationDate = Option(expirationDate))
    val id4 = ProductDao.create(pr4).futureValue
    // format: on

    val request1 = Get(uri = s"/products?vendor=apple")
    request1 ~> productRoutes.routes ~> check {
      status shouldBe StatusCodes.OK
      val appleList = responseAs[List[ProductView]]
      appleList should contain theSameElementsAs List(pr1.toRestView, pr2.toRestView, pr3.toRestView)
      appleList.length shouldBe 3
    }

    val request2 = Get(uri = s"/products?vendor=dell")
    request2 ~> productRoutes.routes ~> check {
      status shouldBe StatusCodes.OK
      val appleList = responseAs[List[ProductView]]
      appleList shouldBe List(pr4.toRestView)
      appleList.length shouldBe 1
    }

    val request3 = Get(uri = s"/products?vendor=bmw")
    request3 ~> productRoutes.routes ~> check {
      status shouldBe StatusCodes.OK
      val appleList = responseAs[List[ProductView]]
      appleList shouldBe List()
      appleList.length shouldBe 0
    }

  }

  it should "return an empty list when there is no product with that vendor" in {
    val expirationDate = LocalDate.now().plusYears(2)
    // format: off
    val pr1 = Product(id = None, name = "iphone 6", vendor = "apple", price = 199.99, expirationDate = Option(expirationDate))
    val id1 = ProductDao.create(pr1).futureValue
    val pr2 = Product(id = None, name = "iphone 7", vendor = "apple", price = 399.99, expirationDate = Option(expirationDate))
    val id2 = ProductDao.create(pr2).futureValue
    val pr3 = Product(id = None, name = "iphone 8", vendor = "apple", price = 699.99, expirationDate = Option(expirationDate))
    val id3 = ProductDao.create(pr3).futureValue
    val pr4 = Product(id = None, name = "notebook", vendor = "dell", price = 999.99, expirationDate = Option(expirationDate))
    val id4 = ProductDao.create(pr4).futureValue
    // format: on

    val request = Get(uri = s"/products?vendor=bmw")
    request ~> productRoutes.routes ~> check {
      status shouldBe StatusCodes.OK
      val appleList = responseAs[List[ProductView]]
      appleList shouldBe List()
      appleList.length shouldBe 0
    }

  }

  "GET /products" should "return the list of all products" in {
    val expirationDate = LocalDate.now().plusYears(2)
    // format: off
    val pr1 = Product(id = None, name = "iphone 6", vendor = "apple", price = 199.99, expirationDate = Option(expirationDate))
    val id1 = ProductDao.create(pr1).futureValue
    val pr2 = Product(id = None, name = "iphone 7", vendor = "apple", price = 399.99, expirationDate = Option(expirationDate))

    val id2 = ProductDao.create(pr2).futureValue
    val pr3 = Product(id = None, name = "iphone 8", vendor = "apple", price = 699.99, expirationDate = Option(expirationDate))
    val id3 = ProductDao.create(pr3).futureValue
    // format: on

    val request = Get(uri = s"/products")
    request ~> productRoutes.routes ~> check {
      status shouldBe StatusCodes.OK
      val appleList = responseAs[List[ProductView]]
      appleList should contain theSameElementsAs List(pr1.toRestView, pr2.toRestView, pr3.toRestView)
      appleList.length shouldBe 3
    }
  }

  it should "return an empty list when there are not products" in {
    Get(uri = s"/products") ~> productRoutes.routes ~> check {
      status shouldBe StatusCodes.OK
      val list = responseAs[List[ProductView]]
      list shouldBe List()
      list.length shouldBe 0
    }
  }

  "GET /products/{uuid}" should "return the correct product" in {
    val expirationDate = LocalDate.now().plusYears(2)
    // format: off
    val pr1 = Product(id = None, name = "iphone 6", vendor = "apple", price = 199.99, expirationDate = Option(expirationDate))
    val id1 = ProductDao.create(pr1).futureValue
    val pr2 = Product(id = None, name = "iphone 7", vendor = "apple", price = 399.99, expirationDate = Option(expirationDate))

    val id2 = ProductDao.create(pr2).futureValue
    val pr3 = Product(id = None, name = "iphone 8", vendor = "apple", price = 699.99, expirationDate = Option(expirationDate))
    val id3 = ProductDao.create(pr3).futureValue
    // format: on

    val request1 = Get(uri = s"/products/$id1")
    request1 ~> productRoutes.routes ~> check {
      status shouldBe StatusCodes.OK
      responseAs[ProductView] shouldBe pr1.toRestView
    }

    val request2 = Get(uri = s"/products/$id2")
    request2 ~> productRoutes.routes ~> check {
      status shouldBe StatusCodes.OK
      responseAs[ProductView] shouldBe pr2.toRestView
    }

    val request3 = Get(uri = s"/products/$id3")
    request3 ~> productRoutes.routes ~> check {
      status shouldBe StatusCodes.OK
      responseAs[ProductView] shouldBe pr3.toRestView
    }
  }

  it should "return 404 Not found when there are no products" in {
    val request = Get(uri = s"/products/1")
    request ~> productRoutes.routes ~> check {
      status shouldBe StatusCodes.NotFound
    }
  }

  it should "return 404 Not found when the product does not exist" in {
    val expirationDate = LocalDate.now().plusYears(2)
    val pr1 = ProductDao
      .create(
        Product(id = None, name = "iphone 6", vendor = "apple", price = 199.99, expirationDate = Option(expirationDate))
      )
      .futureValue

    val request = Get(uri = s"/products/${pr1 + 1}")
    request ~> productRoutes.routes ~> check {
      status shouldBe StatusCodes.NotFound
    }
  }

  override def beforeEach(): Unit = {
    super.beforeEach()
    ProductDao.deleteAll()
  }

}
