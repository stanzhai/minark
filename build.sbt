name := "minark"

version := "1.0"

scalaVersion := "2.11.5"

libraryDependencies ++= {
  val akkaVersion = "2.4.17"
  val scalatestVersion = "3.0.1"

  Seq(
    "com.typesafe.akka" %% "akka-remote" % akkaVersion,
    "org.slf4j" % "slf4j-log4j12" % "1.7.21",
    "org.scalatest" %% "scalatest" % scalatestVersion % "test"
  )
}