package com.cc.db

trait DatabaseConfig {
  val profile = slick.jdbc.H2Profile

  import profile.api._

  def db = Database.forConfig("h2db")

}
