package com.cc.services

import com.cc.domain.Product

sealed trait ServiceResponse

object ServiceResponse {

  // When a product is created without errors
  final case class ProductCreated(id: String) extends ServiceResponse

  // There was a problem when creating a new product
  final case class ErrorWhenCreatingProduct(msg: String) extends ServiceResponse

  // If price is invalid, name of product is invalid, etc. will return a list of errors:
  final case class ValidationErrors(errors: List[String]) extends ServiceResponse

  // List of products
  final case class ProductList(products: List[Product]) extends ServiceResponse

}
