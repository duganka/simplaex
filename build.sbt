lazy val versions = new {
  val fs2 = "1.0.4"
  val scallop = "3.2.0"
  val scalatest = "3.0.7"
}

lazy val dependencies = new {
  val fs2 = Vector(
    "co.fs2" %% "fs2-core" % versions.fs2,
    "co.fs2" %% "fs2-io" % versions.fs2
  )

  val scallop = Vector(
    "org.rogach" %% "scallop" % versions.scallop
  )

  val test = Vector(
    "org.scalatest" %% "scalatest" % versions.scalatest % Test
  )
}

lazy val simplaex = (project in file("."))
  .settings(
    organization := "com.simplaex",
    name := "simplaex",
    scalaVersion := "2.12.8",
    scalafmtOnCompile := true,
    libraryDependencies ++= dependencies.fs2 ++ dependencies.scallop ++ dependencies.test
  )
