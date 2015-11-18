import sbt._
import Keys._
import PlayKeys._

name := """my-mangas"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.11.7"

resolvers += "Local Maven Repository" at Path.userHome.asFile.toURI.toURL + ".m2/repository"

libraryDependencies ++= Seq(
  cache,
  "org.reactivemongo" %% "play2-reactivemongo" % "0.11.7.play24" withSources(),
  "com.typesafe.scala-logging" %% "scala-logging" % "3.1.0" withSources(),
  "com.typesafe.play" %% "play-mailer" % "3.0.1" withSources(),
  "com.typesafe" % "config" % "1.3.0" withSources(),
  "com.logentries" % "logentries-appender" % "1.1.32" withSources(),
  "com.sksamuel.scrimage" %% "scrimage-core" % "2.1.0" withSources(),
  "net.ruippeixotog" %% "scala-scraper" % "0.1.1" withSources(),
  "com.smartfile" % "JavaClient" % "1.0",
  "org.scalatest" % "scalatest_2.11" % "2.2.4" % Test withSources()
)

// Play provides two styles of routers, one expects its actions to be injected, the
// other, legacy style, accesses its actions statically.
routesGenerator := InjectedRoutesGenerator

// run gulp
playRunHooks += RunSubProcess("gulp")