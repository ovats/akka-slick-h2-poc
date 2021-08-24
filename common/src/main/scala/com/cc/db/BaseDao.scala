package com.cc.db

import com.cc.db.dao.ProductTable
import slick.dbio.NoStream
import slick.lifted.TableQuery
import slick.sql.{FixedSqlStreamingAction, SqlAction}

import scala.concurrent.Future

trait BaseDao extends DatabaseConfig {

  val productsTable = TableQuery[ProductTable]

  //Action must be a subtype of slick.dbio.Effect
  protected implicit def executeFromDb[A](action: SqlAction[A, NoStream, _ <: slick.dbio.Effect]): Future[A] = {
    db.run(action)
  }

  protected implicit def executeReadStreamFromDb[A](
      action: FixedSqlStreamingAction[Seq[A], A, _ <: slick.dbio.Effect]
  ): Future[Seq[A]] = {
    db.run(action)
  }

}
