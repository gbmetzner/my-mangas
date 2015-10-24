name := """my-mangas"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

val latestIntegration = "latest.integration"

scalaVersion := "2.11.7"

resolvers += "Local Maven Repository" at Path.userHome.asFile.toURI.toURL + ".m2/repository"

libraryDependencies ++= Seq(
  cache,
  specs2 % Test,
  "org.reactivemongo" %% "play2-reactivemongo" % latestIntegration withSources(),
  "com.typesafe.scala-logging" %% "scala-logging" % latestIntegration withSources(),
  "com.typesafe.play" %% "play-mailer" % latestIntegration withSources(),
  "com.typesafe" % "config" % latestIntegration withSources(),
  "com.smartfile" % "JavaClient" % "1.0",
  "net.ruippeixotog" %% "scala-scraper" % "0.1.1"
)

// Play provides two styles of routers, one expects its actions to be injected, the
// other, legacy style, accesses its actions statically.
routesGenerator := InjectedRoutesGenerator
