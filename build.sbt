import sbt._
import Keys._
import PlayKeys._

name := """my-mangas"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.11.8"

conflictManager := ConflictManager.latestRevision

resolvers += "Local Maven Repository" at Path.userHome.asFile.toURI.toURL + ".m2/repository"

resolvers += "Sonatype Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots/"

libraryDependencies ++= Seq(
  cache,
  "org.reactivemongo" %% "play2-reactivemongo" % "0.11.9-play25-SNAPSHOT" withSources(),
  "com.typesafe.scala-logging" %% "scala-logging" % "3.1.0" withSources(),
  "com.typesafe.play" %% "play-mailer" % "3.0.1" withSources(),
  "com.typesafe" % "config" % "1.3.0" withSources(),
  "com.logentries" % "logentries-appender" % "1.1.32" withSources(),
  "com.sksamuel.scrimage" %% "scrimage-core" % "2.1.0" withSources(),
  "net.ruippeixotog" %% "scala-scraper" % "0.1.1" withSources(),
  "com.smartfile" % "JavaClient" % "1.0",
  "com.typesafe.akka" %% "akka-actor" % "2.4.2" withSources(),
  "org.scalatest" %% "scalatest" % "2.2.6" % Test withSources(),
  "com.typesafe.akka" %% "akka-testkit" % "2.4.2" % Test withSources()
)

// Play provides two styles of routers, one expects its actions to be injected, the
// other, legacy style, accesses its actions statically.
routesGenerator := InjectedRoutesGenerator

// run gulp
playRunHooks += RunSubProcess("gulp")