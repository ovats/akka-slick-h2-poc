package com.cc

import akka.actor.ActorSystem
import com.cc.db.dao.ProductDao

import java.time.LocalDate
import java.util.UUID
import scala.concurrent.Future
import scala.util.{Failure, Success}

object Pruebas {

  def sep = println("*" * 80)
  def printMsg(msg: String) = { print(s"\n$msg "); println("-" * 80); println }

  def pruebasDeMetodosDAO = {
    implicit val system: ActorSystem = ActorSystem("CCApiSystem")
    import system.dispatcher

    //ProductDao.createSchema

    val uuid1 = UUID.randomUUID()
    val d1    = LocalDate.parse("2021-07-31")
    val p1    = com.cc.domain.Product(None, "iphone 6", "apple", 10.99, uuid1, expirationDate = Option(d1))
    ProductDao.create(p1).onComplete {
      case Failure(e)  => printMsg(s"Error p1 $e")
      case Success(id) => printMsg(s"Id nuevo p1 $id")
    }

    val uuid2 = UUID.randomUUID()
    val d2    = LocalDate.parse("2021-08-15")
    val p2    = com.cc.domain.Product(None, "iphone7", "apple", 20.99, uuid2, expirationDate = Option(d2))
    ProductDao.create(p2).onComplete {
      case Failure(e)  => printMsg(s"Error p2 $e")
      case Success(id) => printMsg(s"Id nuevo p2 $id")
    }

    val d3 = LocalDate.parse("2021-08-20")
    val p3 = com.cc.domain.Product(None, "iphone8", "apple", 30.99, uuid2, expirationDate = Option(d3))
    ProductDao.create(p3).onComplete {
      case Failure(e)  => printMsg(s"Error p3 $e")
      case Success(id) => printMsg(s"Id nuevo p3 $id")
    }

    val d4 = LocalDate.parse("2021-08-30")
    val p4 =
      com.cc.domain.Product(
        None,
        "dell notebook",
        "dell",
        40.99,
        UUID.randomUUID(),
        expirationDate = Option(d4),
      )
    ProductDao.create(p4).onComplete {
      case Failure(e)  => printMsg(s"Error p4 $e")
      case Success(id) => printMsg(s"Id nuevo p4 $id")
    }

    // Find All
    val listAll: Future[Seq[domain.Product]] = ProductDao.findAll
    listAll.onComplete {
      case Failure(exception) =>
        println(s"Error ${exception.getMessage}")
      case Success(value) =>
        println("Success")
        println(s"findAll: ${value.toArray.mkString("<", "\n", ">")}")
    }

    // find por uuid
    val listUuid1 = ProductDao.findByUUID(uuid1)
    listUuid1.onComplete {
      case Failure(exception) =>
        println(s"Error ${exception.getMessage}")
      case Success(value) =>
        println("Success")
        println(s"findByUUID: $value")
    }

    // find por uuid que no existe
    val listUuidInexistente = ProductDao.findByUUID(UUID.fromString("52ae4ed7-d318-44ac-8c33-a4ad6a3c0d35"))
    listUuidInexistente.onComplete {
      case Failure(exception) =>
        println(s"Error inexistente ${exception.getMessage}")
      case Success(value) =>
        println("Success")
        println(s"findByUUID inexistente: $value")
    }

    // Find by vendor apple
    val listApple: Future[Seq[domain.Product]] = ProductDao.findByVendor("apple")
    listApple.onComplete {
      case Failure(exception) =>
        println(s"Error ${exception.getMessage}")
      case Success(value) =>
        println("Success")
        println(s"findByVendor apple: ${value.toArray.mkString("<", "\n", ">")}")
    }

    // Find by vendor dell
    val listDell: Future[Seq[domain.Product]] = ProductDao.findByVendor("dell")
    listDell.onComplete {
      case Failure(exception) =>
        println(s"Error ${exception.getMessage}")
      case Success(value) =>
        println("Success")
        println(s"findByVendor dell: ${value.toArray.mkString("<", "\n", ">")}")
    }

  }
}
