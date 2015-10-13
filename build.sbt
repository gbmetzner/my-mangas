name := """my-mangas"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

val latestIntegration = "latest.integration"

scalaVersion := "2.11.7"

libraryDependencies ++= Seq(
  cache,
  specs2 % Test,
  "org.reactivemongo" %% "play2-reactivemongo" % latestIntegration withSources(),
  "com.typesafe.scala-logging" %% "scala-logging" % latestIntegration withSources(),
  "com.typesafe.play" %% "play-mailer" % latestIntegration withSources()
)

// only for Play 2.4.x
libraryDependencies ++= Seq(

)

// Play provides two styles of routers, one expects its actions to be injected, the
// other, legacy style, accesses its actions statically.
routesGenerator := InjectedRoutesGenerator
