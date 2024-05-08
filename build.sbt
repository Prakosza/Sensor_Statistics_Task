ThisBuild / version := "1.0"

ThisBuild / scalaVersion := "2.13.14"

enablePlugins(AssemblyPlugin)
libraryDependencies ++= Seq(
  "org.scalatest" %% "scalatest" % "3.2.18" % Test)

lazy val root = (project in file("."))
  .settings(
    name := "Sensor Statistics Task"
  )
