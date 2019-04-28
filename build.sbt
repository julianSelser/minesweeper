name := "minesweeper"

version := "0.1"

scalaVersion := "2.12.8"

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-actor" % "2.5.13",
  "com.typesafe.akka" %% "akka-stream" % "2.5.13",
  "com.typesafe.akka" %% "akka-http" % "10.1.3",
  "org.scalikejdbc" %% "scalikejdbc" % "3.3.2",
  "com.h2database" % "h2" % "1.4.197",
  "org.scalatest" %% "scalatest" % "3.0.5" % "test"
)
