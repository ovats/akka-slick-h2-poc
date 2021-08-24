package com.cc

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import com.cc.Pruebas.pruebasDeMetodosDAO
import com.cc.config.ApiAppConfig
import com.cc.db.dao.ProductDao
import com.cc.routes.{PingRoutes, ProductRoutes}
import com.cc.services.ProductsService
import com.typesafe.scalalogging.LazyLogging

import scala.util.{Failure, Success}

object CCApiMain extends LazyLogging {

  def main(args: Array[String]): Unit = {

    logger.info("Starting Api ...")

    implicit val system: ActorSystem = ActorSystem("CCApiSystem")
    import system.dispatcher
    implicit val config: ApiAppConfig = ApiAppConfig()

    // We need to create the schema in the db
    ProductDao.createSchema
    //pruebasDeMetodosDAO

    //TODO maybe add a context class for services, routes, config, etc

    // Service layer
    //TODO make it private
    val productsService = new ProductsService()

    // API Rest
    val routes: Route =
      new PingRoutes().routes ~ new ProductRoutes(productsService).routes

    // Start de server
    val host: String = config.http.host
    val port: Int    = config.http.port
    Http()
      .newServerAt(host, port)
      .bind(routes)
      .onComplete {
        case Success(_) => logger.info(s"Started at port $port")
        case Failure(e) => logger.error("Failed to start", e)
      }
  }

}
