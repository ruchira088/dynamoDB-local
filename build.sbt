import Dependencies._

lazy val root = (project in file(".")).
  settings(
    inThisBuild(List(
      organization := "com.example",
      scalaVersion := "2.12.3",
      version      := "0.1.0"
    )),
    name := "DynamoDB-local",
    libraryDependencies += scalaTest % Test,
    libraryDependencies += "com.amazonaws" % "aws-java-sdk-dynamodb" % "1.11.186",
    libraryDependencies += "com.typesafe.akka" % "akka-actor_2.12" % "2.5.4"
  )
