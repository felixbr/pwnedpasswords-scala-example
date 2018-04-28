lazy val root = (project in file("."))
  .settings(
    inThisBuild(
      List(
        scalaVersion := "2.12.2"
      )
    ),
    name := """pwnedpasswords-scala""",
    version := "0.1.0",
    libraryDependencies ++= List(
      Dependencies.monix,
      Dependencies.betterFiles,
      Dependencies.scalaTest,
      Dependencies.scalaCheck
    ) ++ Dependencies.circe.json
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
  "-Xfuture"
)
