import sbt._

object Dependencies {

  object circe {
    val core    = "io.circe" %% "circe-core"    % Version.circe
    val generic = "io.circe" %% "circe-generic" % Version.circe
    val parser  = "io.circe" %% "circe-parser"  % Version.circe
    val java8   = "io.circe" %% "circe-java8"   % Version.circe
    val yaml    = "io.circe" %% "circe-yaml"    % "0.6.1"

    val json = List(core, generic, parser)
    val all  = List(core, generic, parser, java8, yaml)
  }

  object akka {
    val actor   = "com.typesafe.akka" %% "akka-actor"   % Version.akka
    val cluster = "com.typesafe.akka" %% "akka-cluster" % Version.akka
    val stream  = "com.typesafe.akka" %% "akka-stream"  % Version.akka

    val http      = "com.typesafe.akka" %% "akka-http"       % Version.akkaHttp
    val httpCirce = "de.heikoseeberger" %% "akka-http-circe" % "1.19.0"

    val testkit       = "com.typesafe.akka" %% "akka-testkit"        % Version.akka     % "test"
    val streamTestkit = "com.typesafe.akka" %% "akka-stream-testkit" % Version.akka     % "test"
    val httpTestkit   = "com.typesafe.akka" %% "akka-http-testkit"   % Version.akkaHttp % "test"

    val allHttp = List(http, httpCirce, httpTestkit)
  }

  object refined {
    val core       = "eu.timepit" %% "refined"            % Version.refined
    val pureConfig = "eu.timepit" %% "refined-pureconfig" % Version.refined
  }

  val cats  = "org.typelevel" %% "cats"  % "1.0.1"
  val monix = "io.monix"      %% "monix" % "3.0.0-RC1"

  val betterFiles = "com.github.pathikrit" %% "better-files" % "3.0.0"
  val scalajHttp  = "org.scalaj"           %% "scalaj-http"  % "2.3.0"
  val pureConfig  = "com.github.melrief"   %% "pureconfig"   % "0.7.2"
  val pprint      = "com.lihaoyi"          %% "pprint"       % "0.5.0"
  val scalaTest   = "org.scalatest"        %% "scalatest"    % "3.0.1" % "test"
  val scalaCheck  = "org.scalacheck"       %% "scalacheck"   % "1.13.4" % "test"

}

object Version {
  val circe    = "0.9.1"
  val akka     = "2.5.3"
  val akkaHttp = "10.0.9"
  val refined  = "0.8.2"
}
