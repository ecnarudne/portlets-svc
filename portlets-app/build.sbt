import NativePackagerKeys._

name := """portlets-app"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayJava)

scalaVersion := "2.11.1"

libraryDependencies ++= Seq(
  javaJdbc,
  javaEbean,
  cache,
  javaWs,
  //"com.heroku" %% "sbt-heroku" % "0.3.0",
  "be.objectify" %% "deadbolt-java" % "2.3.2",
  // Comment the next line for local development of the Play Authentication core:
  "com.feth" %% "play-authenticate" % "0.6.8",
  "mysql" % "mysql-connector-java" % "5.1.34",
  "com.typesafe.play.plugins" %% "play-plugins-redis" % "2.3.1"
)

maintainer := "Ashish Awasthi"

dockerExposedPorts in Docker := Seq(9000, 9443)

herokuAppName in Compile := "portlets-svc-dev1"

resolvers += Resolver.url("play-easymail (release)", url("http://joscha.github.io/play-easymail/repo/releases/"))(Resolver.ivyStylePatterns)

resolvers += Resolver.url("Objectify Play Repository", url("http://deadbolt.ws/releases/"))(Resolver.ivyStylePatterns)

resolvers += Resolver.url("play-authenticate (release)", url("http://joscha.github.io/play-authenticate/repo/releases/"))(Resolver.ivyStylePatterns)

resolvers += Resolver.url("heroku-sbt-plugin-releases", url("http://dl.bintray.com/heroku/sbt-plugins/"))(Resolver.ivyStylePatterns)

resolvers += "google-sedis-fix" at "http://pk11-scratch.googlecode.com/svn/trunk"
