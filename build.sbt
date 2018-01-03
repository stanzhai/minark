name := "minark"

version := "1.0"

scalaVersion := "2.11.5"

lazy val cp = taskKey[String]("dependency class path")

cp := s"java -cp ${(dependencyClasspath in Compile).map(_.files.mkString(":")).value}:${target.value}/scala-${scalaBinaryVersion.value}/classes"

libraryDependencies ++= {
  val akkaVersion = "2.5.8"
  val scalatestVersion = "3.0.1"

  Seq(
    "com.typesafe.akka" %% "akka-remote" % akkaVersion,
    "org.slf4j" % "slf4j-log4j12" % "1.7.21",
    "org.scalatest" %% "scalatest" % scalatestVersion % "test"
  )
}