import Dependencies.Libraries.{akkHttpTest, akkaTestKit, scalaTest}
import Dependencies.Versions._
import sbt._

object Dependencies {

  object Versions {
    val slickVersion = "3.3.0"
    val h2DbVersion  = "1.4.197"

    val akkaVersion          = "2.6.8"
    val akkaHttpVersion      = "10.2.4"
    val akkaHttpCirceVersion = "1.35.3"
    val circeVersion         = "0.13.0"

    val logbackVersion      = "1.2.3"
    val scalaLoggingVersion = "3.9.4"
    val pureConfigVersion   = "0.16.0"

    val scalaTestVersion = "3.2.9"

  }

  object Libraries {
    // Db
    val slick = "com.typesafe.slick" %% "slick" % slickVersion
    val h2Db  = "com.h2database"      % "h2"    % h2DbVersion

    // Logs
    val scalaLogging = "com.typesafe.scala-logging" %% "scala-logging"   % Versions.scalaLoggingVersion
    val logback      = "ch.qos.logback"              % "logback-classic" % logbackVersion

    // Akka Framework
    val akkaActors    = "com.typesafe.akka" %% "akka-actor"        % Versions.akkaVersion
    val akkaHttp      = "com.typesafe.akka" %% "akka-http"         % Versions.akkaHttpVersion
    val akkaStream    = "com.typesafe.akka" %% "akka-stream"       % Versions.akkaVersion
    val akkaHttpCirce = "de.heikoseeberger" %% "akka-http-circe"   % Versions.akkaHttpCirceVersion
    val akkHttpTest   = "com.typesafe.akka" %% "akka-http-testkit" % Versions.akkaHttpVersion % Test
    val akkaTestKit   = "com.typesafe.akka" %% "akka-testkit"      % Versions.akkaVersion

    // Circe
    val circeCore    = "io.circe" %% "circe-core"    % Versions.circeVersion
    val circeGeneric = "io.circe" %% "circe-generic" % Versions.circeVersion

    // PureConfig
    val pureConfig = "com.github.pureconfig" %% "pureconfig" % Versions.pureConfigVersion

    // ScalaTest
    val scalaTest = "org.scalatest" %% "scalatest" % Versions.scalaTestVersion

    val basicDeps = Seq(logback, scalaLogging, pureConfig)
    val db        = Seq(slick, h2Db)
    val akka      = Seq(akkaActors, akkaHttp, akkaStream, akkaHttpCirce)
    val circe     = Seq(circeCore, circeGeneric)
    val unitTests = Seq(scalaTest, akkHttpTest, akkaTestKit)
  }

}
