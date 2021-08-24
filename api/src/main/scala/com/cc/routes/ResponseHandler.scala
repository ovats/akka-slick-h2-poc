package com.cc.routes

import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives.complete
import akka.http.scaladsl.server.StandardRoute
import com.cc.services.ServiceResponse
import com.cc.services.ServiceResponse.ErrorWhenCreatingProduct
import com.typesafe.scalalogging.LazyLogging
import de.heikoseeberger.akkahttpcirce.FailFastCirceSupport
import io.circe.generic.auto._

trait ResponseHandler extends LazyLogging with FailFastCirceSupport {
  def handleServiceResponse(response: ServiceResponse, entity: String): StandardRoute = {
    response match {
      case ErrorWhenCreatingProduct(errorMsg) =>
        val errorMessage = s"There was an error when creating ($entity): $errorMsg"
        logger.error(errorMessage)
        complete(StatusCodes.InternalServerError, errorMessage)
      case r =>
        val msg = s"Unexpected response ($entity): ${r.toString}"
        logger.error(msg)
        complete(StatusCodes.InternalServerError, ApiResponse(status = "error", message = msg))

    }
  }

  def handleFailure(e: Throwable, entity: String): StandardRoute = {
    val errorMessage = s"Exception has been thrown ($entity)"
    logger.error(errorMessage, e)
    complete(StatusCodes.InternalServerError, ApiResponse(status = "error", message = errorMessage))
  }
}

case class ApiResponse(status: String = "ok", message: String)
