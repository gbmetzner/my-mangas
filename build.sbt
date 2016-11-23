import play.sbt.PlayImport.PlayKeys._
import sbt.Keys._
import sbt._

name := """my-mangas"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.11.8"

javacOptions ++= Seq("-Xlint:unchecked")

scalacOptions ++= Seq("-unchecked", "-deprecation")

conflictManager := ConflictManager.latestRevision

resolvers ++= Seq(
  "scalaz-bintray" at "http://dl.bintray.com/scalaz/releases",
  "Local Maven Repository" at Path.userHome.asFile.toURI.toURL + ".m2/repository",
  "Sonatype Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots/"
)

lazy val akkaVersion = "2.4.14"

dependencyOverrides += "org.scala-lang" % "scala-library" % scalaVersion.value

dependencyOverrides += "com.typesafe.akka" %% "akka-actor" % akkaVersion

libraryDependencies ++= Seq(
  cache,
  "org.reactivemongo" %% "play2-reactivemongo" % "0.12.0" withSources(),
  "com.typesafe.scala-logging" %% "scala-logging" % "3.5.0" withSources(),
  "com.typesafe.play" %% "play-mailer" % "5.0.0" withSources(),
  "com.typesafe" % "config" % "1.3.1" withSources(),
  "com.logentries" % "logentries-appender" % "1.1.35" withSources(),
  "com.sksamuel.scrimage" %% "scrimage-core" % "2.1.8" withSources(),
  "net.ruippeixotog" %% "scala-scraper" % "1.1.0" withSources(),
  "com.smartfile" % "JavaClient" % "1.0",
  "com.typesafe.akka" %% "akka-actor" % akkaVersion withSources(),
  "org.scalatest" %% "scalatest" % "3.0.1" % Test withSources(),
  "com.typesafe.akka" %% "akka-testkit" % akkaVersion % Test withSources(),
  "org.scalatestplus.play" %% "scalatestplus-play" % "1.5.1" % Test withSources()
)

// run gulp
playRunHooks += RunSubProcess("gulp")

fork in run := true