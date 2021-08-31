import Dependencies.Libraries._

name := "akka-slick-h2-poc"

version := "0.1"

val scalaLangVersion = "2.12.14" //"2.13.6"

lazy val settings = Seq(
  scalaVersion := scalaLangVersion,
  scalacOptions ++= Seq(
    "-unchecked",
    "-feature",
    "-language:implicitConversions",
    "-language:postfixOps",
    "-deprecation",
    "-Xfatal-warnings",
    "-encoding",
    "UTF-8",
    "-Ywarn-dead-code",
  ),
  exportJars := true,
)

lazy val rootProject = project
  .in(file("."))
  .settings(
    name := "slick-poc",
    scalaVersion := scalaLangVersion,
  )
  .aggregate(common, api)

lazy val common = project
  .settings(
    name := "common",
    settings,
    libraryDependencies ++= db ++ basicDeps ++ circe ++ unitTests,
  )

lazy val api = project
  .settings(
    name := "api",
    settings,
    libraryDependencies ++= db ++ basicDeps ++ akka ++ circe ++ unitTests,
  )
  .dependsOn(common)
