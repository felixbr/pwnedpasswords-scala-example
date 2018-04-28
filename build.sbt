lazy val root = (project in file("."))
  .settings(
    inThisBuild(
      List(
        scalaVersion := "2.12.6"
      )
    ),
    name := """pwnedpasswords-scala-example""",
    version := "0.1.0",
    libraryDependencies ++= List(
      Dependencies.monix,
      Dependencies.scalajHttp
    )
  )
  .enablePlugins(JavaAppPackaging)

scalacOptions ++= List( // useful compiler flags for scala
  "-deprecation",
  "-encoding",
  "UTF-8", // yes, this is 2 args
  "-feature",
  "-unchecked",
  "-Xfatal-warnings",
  "-Xlint",
  "-Yno-adapted-args",
  "-Ywarn-numeric-widen",
  "-Xfuture",
  "-Ypartial-unification"
)
